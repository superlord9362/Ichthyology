package fuffles.ichthyology.common.entity;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.init.ModItems;
import fuffles.ichthyology.init.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class Mudskipper extends Animal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Mudskipper.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> ROLLING = SynchedEntityData.defineId(Mudskipper.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FIGHTING = SynchedEntityData.defineId(Mudskipper.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> BLINKING = SynchedEntityData.defineId(Mudskipper.class, EntityDataSerializers.BOOLEAN);

	public int fightCooldownTicks;

	@SuppressWarnings("deprecation")
	public Mudskipper(EntityType<? extends Mudskipper> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.3F, false);
		this.lookControl = new SmoothSwimmingLookControl(this, 20);
		this.setMaxUpStep(1.0F);
		this.fightCooldownTicks = 4800;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(0, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2F));
		//		this.goalSelector.addGoal(2, new MudskipperFindWaterGoal(this));
		this.goalSelector.addGoal(1, new MudskipperRollGoal(this));
		this.goalSelector.addGoal(1, new MudskipperFightGoal(this));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FROM_BUCKET, false);
		this.entityData.define(ROLLING, false);
		this.entityData.define(FIGHTING, false);
		this.entityData.define(BLINKING, false);
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

	public void rehydrate() {
		int i = this.getAirSupply() + 1800;
		this.setAirSupply(Math.min(i, this.getMaxAirSupply()));
	}

	public int getMaxAirSupply() {
		return 6000;
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public boolean isPushedByFluid() {
		return false;
	}

	public MobType getMobType() {
		return MobType.WATER;
	}

	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	public void setFromBucket(boolean pFromBucket) {
		this.entityData.set(FROM_BUCKET, pFromBucket);
	}

	public boolean isRolling() {
		return this.entityData.get(ROLLING);
	}

	public void setRolling(boolean rolling) {
		this.entityData.set(ROLLING, rolling);
	}

	public boolean isFighting() {
		return this.entityData.get(FIGHTING);
	}

	public void setFighting(boolean fighting) {
		this.entityData.set(FIGHTING, fighting);
	}

	public boolean isBlinking() {
		return this.entityData.get(BLINKING);
	}

	public void setBlinking(boolean blinking) {
		this.entityData.set(BLINKING, blinking);
	}

	public int getFightCooldownTicks() {
		return this.fightCooldownTicks;
	}

	public void setFightCooldownTicks(int cooldownTicks) {
		this.fightCooldownTicks = cooldownTicks;
	}

	protected PathNavigation createNavigation(Level pLevel) {
		return new AmphibiousPathNavigation(this, pLevel);
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
	}

	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.fromBucket();
	}

	public static boolean checkMudskipperSpawnRules(EntityType<? extends LivingEntity> p_217018_, ServerLevelAccessor p_217019_, MobSpawnType p_217020_, BlockPos p_217021_, RandomSource p_217022_) {
		return p_217019_.getRawBrightness(p_217021_, 0) > 8 && p_217019_.getBlockState(p_217021_.below()).is(Blocks.MUD);
	}

	public void travel(Vec3 pTravelVector) {
		if (this.isControlledByLocalInstance() && this.isInWater()) {
			this.moveRelative(this.getSpeed(), pTravelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
		} else {
			super.travel(pTravelVector);
		}

	}

	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("FromBucket", this.fromBucket());
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setFromBucket(pCompound.getBoolean("FromBucket"));
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.MUDSKIPPER_BUCKET);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D);
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.MUDSKIPPER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource)
	{
		return ModSoundEvents.MUDSKIPPER_HURT;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveToBucketTag(ItemStack pStack) {
		Bucketable.saveDefaultDataToBucketTag(this, pStack);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadFromBucketTag(CompoundTag pTag) {
		Bucketable.loadDefaultDataFromBucketTag(this, pTag);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		return null;
	}

	public void aiStep() {
		super.aiStep();
		for (Mudskipper mudskipper : this.level().getEntitiesOfClass(Mudskipper.class, this.getBoundingBox().inflate(4, 4, 4))) {
			if (mudskipper != this) {
				if (!this.isRolling() && !mudskipper.isRolling()) {
					if (mudskipper.getFightCooldownTicks() == 0 && this.getFightCooldownTicks() == 0) {
						if (!this.isInWater() && !mudskipper.isInWater()) {
							mudskipper.getLookControl().setLookAt(this);
							this.getLookControl().setLookAt(mudskipper);
							mudskipper.setFighting(true);
							this.setFighting(true);
						}
					}
				}
			}
		}
		if (this.tickCount % 4800 == 0 && !this.isInWater()) {
			if (level().getBlockState(this.blockPosition().below()).getBlock() == Blocks.CLAY || level().getBlockState(this.blockPosition()).getBlock() == Blocks.MUD) {
				this.setRolling(true);
			}
		}
		if (this.getRandom().nextInt(100) == 36) {
			if (this.isBlinking()) this.setBlinking(false);
			else this.setBlinking(true);
		}
		if (this.getFightCooldownTicks() != 0) this.setFightCooldownTicks(this.getFightCooldownTicks() - 1);
	}

	public class MudskipperRollGoal extends Goal {

		Mudskipper mudskipper;

		public MudskipperRollGoal(Mudskipper mudskipper) {
			this.mudskipper = mudskipper;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return mudskipper.isRolling();
		}

		public void tick() {
			super.tick();
			mudskipper.moveControl.setWantedPosition(mudskipper.getX(), mudskipper.getY(), mudskipper.getZ(), 0);
			if (mudskipper.getRandom().nextInt(100) == 0) {
				ItemEntity item = new ItemEntity(mudskipper.level(), mudskipper.getX(), mudskipper.getY(), mudskipper.getZ(), new ItemStack(Items.CLAY_BALL));
				item.setPos(mudskipper.getEyePosition());
				mudskipper.level().addFreshEntity(item);
				mudskipper.setRolling(false);
			}
		}

		public boolean canContinueToUse() {
			return mudskipper.isRolling() && !mudskipper.isInWater();
		}

		public void stop() {
			mudskipper.setRolling(false);
		}

	}

	public class MudskipperFightGoal extends Goal {

		Mudskipper mudskipper;

		public MudskipperFightGoal(Mudskipper mudskipper) {
			this.mudskipper = mudskipper;
			this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return mudskipper.isFighting();
		}

		public void tick() {
			super.tick();
			mudskipper.moveControl.setWantedPosition(mudskipper.getX(), mudskipper.getY(), mudskipper.getZ(), 0);
			if (mudskipper.tickCount % 400 == 0) {
				mudskipper.setFightCooldownTicks(3600);
				mudskipper.setFighting(false);
				stop();
			}
		}

		public boolean canContinueToUse() {
			return mudskipper.isFighting();
		}
		
		public void stop() {
			super.stop();
			mudskipper.setFightCooldownTicks(3600);
			mudskipper.setFighting(false);
		}

	}

	public class MudskipperFindWaterGoal extends TryFindWaterGoal {

		public MudskipperFindWaterGoal(PathfinderMob pMob) {
			super(pMob);
		}

		public boolean canUse()  {
			return super.canUse() && Mudskipper.this.getAirSupply() < 1000;
		}

	}
}
