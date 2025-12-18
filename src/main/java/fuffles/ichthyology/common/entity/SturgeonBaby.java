package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.init.ModEntityTypes;
import fuffles.ichthyology.init.ModItems;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class SturgeonBaby extends WaterAnimal implements Bucketable {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(ModItems.GROUND_FISH, ModItems.CRAYFISH);
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SturgeonBaby.class, EntityDataSerializers.BOOLEAN);
	public static int ticksToBeAdult = Math.abs(-48000);
	private int age;

	public SturgeonBaby(EntityType<? extends SturgeonBaby> entityType, Level level) {
		super(entityType, level);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
	}

	private boolean isFood(ItemStack pStack) {
		return FOOD_ITEMS.test(pStack);
	}

	protected PathNavigation createNavigation(Level p_28362_) {
		return new WaterBoundPathNavigation(this, p_28362_);
	}

	public void travel(Vec3 p_28383_) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(this.getSpeed(), p_28383_);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(p_28383_);
		}
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FROM_BUCKET, true);
	}

	public boolean fromBucket() {
		return true;
	}

	public void setFromBucket(boolean pFromBucket) {
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
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
			this.setOnGround(false);
			this.hasImpulse = true;
			this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
		}
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.COD_FLOP;
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

	@SuppressWarnings("deprecation")
	private void ageUp() {
		Level level = this.level();
		if (level instanceof ServerLevel serverLevel) {
			Sturgeon sturgeon = ModEntityTypes.STURGEON.create(this.level());
			if (sturgeon != null) {
				sturgeon.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
				sturgeon.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(sturgeon.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);
				sturgeon.setNoAi(this.isNoAi());
				if (this.hasCustomName()) {
					sturgeon.setCustomName(this.getCustomName());
					sturgeon.setCustomNameVisible(this.isCustomNameVisible());
				}
				sturgeon.setPersistenceRequired();
				serverLevel.addFreshEntityWithPassengers(sturgeon);
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

}
