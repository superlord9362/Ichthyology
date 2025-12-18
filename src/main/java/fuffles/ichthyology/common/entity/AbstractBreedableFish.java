package fuffles.ichthyology.common.entity;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractBreedableFish extends AbstractIchthyologyFish {
	private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AbstractBreedableFish.class, EntityDataSerializers.BOOLEAN);
	public static final int BABY_START_AGE = -24000;
	protected int age;
	protected int forcedAge;
	protected int forcedAgeTimer;
	private int inLove;
	@Nullable
	private UUID loveCause;

	public AbstractBreedableFish(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	protected void customServerAiStep() {
		if (this.getAge() != 0) {
			this.inLove = 0;
		}

		super.customServerAiStep();
	}

	protected void ageBoundaryReached() {
		if (!this.isBaby() && this.isPassenger()) {
			Entity entity = this.getVehicle();
			if (entity instanceof Boat) {
				Boat boat = (Boat)entity;
				if (!boat.hasEnoughSpaceFor(this)) {
					this.stopRiding();
				}
			}
		}

	}

	public boolean isBaby() {
		return this.getAge() < 0;
	}

	public void setBaby(boolean pBaby) {
		this.setAge(pBaby ? -24000 : 0);
	}

	public void aiStep() {
		super.aiStep();
		if (this.getAge() != 0) {
			this.inLove = 0;
		}
		if (this.level().isClientSide()) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
				}

				--this.forcedAgeTimer;
			}
		} else if (this.isAlive()) {
			int i = this.getAge();
			if (i < 0) {
				++i;
				this.setAge(i);
			} else if (i > 0) {
				--i;
				this.setAge(i);
			}
		}
		if (this.inLove > 0) {
			--this.inLove;
			if (this.inLove % 10 == 0) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
			}
		}
	}

	public abstract boolean isFood(ItemStack stack);

	protected void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack) {
		if (!pPlayer.getAbilities().instabuild) {
			pStack.shrink(1);
		}
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (this.isFood(itemstack)) {
			int i = this.getAge();
			if (!this.level().isClientSide() && i == 0 && this.canFallInLove()) {
				this.usePlayerItem(pPlayer, pHand, itemstack);
				this.setInLove(pPlayer);
				return InteractionResult.SUCCESS;
			}

			if (this.isBaby()) {
				this.usePlayerItem(pPlayer, pHand, itemstack);
				this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
				return InteractionResult.sidedSuccess(this.level().isClientSide());
			}

			if (this.level().isClientSide()) {
				return InteractionResult.CONSUME;
			}
		}
		return super.mobInteract(pPlayer, pHand);
	}

	public static int getSpeedUpSecondsWhenFeeding(int pTicksUntilAdult) {
		return (int)((float)(pTicksUntilAdult / 20) * 0.1F);
	}

	public int getAge() {
		if (this.level().isClientSide()) {
			return this.entityData.get(DATA_BABY_ID) ? -1 : 1;
		} else {
			return this.age;
		}
	}

	public void ageUp(int pAmount, boolean pForced) {
		int i = this.getAge();
		i += pAmount * 20;
		if (i > 0) {
			i = 0;
		}

		int j = i - i;
		this.setAge(i);
		if (pForced) {
			this.forcedAge += j;
			if (this.forcedAgeTimer == 0) {
				this.forcedAgeTimer = 40;
			}
		}

		if (this.getAge() == 0) {
			this.setAge(this.forcedAge);
		}

	}

	public void ageUp(int pAmount) {
		this.ageUp(pAmount, false);
	}

	public void setAge(int pAge) {
		int i = this.getAge();
		this.age = pAge;
		if (i < 0 && pAge >= 0 || i >= 0 && pAge < 0) {
			this.entityData.set(DATA_BABY_ID, pAge < 0);
			this.ageBoundaryReached();
		}

	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_BABY_ID, false);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.inLove = compound.getInt("InLove");
		this.setAge(compound.getInt("Age"));
		this.forcedAge = compound.getInt("ForcedAge");
		this.loveCause = compound.hasUUID("LoveCause") ? compound.getUUID("LoveCause") : null;
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("InLove", this.inLove);
		if (this.loveCause != null) {
			compound.putUUID("LoveCause", this.loveCause);
		}
		compound.putInt("Age", this.getAge());
		compound.putInt("ForcedAge", this.forcedAge);
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (DATA_BABY_ID.equals(pKey)) {
			this.refreshDimensions();
		}

		super.onSyncedDataUpdated(pKey);
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource)) {
			return false;
		} else {
			this.inLove = 0;
			return super.hurt(pSource, pAmount);
		}
	}

	public boolean canFallInLove() {
		return this.inLove <= 0;
	}

	public void setInLove(@Nullable Player pPlayer) {
		this.inLove = 600;
		if (pPlayer != null) {
			this.loveCause = pPlayer.getUUID();
		}

		this.level().broadcastEntityEvent(this, (byte)18);
	}

	public void setInLoveTime(int pInLove) {
		this.inLove = pInLove;
	}

	public int getInLoveTime() {
		return this.inLove;
	}

	@Nullable
	public ServerPlayer getLoveCause() {
		if (this.loveCause == null) {
			return null;
		} else {
			Player player = this.level().getPlayerByUUID(this.loveCause);
			return player instanceof ServerPlayer ? (ServerPlayer)player : null;
		}
	}

	public boolean isInLove() {
		return this.inLove > 0;
	}

	public void resetLove() {
		this.inLove = 0;
	}

	public boolean canMate(AbstractBreedableFish pOtherAnimal) {
		if (pOtherAnimal == this) {
			return false;
		} else if (pOtherAnimal.getClass() != this.getClass()) {
			return false;
		} else {
			return this.isInLove() && pOtherAnimal.isInLove();
		}
	}

	public void handleEntityEvent(byte pId) {
		if (pId == 18) {
			for(int i = 0; i < 7; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
			}
		} else {
			super.handleEntityEvent(pId);
		}

	}

}
