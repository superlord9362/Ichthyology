package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Crayfish extends WaterAnimal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Crayfish.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Crayfish.Variant> VARIANT_ID = SynchedEntityData.defineId(Crayfish.class, ModEntityDataSerializers.CRAYFISH_VARIANT);
	private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(Crayfish.class, EntityDataSerializers.INT);
	public static final String TAG_VARIANT = "Variant";
	private float meleeProgress = 0.0F;
	private float prevMeleeProgress = 0.0F;

	@SuppressWarnings("deprecation")
	public Crayfish(EntityType<? extends Crayfish> entityType, Level level) {
		super(entityType, level);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
		this.moveControl = new MoveHelperController(this);
		this.lookControl = new SmoothSwimmingLookControl(this, 20);
		this.setMaxUpStep(1);
	}

	public int getMaxAirSupply() {
		return 6000;
	}
	
	public static int createTagToFishType(CompoundTag tag) {
		Crayfish.Variant variant = Crayfish.Variant.readVariant(tag);
		return variant != null ? variant.getId() : 0;
	}
	
	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle) {
		if (stack.hasTag()) {
			Crayfish.Variant variant = Crayfish.Variant.readVariant(stack.getTag());
			if (variant != null) {
				tooltipComponents.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
				return true;
			}
		}
		return false;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
		this.goalSelector.addGoal(0, new CrayfishPanicGoal());
		this.goalSelector.addGoal(0, new CrayfishMeleeAttackGoal());
		this.targetSelector.addGoal(0, new CrayfishAttackEntityGoal());
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FROM_BUCKET, false);
		this.entityData.define(VARIANT_ID, Crayfish.Variant.RED_SWAMP);
		this.entityData.define(ATTACK_TICK, 0);
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("FromBucket", this.fromBucket());
        this.getVariant().writeVariant(compound);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setFromBucket(compound.getBoolean("FromBucket"));
        Crayfish.Variant variant = Crayfish.Variant.readVariant(compound);
        if (variant != null) this.setVariant(variant);
	}

	public void tick() {
		super.tick();
		prevMeleeProgress = meleeProgress;
		if (this.entityData.get(ATTACK_TICK) > 0) {
			LivingEntity target = this.getTarget();
			if (this.entityData.get(ATTACK_TICK) == 1 && target != null && this.hasLineOfSight(target) && this.distanceTo(target) < 1.5F + this.getBbWidth() + target.getBbWidth()) {
				this.onAttackAnimationFinish(target);
			}
			this.entityData.set(ATTACK_TICK, this.entityData.get(ATTACK_TICK) - 1);
			if (meleeProgress < 1.0F) {
				meleeProgress = Math.min(meleeProgress + 0.2F, 1.0F);
			}
		} else {
			if (meleeProgress > 0F) {
				meleeProgress = Math.max(meleeProgress - 0.2F, 0.0F);
			}
		}
	}

	public boolean doHurtTarget(Entity entityIn) {
		this.entityData.set(ATTACK_TICK, 7);
		return true;
	}


	public float getMeleeProgress(float partialTick) {
		return prevMeleeProgress + (meleeProgress - prevMeleeProgress) * partialTick;
	}

	public boolean onAttackAnimationFinish(Entity target) {
		return target.hurt(this.damageSources().mobAttack(this), (float) ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.CRAYFISH_BUCKET);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.FOLLOW_RANGE, 2);
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
		Crayfish.Variant variant = Crayfish.Variant.readVariant(pTag);
		if (variant != null) this.setVariant(variant);
		else this.setVariant(Crayfish.Variant.byId(this.level().getRandom().nextInt(Crayfish.Variant.VALUES.length)));
	}

	public Crayfish.Variant getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Crayfish.Variant variant) {
		this.entityData.set(VARIANT_ID, variant);
	}

	public static enum Variant {
		RED_SWAMP("red_swamp", Ichthyology.id("textures/entity/crayfish/red_swamp.png")),
		NOBLE("noble", Ichthyology.id("textures/entity/crayfish/noble.png")),
		CAVE("cave", Ichthyology.id("textures/entity/crayfish/cave.png"));

		public static final Crayfish.Variant[] VALUES = values();
		private static final IntFunction<Crayfish.Variant> BY_ID = ByIdMap.continuous(Crayfish.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

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
			return "entity." + Ichthyology.ID + ".crayfish.variant." + this.getName();
		}

		public void writeVariant(CompoundTag tag) {
			tag.putString(TAG_VARIANT, this.getName());
		}

		public static Crayfish.Variant byId(int i) {
			return BY_ID.apply(i);
		}

		@Nullable
		public static Crayfish.Variant byData(String name) {
			for (Crayfish.Variant variant : VALUES) {
				if (variant.getName().equals(name)) {
					return variant;
				}
			}
			return null;
		}

		@Nullable
		public static Crayfish.Variant readVariant(CompoundTag tag) {
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

	class CrayfishMeleeAttackGoal extends MeleeAttackGoal {
		public CrayfishMeleeAttackGoal() {
			super(Crayfish.this, 1.25D, true);
		}

		public boolean canUse() {
			return super.canUse() && Crayfish.this.getHealth() >= (Crayfish.this.getMaxHealth() / 2);
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && Crayfish.this.getHealth() >= (Crayfish.this.getMaxHealth() / 2);
		}

		protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
			double d0 = this.getAttackReachSqr(pEnemy);
			if (pDistToEnemySqr <= d0 && this.isTimeToAttack()) {
				this.resetAttackCooldown();
				this.mob.doHurtTarget(pEnemy);
			} else if (pDistToEnemySqr <= d0 * 2.0D) {
				if (this.isTimeToAttack()) {
					this.resetAttackCooldown();
				}
			} else {
				this.resetAttackCooldown();
			}

		}

		public void stop() {
			super.stop();
		}

		protected double getAttackReachSqr(LivingEntity pAttackTarget) {
			return (double)(4.0F + pAttackTarget.getBbWidth());
		}
	}

	protected PathNavigation createNavigation(Level pLevel) {
	      return new AmphibiousPathNavigation(this, pLevel);
	}

	class CrayfishPanicGoal extends PanicGoal {
		public CrayfishPanicGoal() {
			super(Crayfish.this, 2.0D);
		}

		protected boolean shouldPanic() {
			return this.mob.getLastHurtByMob() != null && this.mob.getHealth() < (this.mob.getMaxHealth() / 2) || this.mob.isOnFire();
		}
	}

	class CrayfishAttackEntityGoal extends NearestAttackableTargetGoal<LivingEntity> {
		public CrayfishAttackEntityGoal() {
			super(Crayfish.this, LivingEntity.class, 20, true, true, (Predicate<LivingEntity>)null);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			if (Crayfish.this.getHealth() < (Crayfish.this.getMaxHealth() / 2)) {
				return false;
			} else return super.canUse();
		}

		protected double getFollowDistance() {
			return super.getFollowDistance() * 0.5D;
		}
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		if (reason == MobSpawnType.BUCKET) return spawnData;
		else {
			if (reason == MobSpawnType.SPAWN_EGG) setVariant(Crayfish.Variant.byId(level.getRandom().nextInt(Crayfish.Variant.VALUES.length))); 
			else {
				if (this.blockPosition().getY() <= level.getSeaLevel() - 33) this.setVariant(Variant.CAVE);
				else {
					if (level.getBiome(this.blockPosition()).is(Biomes.SWAMP)) this.setVariant(Variant.RED_SWAMP);
					if (level.getBiome(this.blockPosition()).is(Biomes.RIVER)) this.setVariant(Variant.NOBLE);
				}
			}
			return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean checkCrayfishSpawnRules(EntityType<? extends LivingEntity> p_217018_, ServerLevelAccessor p_217019_, MobSpawnType p_217020_, BlockPos p_217021_, RandomSource p_217022_) {
		int i = p_217019_.getSeaLevel();
		int j = i - 13;
		return p_217021_.getY() <= p_217019_.getSeaLevel() - 33 && p_217019_.getRawBrightness(p_217021_, 0) == 0 && p_217019_.getBlockState(p_217021_).is(Blocks.WATER) || p_217021_.getY() >= j && p_217021_.getY() <= i && (p_217019_.getBiome(p_217021_).is(Biomes.RIVER) || p_217019_.getBiome(p_217021_).is(Biomes.SWAMP)) && p_217019_.getBlockState(p_217021_.above()).is(Blocks.WATER) && p_217019_.getBlockState(p_217021_.below()).isSolid();
	}
	
	static class MoveHelperController extends MoveControl {
		private final Crayfish fish;

		MoveHelperController(Crayfish fish) {
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
	
	public boolean removeWhenFarAway(double p_27492_) {
		return !this.fromBucket() && !this.hasCustomName();
	}

}
