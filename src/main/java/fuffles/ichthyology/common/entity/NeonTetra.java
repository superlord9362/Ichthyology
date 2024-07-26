package fuffles.ichthyology.common.entity;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;
import com.mojang.datafixers.DataFixUtils;

import fuffles.ichthyology.init.IItems;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NeonTetra extends AbstractIchthyologySchoolingFish {

	public NeonTetra(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}
	
	@Override
	public int getMaxSchoolSize() {
		return 12;
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(5, new NeonTetraFollowFlockLeaderGoal(this));
		this.goalSelector.addGoal(5, new BoidGoal(this, 0.5f, 0.2f, 8 / 20f, 1 / 20f));
		this.goalSelector.addGoal(3, new StayInWaterGoal(this));
		this.goalSelector.addGoal(2, new LimitSpeedAndLookInVelocityDirectionGoal(this, 0.2f, 0.4f));
	}

	class NeonTetraFollowFlockLeaderGoal extends Goal {
		@SuppressWarnings("unused")
		private static final int INTERVAL_TICKS = 200;
		private final NeonTetra mob;
		private int timeToRecalcPath;
		private int nextStartTick;

		public NeonTetraFollowFlockLeaderGoal(NeonTetra p_25249_) {
			this.mob = p_25249_;
			this.nextStartTick = this.nextStartTick(p_25249_);
		}

		protected int nextStartTick(NeonTetra p_25252_) {
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
				Predicate<NeonTetra> predicate = (p_25258_) -> {
					return p_25258_.canBeFollowed() || !p_25258_.isFollower();
				};
				List<? extends NeonTetra> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
				NeonTetra abstractschoolingfish = DataFixUtils.orElse(list.stream().filter(NeonTetra::canBeFollowed).findAny(), this.mob);
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
		return new ItemStack(IItems.NEON_TETRA_BUCKET.get());
	}
	
	class BoidGoal extends Goal {
		public static final Logger LOGGER = LogManager.getLogger();

		public final float separationInfluence;
		public final float separationRange;
		public final float alignmentInfluence;
		public final float cohesionInfluence;
		private final Mob mob;
		private int timeToFindNearbyEntities;
		List<? extends Mob> nearbyMobs;
		private boolean enabled = true;

		public BoidGoal(Mob mob, float separationInfluence, float separationRange, float alignmentInfluence, float cohesionInfluence) {
			timeToFindNearbyEntities = 0;

			this.mob = mob;
			this.separationInfluence = separationInfluence;
			this.separationRange = separationRange;
			this.alignmentInfluence = alignmentInfluence;
			this.cohesionInfluence = cohesionInfluence;
		}

		@Override
		public boolean canUse() {
			return true;
		}

		public void tick() {
			if (!enabled) {
				return;
			}

			if (--this.timeToFindNearbyEntities <= 0) {
				this.timeToFindNearbyEntities = this.adjustedTickDelay(40);
				nearbyMobs = getNearbyEntitiesOfSameClass(mob);
			} else {
				nearbyMobs.removeIf(LivingEntity::isDeadOrDying);
			}

			if (nearbyMobs.isEmpty()) {
				LOGGER.warn("No nearby entities found. There should always be at least the entity itself. Will disable behavior for this entity instead of crash for compatibility reasons");
				enabled = false;
			}

			mob.addDeltaMovement(random());
			mob.addDeltaMovement(cohesion());
			mob.addDeltaMovement(alignment());
			mob.addDeltaMovement(separation());
		}

		public static List<? extends Mob> getNearbyEntitiesOfSameClass(Mob mob) {
			Predicate<Mob> predicate = (_mob) -> true;

			return mob.level().getEntitiesOfClass(mob.getClass(), mob.getBoundingBox().inflate(4.0, 4.0, 4.0), predicate);
		}

		public Vec3 random() {
			var velocity = mob.getDeltaMovement();

			if (Mth.abs((float) velocity.x) < 0.1 && Mth.abs((float) velocity.z) < 0.1)
				return new Vec3(randomSign() * 0.2, 0, randomSign() * 0.2);

			return Vec3.ZERO;
		}

		public int randomSign() {
			var isNegative = mob.getRandom().nextBoolean();

			if (isNegative) {
				return -1;
			}

			return 1;
		}

		public Vec3 separation() {
			var c = Vec3.ZERO;

			for (Mob nearbyMob : nearbyMobs) {
				if ((nearbyMob.position().subtract(mob.position()).length()) < separationRange) {
					c = c.subtract(nearbyMob.position().subtract(mob.position()));
				}
			}

			return c.scale(separationInfluence);
		}

		public Vec3 alignment() {
			var c = Vec3.ZERO;

			for (Mob nearbyMob : nearbyMobs) {
				c = c.add(nearbyMob.getDeltaMovement());
			}

			c = c.scale(1f / nearbyMobs.size());
			c = c.subtract(mob.getDeltaMovement());
			return c.scale(alignmentInfluence);
		}

		public Vec3 cohesion() {
			var c = Vec3.ZERO;

			for (Mob nearbyMob : nearbyMobs) {
				c = c.add(nearbyMob.position());
			}

			c = c.scale(1f / nearbyMobs.size());
			c = c.subtract(mob.position());
			return c.scale(cohesionInfluence);
		}
	}

	class StayInWaterGoal extends Goal {
		private final Mob mob;

		public StayInWaterGoal(Mob mob) {
			this.mob = mob;
		}

		@Override
		public boolean canUse() {
			return true;
		}

		@Override
		public void tick() {
			var blockPos = mob.blockPosition();
			var blockAbove = mob.level().getBlockState(blockPos.above(2));
			var blockBelow = mob.level().getBlockState(blockPos.below(1));
			var amount = amount();

			if(blockAbove.getFluidState().isEmpty()) {
				mob.addDeltaMovement(new Vec3(0, -amount, 0));
			}

			if(blockBelow.getFluidState().isEmpty()) {
				mob.addDeltaMovement(new Vec3(0, amount, 0));
			}
		}

		float amount() {
			var amount = 0.1f;
			var dY = Mth.abs((float) mob.getDeltaMovement().y);

			if (dY > amount) {
				amount = dY;
			}

			return amount;
		}
	}

	class LimitSpeedAndLookInVelocityDirectionGoal extends Goal {
		private final Mob mob;
		private final float minSpeed;
		private final float maxSpeed;

		public LimitSpeedAndLookInVelocityDirectionGoal(Mob mob, float minSpeed, float maxSpeed) {
			this.mob = mob;
			this.minSpeed = minSpeed;
			this.maxSpeed = maxSpeed;
		}

		@Override
		public boolean canUse() {
			return true;
		}

		@Override
		public void tick() {
			var velocity = mob.getDeltaMovement();
			var speed = velocity.length();

			if (speed < minSpeed)
				velocity = velocity.normalize().scale(minSpeed);
			if (speed > maxSpeed)
				velocity = velocity.normalize().scale(maxSpeed);

			mob.setDeltaMovement(velocity);
			mob.lookAt(EntityAnchorArgument.Anchor.EYES, mob.position().add(velocity.scale(3))); // Scale by 3 just to be sure it is roughly the right direction
		}
	}

}
