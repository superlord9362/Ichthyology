package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.init.ModEntityTypes;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class CatfishBaby extends WaterAnimal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CatfishBaby.class, EntityDataSerializers.BOOLEAN);
	public static int ticksToBeAdult = Math.abs(-24000);
	private int age;

	public CatfishBaby(EntityType<? extends CatfishBaby> entityType, Level level) {
		super(entityType, level);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
	}

	private boolean isFood(ItemStack pStack) {
		return pStack.getItem() == ModItems.CRAYFISH;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new CatfishRestInVegetationGoal(1, 10, 5));
	}
	
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FROM_BUCKET, false);
	}

	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	public void setFromBucket(boolean pFromBucket) {
		this.entityData.set(FROM_BUCKET, pFromBucket);
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (this.isFood(itemstack)) {
			this.feed(pPlayer, itemstack);
			return InteractionResult.sidedSuccess(this.level().isClientSide());
		} else {
			return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
		}
	}

	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.fromBucket();
	}

	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("FromBucket", this.fromBucket());
		pCompound.putInt("Age", this.age);
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setFromBucket(pCompound.getBoolean("FromBucket"));
		this.setAge(pCompound.getInt("Age"));
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.MUDSKIPPER_BUCKET);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
	}

	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide()) {
			this.setAge(this.age + 1);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveToBucketTag(ItemStack pStack) {
		Bucketable.saveDefaultDataToBucketTag(this, pStack);
		CompoundTag compoundtag = pStack.getOrCreateTag();
		compoundtag.putInt("Age", this.getAge());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadFromBucketTag(CompoundTag pTag) {
		Bucketable.loadDefaultDataFromBucketTag(this, pTag);
		if (pTag.contains("Age")) {
			this.setAge(pTag.getInt("Age"));
		}
	}

	private void feed(Player pPlayer, ItemStack pStack) {
		this.usePlayerItem(pPlayer, pStack);
		this.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(this.getTicksLeftUntilAdult()));
		this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
	}

	private void usePlayerItem(Player pPlayer, ItemStack pStack) {
		if (!pPlayer.getAbilities().instabuild) {
			pStack.shrink(1);
		}

	}

	private int getAge() {
		return this.age;
	}

	private void ageUp(int pOffset) {
		this.setAge(this.age + pOffset * 20);
	}

	private void setAge(int pAge) {
		this.age = pAge;
		if (this.age >= ticksToBeAdult) {
			this.ageUp();
		}
	}

	private void ageUp() {
		Level level = this.level();
		if (level instanceof ServerLevel serverLevel) {
			Catfish catfish = ModEntityTypes.CATFISH.create(this.level());
			if (catfish != null) {
				catfish.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
				catfish.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(catfish.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);
				catfish.setNoAi(this.isNoAi());
				if (this.hasCustomName()) {
					catfish.setCustomName(this.getCustomName());
					catfish.setCustomNameVisible(this.isCustomNameVisible());
				}
				catfish.setPersistenceRequired();
				serverLevel.addFreshEntityWithPassengers(catfish);
				this.discard();
			}
		}
	}

	private int getTicksLeftUntilAdult() {
		return Math.max(0, ticksToBeAdult - this.age);
	}

	public boolean shouldDropExperience() {
		return false;
	}

	public class CatfishRestInVegetationGoal extends MoveToBlockGoal {
		protected int ticksWaited;

		public CatfishRestInVegetationGoal(double p_28675_, int p_28676_, int p_28677_) {
			super(CatfishBaby.this, p_28675_, p_28676_, p_28677_);
		}

		public double acceptedDistance() {
			return 0.5D;
		}

		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		protected boolean isValidTarget(LevelReader p_28680_, BlockPos p_28681_) {
			BlockState blockstate = p_28680_.getBlockState(p_28681_);
			return (blockstate.is(Blocks.SEAGRASS) || blockstate.is(Blocks.TALL_SEAGRASS) || blockstate.is(Blocks.KELP) || blockstate.is(Blocks.KELP_PLANT));
		}

		public void tick() {
			if (this.isReachedTarget()) {
				if (this.ticksWaited >= 40) {
				} else {
					++this.ticksWaited;
				}
			}

			super.tick();
		}

		public boolean canUse() {
			return super.canUse() && CatfishBaby.this.level().isDay();
		}

		public boolean canContinueToUse() {
			return CatfishBaby.this.level().isDay();
		}

		public void start() {
			this.ticksWaited = 0;
			super.start();
		}
	}

}
