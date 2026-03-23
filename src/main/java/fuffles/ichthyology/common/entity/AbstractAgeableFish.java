package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.mixin.common.advancement.SimpleCriterionTriggerAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

//I'd imagine this will be more compatible with mods that do stuff with mobs that can breed than copy-pasting AgeableMob into AbstractModFish
public abstract class AbstractAgeableFish extends AgeableMob implements Bucketable
{
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractAgeableFish.class, EntityDataSerializers.BOOLEAN);;
    private static final Object2ObjectMap<EntityType<?>, ObjectObjectImmutablePair<ResourceLocation, ResourceLocation>> ADULT_BABY_LOOT_TABLE_CACHE = new Object2ObjectOpenHashMap<>();
    protected static final int PARENT_AGE_AFTER_BREEDING = 6000;
    private int inLove;
    @Nullable
    private UUID loveCause;

    public AbstractAgeableFish(EntityType<? extends AbstractAgeableFish> entityType, Level level)
    {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public abstract boolean hasCustomBabyDrops();

    @Override
    protected boolean shouldDropLoot()
    {
        return this.hasCustomBabyDrops() || super.shouldDropLoot();
    }

    public static ResourceLocation lootTableLocation(EntityType<?> type, boolean forBaby)
    {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        return new ResourceLocation(id.getNamespace(), "entities/" + id.getPath() + "/" + (forBaby ? "baby" : "adult"));
    }

    @Override
    protected ResourceLocation getDefaultLootTable()
    {
        if (this.hasCustomBabyDrops())
        {
            var adultBabyPair = ADULT_BABY_LOOT_TABLE_CACHE.computeIfAbsent(this.getType(), type -> new ObjectObjectImmutablePair<>(lootTableLocation(this.getType(), false), lootTableLocation(this.getType(), true)));
            return this.isBaby() ? adultBabyPair.right() : adultBabyPair.left();
        }
        else
        {
            return super.getDefaultLootTable();
        }
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    public MobType getMobType()
    {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level)
    {
        return level.isUnobstructed(this);
    }

    @Override
    public int getAmbientSoundInterval()
    {
        return 120;
    }

    @Override
    public int getExperienceReward()
    {
        return 1 + this.level().random.nextInt(3);
    }

    protected void handleAirSupply(int airSupply)
    {
        if (this.isAlive() && !this.isInWaterOrBubble())
        {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20)
            {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        }
        else
        {
            this.setAirSupply(300);
        }

    }

    @Override
    public void baseTick()
    {
        int i = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(i);
    }

    @Override
    public boolean isPushedByFluid()
    {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player)
    {
        return false;
    }

    /*public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> pWaterAnimal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        int $$5 = pLevel.getSeaLevel();
        int $$6 = $$5 - 13;
        return pPos.getY() >= $$6 && pPos.getY() <= $$5 && pLevel.getFluidState(pPos.below()).is(FluidTags.WATER) && pLevel.getBlockState(pPos.above()).is(Blocks.WATER);
    }*/

    //

    @Override
    protected void customServerAiStep()
    {
        if (this.getAge() != 0)
            this.inLove = 0;
        super.customServerAiStep();
    }

    /*@Override
    public void aiStep()
    {
        super.aiStep();
        if (this.getAge() != 0) {
            this.inLove = 0;
        }

        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX((double)1.0F), this.getRandomY() + (double)0.5F, this.getRandomZ((double)1.0F), d0, d1, d2);
            }
        }

    }*/

    public boolean hurt(DamageSource source, float pAmount)
    {
        if (this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            this.inLove = 0;
            return super.hurt(source, pAmount);
        }
    }

    public double getMyRidingOffset()
    {
        return 0.14;
    }

    public abstract boolean isFood(ItemStack pStack);

    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack)
    {
        if (!player.getAbilities().instabuild)
            stack.shrink(1);
    }

    public boolean canFallInLove()
    {
        return this.inLove <= 0;
    }

    public void setInLove(@Nullable Player player)
    {
        this.inLove = 600;
        if (player != null)
            this.loveCause = player.getUUID();
        this.level().broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
    }

    public void setInLoveTime(int inLove)
    {
        this.inLove = inLove;
    }

    public int getInLoveTime()
    {
        return this.inLove;
    }

    @Nullable
    public ServerPlayer getLoveCause()
    {
        if (this.loveCause == null)
        {
            return null;
        }
        else
        {
            Player player = this.level().getPlayerByUUID(this.loveCause);
            return player instanceof ServerPlayer ? (ServerPlayer)player : null;
        }
    }

    public boolean isInLove()
    {
        return this.inLove > 0;
    }

    public void resetLove()
    {
        this.inLove = 0;
    }

    public boolean canMate(AbstractAgeableFish otherFeesh)
    {
        return otherFeesh != this && otherFeesh.getClass() == this.getClass() && this.isInLove() && otherFeesh.isInLove();
    }

    public void spawnChildFromBreeding(ServerLevel level, AbstractAgeableFish mate)
    {
        AgeableMob ageablemob = this.getBreedOffspring(level, mate);
        BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this, mate, ageablemob);
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            this.setAge(PARENT_AGE_AFTER_BREEDING);
            mate.setAge(PARENT_AGE_AFTER_BREEDING);
            this.resetLove();
            mate.resetLove();
        }
        else if (event.getChild() != null)
        {
            event.getChild().setBaby(true);
            event.getChild().moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            this.finalizeSpawnChildFromBreeding(level, mate, event.getChild());
            level.addFreshEntity(event.getChild());
        }
    }

    public void finalizeSpawnChildFromBreeding(ServerLevel pLevel, AbstractAgeableFish mate, @Nullable AgeableMob baby)
    {
        Optional.ofNullable(this.getLoveCause()).or(() -> Optional.ofNullable(mate.getLoveCause())).ifPresent((breedingInstagator) -> {
            breedingInstagator.awardStat(Stats.ANIMALS_BRED);
            AbstractAgeableFish.triggerBredAnimalsCriteria(breedingInstagator, this, mate, baby);
        });
        this.setAge(PARENT_AGE_AFTER_BREEDING);
        mate.setAge(PARENT_AGE_AFTER_BREEDING);
        this.resetLove();
        mate.resetLove();
        pLevel.broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            pLevel.addFreshEntity(new ExperienceOrb(pLevel, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }

    }

    @SuppressWarnings("unchecked")
    public static void triggerBredAnimalsCriteria(ServerPlayer player, AbstractAgeableFish parent, AbstractAgeableFish partner, @Nullable AgeableMob child)
    {
        LootContext parentContext = EntityPredicate.createContext(player, parent);
        LootContext partnerContext = EntityPredicate.createContext(player, partner);
        LootContext childContext = child != null ? EntityPredicate.createContext(player, child) : null;
        ((SimpleCriterionTriggerAccessor<BredAnimalsTrigger.TriggerInstance>)CriteriaTriggers.BRED_ANIMALS).ichthyology$trigger(player, instance -> instance.matches(parentContext, partnerContext, childContext));
    }

    private void emitLoveHeart()
    {
        double dx = this.random.nextGaussian() * 0.02;
        double dy = this.random.nextGaussian() * 0.02;
        double dz = this.random.nextGaussian() * 0.02;
        this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0F), this.getRandomY() + (double)0.5F, this.getRandomZ(1.0F), dx, dy, dz);
    }

    public void handleEntityEvent(byte id)
    {
        if (id == EntityEvent.IN_LOVE_HEARTS)
        {
            for(int i = 0; i < 7; i++)
            {
                this.emitLoveHeart();
            }
        }
        else
        {
            super.handleEntityEvent(id);
        }
    }

    //

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size)
    {
        return size.height * 0.65F;
    }

    @Override
    public boolean requiresCustomPersistence()
    {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer)
    {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public int getMaxSpawnClusterSize()
    {
        return 8;
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    public boolean fromBucket()
    {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket)
    {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound)
    {
        super.addAdditionalSaveData(compound);
        this.commonSaveToTag(compound, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound)
    {
        super.readAdditionalSaveData(compound);
        this.commonLoadFromTag(compound, false);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25F));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6, 1.4, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(4, new AbstractAgeableFish.FishSwimGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level)
    {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector)
    {
        if (this.isEffectiveAi() && this.isInWater())
        {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null)
            {
                this.setDeltaMovement(this.getDeltaMovement().add((double)0.0F, -0.005, (double)0.0F));
            }
        }
        else
        {
            super.travel(travelVector);
        }
    }

    @Override
    public void aiStep()
    {
        if (!this.isInWater() && this.onGround() && this.verticalCollision)
        {
            this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
            this.setOnGround(false);
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }

        if (this.getAge() != 0)
            this.inLove = 0;

        // Interesting, this is technically also bugged in vanilla?
        if (this.inLove > 0)
        {
            --this.inLove;
            if (this.inLove % 10 == 0)
            {
                this.emitLoveHeart();
            }
        }
        super.aiStep();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        if (this.isBucketable(player, hand))
        {
            Optional<InteractionResult> bucketableResult = Bucketable.bucketMobPickup(player, hand, this);
            if (bucketableResult.isPresent())
                return bucketableResult.get();
        }
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.isFood(itemstack))
        {
            int i = this.getAge();
            if (!this.level().isClientSide && i == 0 && this.canFallInLove())
            {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby())
            {
                this.usePlayerItem(player, hand, itemstack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.level().isClientSide)
                return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    public boolean isBucketable(Player player, InteractionHand hand)
    {
        return true;
    }

    @Override
    public void saveToBucketTag(ItemStack stack)
    {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        this.commonSaveToTag(stack.getOrCreateTag(), true);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag)
    {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.commonLoadFromTag(tag, true);
    }

    protected void commonSaveToTag(CompoundTag tag, boolean bucketTag)
    {
        if (!bucketTag)
        {
            tag.putBoolean("FromBucket", this.fromBucket());
            tag.putInt("InLove", this.inLove);
            if (this.loveCause != null)
                tag.putUUID("LoveCause", this.loveCause);
        }
        tag.putInt("Age", this.getAge());
    }

    protected void commonLoadFromTag(CompoundTag tag, boolean bucketTag)
    {
        if (!bucketTag)
        {
            this.setFromBucket(tag.getBoolean("FromBucket"));
            this.inLove = tag.getInt("InLove");
            this.loveCause = tag.hasUUID("LoveCause") ? tag.getUUID("LoveCause") : null;
        }
        this.setAge(tag.getInt("Age"));
    }

    @Override
    public SoundEvent getPickupSound()
    {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    protected boolean canRandomSwim()
    {
        return true;
    }

    protected abstract SoundEvent getFlopSound();

    @Override
    protected SoundEvent getSwimSound()
    {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) { }

    static class FishSwimGoal extends RandomSwimmingGoal
    {
        private final AbstractAgeableFish fish;

        public FishSwimGoal(AbstractAgeableFish fish)
        {
            super(fish, 1.0F, 40);
            this.fish = fish;
        }

        public boolean canUse()
        {
            return this.fish.canRandomSwim() && super.canUse();
        }
    }

    public static boolean checkSurfaceAgeableFishSpawnRules(EntityType<? extends AbstractAgeableFish> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random)
    {
        int max = level.getSeaLevel();
        int min = max - 13;
        return pos.getY() >= min && pos.getY() <= max && level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(Blocks.WATER);
    }
}
