package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.function.IntFunction;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Olm extends Animal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Olm.Variant> VARIANT_ID = SynchedEntityData.defineId(Olm.class, ModEntityDataSerializers.OLM_VARIANT);
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.BOOLEAN);
	public static final String TAG_VARIANT = "Variant";
	int layEggsCounter;

	@SuppressWarnings("deprecation")
	public Olm(EntityType<? extends Animal> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
		this.moveControl = new MoveHelperController(this);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
		this.setMaxUpStep(1);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new OlmBreedGoal(this, 1));
		this.goalSelector.addGoal(1, new OlmLayEggsGoal(this, 1));
	}

	public static int createTagToFishType(CompoundTag tag) {
		Olm.Variant variant = Olm.Variant.readVariant(tag);
		return variant != null ? variant.getId() : 0;
	}

	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle) {
		if (stack.hasTag()) {
			Olm.Variant variant = Olm.Variant.readVariant(stack.getTag());
			if (variant != null) {
				tooltipComponents.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
				return true;
			}
		}
		return false;
	}

	public boolean hasEggs() {
		return this.entityData.get(HAS_EGGS);
	}

	void setHasEggs(boolean pHasEggs) {
		this.entityData.set(HAS_EGGS, pHasEggs);
	}

	public boolean isLayingEggs() {
		return this.entityData.get(LAYING_EGGS);
	}

	void setLayingEggs(boolean pIsLayingEggs) {
		this.layEggsCounter = pIsLayingEggs ? 1 : 0;
		this.entityData.set(LAYING_EGGS, pIsLayingEggs);
	}

	public boolean isFood(ItemStack pStack) {
		return pStack.getTag().contains("entity.ichthyology.crayfish.cave") && pStack.is(ModItems.CRAYFISH);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAS_EGGS, false);
		this.entityData.define(LAYING_EGGS, false);
		this.entityData.define(FROM_BUCKET, false);
		this.entityData.define(VARIANT_ID, Olm.Variant.WHITE);
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("HasEggs", this.hasEggs());
		pCompound.putBoolean("FromBucket", this.fromBucket());
		this.getVariant().writeVariant(pCompound);
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setHasEggs(pCompound.getBoolean("HasEggs"));
		this.setFromBucket(pCompound.getBoolean("FromBucket"));
		Olm.Variant variant = Olm.Variant.readVariant(pCompound);
		if (variant != null) this.setVariant(variant);
	}

	public boolean canFallInLove() {
		return super.canFallInLove() && !this.hasEggs();
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.OLM_BUCKET);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveToBucketTag(ItemStack pStack) {
		Bucketable.saveDefaultDataToBucketTag(this, pStack);
		this.getVariant().writeVariant(pStack.getOrCreateTag());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadFromBucketTag(CompoundTag pTag) {
		Bucketable.loadDefaultDataFromBucketTag(this, pTag);
		Olm.Variant variant = Olm.Variant.readVariant(pTag);
		if (variant != null) this.setVariant(variant);
		else this.setVariant(Olm.Variant.byId(this.level().getRandom().nextInt(Olm.Variant.VALUES.length)));
	}

	public Olm.Variant getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Olm.Variant variant) {
		this.entityData.set(VARIANT_ID, variant);
	}



	public static enum Variant {
		WHITE("white", Ichthyology.id("textures/entity/olm/white.png")),
		BLACK("black", Ichthyology.id("textures/entity/olm/black.png"));

		public static final Olm.Variant[] VALUES = values();
		private static final IntFunction<Olm.Variant> BY_ID = ByIdMap.continuous(Olm.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

		private final String name;
		private final ResourceLocation texture;

		private Variant(String name, ResourceLocation texture) {
			this.name = name;
			this.texture = texture;
		}

		public int getId() {
			return this.ordinal();
		}

		public String getName() {
			return this.name;
		}

		@OnlyIn(Dist.CLIENT)
		public ResourceLocation getTexture() {
			return this.texture;
		}

		public String getVariantDescriptionId() {
			return "entity." + Ichthyology.ID + ".olm.variant." + this.getName();
		}

		public void writeVariant(CompoundTag tag) {
			tag.putString(TAG_VARIANT, this.getName());
		}

		public static Olm.Variant byId(int i) {
			return BY_ID.apply(i);
		}

		@Nullable
		public static Olm.Variant byData(String name) {
			for (Olm.Variant variant : VALUES) {
				if (variant.getName().equals(name)) {
					return variant;
				}
			}
			return null;
		}

		@Nullable
		public static Olm.Variant readVariant(CompoundTag tag) {
			return byData(tag.getString(TAG_VARIANT));
		}
	}

	@Override
	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	@Override
	public void setFromBucket(boolean pFromBucket) {
		this.entityData.set(FROM_BUCKET, pFromBucket);		
	}

	@Override
	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
	}

	public void baseTick() {
		int i = this.getAirSupply();
		super.baseTick();
		if (!this.isNoAi()) {
			this.handleAirSupply(i);
		}
	}

	protected void handleAirSupply(int pAirSupply) {
		if (this.isAlive() && !this.isInWaterRainOrBubble()) {
			this.setAirSupply(pAirSupply - 1);
			if (this.getAirSupply() == -20) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().dryOut(), 2.0F);
			}
		} else {
			this.setAirSupply(this.getMaxAirSupply());
		}
	}

	public MobType getMobType() {
		return MobType.WATER;
	}

	public void rehydrate() {
		int i = this.getAirSupply() + 1800;
		this.setAirSupply(Math.min(i, this.getMaxAirSupply()));
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public boolean isPushedByFluid() {
		return false;
	}

	protected PathNavigation createNavigation(Level pLevel) {
		return new WaterBoundPathNavigation(this, pLevel);
	}

	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence();
	}

	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return !this.hasCustomName();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FOLLOW_RANGE, 10).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.ARMOR, 2);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		return null;
	}

	static class OlmBreedGoal extends BreedGoal {
		private final Olm olm;

		OlmBreedGoal(Olm olm, double speedModifier) {
			super(olm, speedModifier);
			this.olm = olm;
		}

		public boolean canUse() {
			return super.canUse() && !olm.hasEggs();
		}

		protected void breed() {
			ServerPlayer serverPlayer = this.animal.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null) {
				serverPlayer = this.partner.getLoveCause();
			}
			if (serverPlayer != null) {
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
				CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this.animal, this.partner, (AgeableMob)null);
			}
			this.olm.setHasEggs(true);
			this.animal.setAbsorptionAmount(6000);
			this.partner.setAbsorptionAmount(6000);
			this.animal.resetLove();
			this.partner.resetLove();
			RandomSource randomSource = this.animal.getRandom();
			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), randomSource.nextInt(7) + 1));
			}
		}
	} 

	static class OlmLayEggsGoal extends MoveToBlockGoal {
		private final Olm olm;

		OlmLayEggsGoal(Olm olm, double speedModifier) {
			super(olm, speedModifier, 16);
			this.olm = olm;
		}


		public double acceptedDistance() {
			return 1D;
		}

		public boolean canUse() {
			return olm.hasEggs() ? super.canUse() : false;
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && olm.hasEggs();
		}

		public void tick() {
			super.tick();
			BlockPos blockpos = olm.blockPosition();
			if (this.isReachedTarget()) {
				if (olm.layEggsCounter < 1) {
					olm.setLayingEggs(true);
				} else if (olm.layEggsCounter > this.adjustedTickDelay(200)) {
					Level level = olm.level();
					level.playSound((Player)null, blockpos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
					BlockState blockstate = ModBlocks.OLM_EGGS.defaultBlockState();
					level.setBlock(blockPos, blockstate, 3);
					olm.setHasEggs(false);
					olm.setLayingEggs(false);
					olm.setInLoveTime(600);
				}
				if (olm.isLayingEggs()) {
					++olm.layEggsCounter;
				}
			}
		}

		protected boolean isValidTarget(LevelReader level, BlockPos pos) {
			Block block = level.getBlockState(pos).getBlock();
			return block == Blocks.WATER && (level.getBlockState(pos.below()).is(Blocks.DRIPSTONE_BLOCK) || level.getBlockState(pos.below()).is(Blocks.CALCITE)) && level.getBrightness(LightLayer.BLOCK, pos) <= 8;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean checkOlmSpawnRules(EntityType<? extends LivingEntity> p_217018_, ServerLevelAccessor p_217019_, MobSpawnType p_217020_, BlockPos p_217021_, RandomSource p_217022_) {
		return p_217021_.getY() <= p_217019_.getSeaLevel() - 33 && p_217019_.getRawBrightness(p_217021_, 0) == 0 && p_217019_.getBlockState(p_217021_).is(Blocks.WATER);
	}

	static class MoveHelperController extends MoveControl {
		private final Olm fish;

		MoveHelperController(Olm fish) {
			super(fish);
			this.fish = fish;
		}

		@SuppressWarnings("deprecation")
		public void tick() {
			if (this.fish.isEyeInFluid(FluidTags.WATER)) {
				this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}

			if (this.operation == MoveControl.Operation.MOVE_TO && !this.fish.getNavigation().isDone()) {
				float f = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
				this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), f));
				double d0 = this.wantedX - this.fish.getX();
				double d2 = this.wantedZ - this.fish.getZ();
				if (d0 != 0.0D || d2 != 0.0D) {
					float f1 = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
					this.fish.setYRot(this.rotlerp(this.fish.getYRot(), f1, 90.0F));
					this.fish.yBodyRot = this.fish.getYRot();
					if (this.fish.horizontalCollision && this.fish.level().getBlockState(this.fish.blockPosition().above()).getBlock() == Blocks.WATER) {
						this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.025D, 0.0D));
					}
				}
			} else {
				this.fish.setSpeed(0.0F);
			}
		}
	}

	protected SoundEvent getSwimSound() {
		return SoundEvents.FISH_SWIM;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		if (reason == MobSpawnType.BUCKET) return spawnData;
		else {
			if (this.getRandom().nextInt(100) == 0) this.setVariant(Variant.BLACK);
			else this.setVariant(Variant.WHITE);
			return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
		}
	}

}
