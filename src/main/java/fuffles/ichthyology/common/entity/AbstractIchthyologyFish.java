package fuffles.ichthyology.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractIchthyologyFish extends WaterAnimal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractIchthyologyFish.class, EntityDataSerializers.BOOLEAN);

	public AbstractIchthyologyFish(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FROM_BUCKET, false);
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

	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
			this.setOnGround(false);
			this.hasImpulse = true;
			this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
		}

		super.aiStep();
	}
	
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();
		if (item == Items.WATER_BUCKET) {
			return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
		} else {
			return super.mobInteract(player, hand);
		}
	}

	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.COD_FLOP;
	}

	protected SoundEvent getSwimSound() {
		return SoundEvents.FISH_SWIM;
	}

	protected void playStepSound(BlockPos p_27482_, BlockState p_27483_) {
	}

	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.fromBucket();
	}

	public boolean removeWhenFarAway(double p_27492_) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	public void addAdditionalSaveData(CompoundTag p_27485_) {
		super.addAdditionalSaveData(p_27485_);
		p_27485_.putBoolean("FromBucket", this.fromBucket());
	}

	public void readAdditionalSaveData(CompoundTag p_27465_) {
		super.readAdditionalSaveData(p_27465_);
		this.setFromBucket(p_27465_.getBoolean("FromBucket"));
	}
	
	@Override
	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	@Override
	public void setFromBucket(boolean p_148834_) {
		this.entityData.set(FROM_BUCKET, p_148834_);
	}

	@Override
	public void saveToBucketTag(ItemStack bucket) {
		if (this.hasCustomName()) {
			bucket.setHoverName(this.getCustomName());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadFromBucketTag(CompoundTag p_148832_) {
		Bucketable.loadDefaultDataFromBucketTag(this, p_148832_);
	}

}
