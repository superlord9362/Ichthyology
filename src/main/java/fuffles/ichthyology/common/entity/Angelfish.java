package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.datafixers.DataFixUtils;

import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Angelfish extends AbstractIchthyologySchoolingFish {
	private static final EntityDataAccessor<Boolean> SCARED = SynchedEntityData.defineId(Angelfish.class, EntityDataSerializers.BOOLEAN);

	public Angelfish(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(5, new AngelfishFollowFlockLeaderGoal(this));
		this.goalSelector.addGoal(0, new AngelfishHideInVegetationGoal((double)1.2F, 12, 1));
	}
	
	public void aiStep() {
		super.aiStep();
		for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2, 2, 2))) {
			if ((entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight()) > (this.getBbWidth() * this.getBbWidth() * this.getBbHeight())) this.setScared(true);
			else this.setScared(false);
		}
		if (this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2, 2, 2)) == null) this.setScared(false);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SCARED, false);
	}

	public boolean isScared() {
		return this.entityData.get(SCARED);
	}

	public void setScared(boolean isScared) {
		this.entityData.set(SCARED, isScared);
	}

	class AngelfishFollowFlockLeaderGoal extends Goal {
		@SuppressWarnings("unused")
		private static final int INTERVAL_TICKS = 200;
		private final Angelfish mob;
		private int timeToRecalcPath;
		private int nextStartTick;

		public AngelfishFollowFlockLeaderGoal(Angelfish p_25249_) {
			this.mob = p_25249_;
			this.nextStartTick = this.nextStartTick(p_25249_);
		}

		protected int nextStartTick(Angelfish p_25252_) {
			return reducedTickDelay(50 % 20);
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
				Predicate<Angelfish> predicate = (p_25258_) -> {
					return p_25258_.canBeFollowed() || !p_25258_.isFollower();
				};
				List<? extends Angelfish> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
				Angelfish abstractschoolingfish = DataFixUtils.orElse(list.stream().filter(Angelfish::canBeFollowed).findAny(), this.mob);
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
		return new ItemStack(ModItems.ANGELFISH_BUCKET);
	}
	
	public class AngelfishHideInVegetationGoal extends MoveToBlockGoal {
		@SuppressWarnings("unused")
		private static final int WAIT_TICKS = 40;
		protected int ticksWaited;

		public AngelfishHideInVegetationGoal(double p_28675_, int p_28676_, int p_28677_) {
			super(Angelfish.this, p_28675_, p_28676_, p_28677_);
		}

		public double acceptedDistance() {
			return 0.5D;
		}

		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		protected boolean isValidTarget(LevelReader p_28680_, BlockPos p_28681_) {
			BlockState blockstate = p_28680_.getBlockState(p_28681_);
			return (blockstate.is(Blocks.SEAGRASS) || blockstate.is(Blocks.TALL_SEAGRASS)  || blockstate.is(Blocks.KELP) || blockstate.is(Blocks.KELP_PLANT));
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
			return super.canUse() && Angelfish.this.isScared();
		}

		public void start() {
			this.ticksWaited = 0;
			super.start();
		}
	}

}
