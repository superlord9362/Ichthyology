package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.datafixers.DataFixUtils;

import fuffles.ichthyology.init.IItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Discus extends AbstractIchthyologySchoolingFish {

	public Discus(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(5, new DiscusFollowFlockLeaderGoal(this));
	}

	class DiscusFollowFlockLeaderGoal extends Goal {
		@SuppressWarnings("unused")
		private static final int INTERVAL_TICKS = 200;
		private final Discus mob;
		private int timeToRecalcPath;
		private int nextStartTick;

		public DiscusFollowFlockLeaderGoal(Discus p_25249_) {
			this.mob = p_25249_;
			this.nextStartTick = this.nextStartTick(p_25249_);
		}

		protected int nextStartTick(Discus p_25252_) {
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
				Predicate<Discus> predicate = (p_25258_) -> {
					return p_25258_.canBeFollowed() || !p_25258_.isFollower();
				};
				List<? extends Discus> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
				Discus abstractschoolingfish = DataFixUtils.orElse(list.stream().filter(Discus::canBeFollowed).findAny(), this.mob);
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
		return new ItemStack(IItems.DISCUS_BUCKET.get());
	}

}
