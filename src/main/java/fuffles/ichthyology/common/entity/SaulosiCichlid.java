package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.datafixers.DataFixUtils;

import fuffles.ichthyology.init.IItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class SaulosiCichlid extends AbstractIchthyologySchoolingFish {
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SaulosiCichlid.class, EntityDataSerializers.INT);

	public SaulosiCichlid(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(5, new SaulosiCichlidFollowFlockLeaderGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(COLOR, 0);
	}

	public void addAdditionalSaveData(CompoundTag p_27485_) {
		super.addAdditionalSaveData(p_27485_);
		p_27485_.putInt("Color", this.getColor());
	}

	public void readAdditionalSaveData(CompoundTag p_27465_) {
		super.readAdditionalSaveData(p_27465_);
		this.setColor(p_27465_.getInt("Color"));
	}

	public int getColor() {
		return this.entityData.get(COLOR);
	}

	public void setColor(int color) {
		this.entityData.set(COLOR, color);
	}

	@Override
	public void saveToBucketTag(ItemStack bucket) {
		CompoundTag nbt = bucket.getOrCreateTag();
		nbt.putInt("Color", this.getColor());
		if (this.hasCustomName()) {
			bucket.setHoverName(this.getCustomName());
		}
	}

	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_27528_, DifficultyInstance p_27529_, MobSpawnType p_27530_, @Nullable SpawnGroupData p_27531_, @Nullable CompoundTag p_27532_) {
		super.finalizeSpawn(p_27528_, p_27529_, p_27530_, p_27531_, p_27532_);
		if (p_27530_ == MobSpawnType.NATURAL) {
			if (this.random.nextInt(7) <= 1) this.setColor(0);
			else this.setColor(1);
		} else this.setColor(random.nextInt(2));
		return p_27531_;
	}

	class SaulosiCichlidFollowFlockLeaderGoal extends Goal {
		@SuppressWarnings("unused")
		private static final int INTERVAL_TICKS = 200;
		private final SaulosiCichlid mob;
		private int timeToRecalcPath;
		private int nextStartTick;

		public SaulosiCichlidFollowFlockLeaderGoal(SaulosiCichlid p_25249_) {
			this.mob = p_25249_;
			this.nextStartTick = this.nextStartTick(p_25249_);
		}

		protected int nextStartTick(SaulosiCichlid p_25252_) {
			return reducedTickDelay(200 + p_25252_.getRandom().nextInt(200) % 20);
		}

		public boolean canUse() {
			if (this.mob.hasFollowers()) {
				return false;
			} else if (this.mob.isFollower()) {
				return true;
			} else if (this.nextStartTick > 0) {
				--this.nextStartTick;
				return false;
			} else {
				this.nextStartTick = this.nextStartTick(this.mob);
				Predicate<SaulosiCichlid> predicate = (p_25258_) -> {
					return p_25258_.canBeFollowed() || !p_25258_.isFollower();
				};
				List<? extends SaulosiCichlid> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
				SaulosiCichlid abstractschoolingfish = DataFixUtils.orElse(list.stream().filter(SaulosiCichlid::canBeFollowed).findAny(), this.mob);
				abstractschoolingfish.addFollowers(list.stream().filter((p_25255_) -> {
					return !p_25255_.isFollower();
				}));
				return this.mob.isFollower();
			}
		}

		public boolean canContinueToUse() {
			return this.mob.isFollower() && this.mob.inRangeOfLeader();
		}

		public void start() {
			this.timeToRecalcPath = 0;
		}

		public void stop() {
			this.mob.stopFollowing();
		}

		public void tick() {
			if (--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = this.adjustedTickDelay(10);
				this.mob.pathToLeader();
			}
		}
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(IItems.SAULOSI_CICHLID_BUCKET.get());
	}

}
