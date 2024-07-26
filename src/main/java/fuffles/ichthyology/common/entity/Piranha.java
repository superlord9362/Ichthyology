package fuffles.ichthyology.common.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import com.mojang.datafixers.DataFixUtils;

import fuffles.ichthyology.init.IItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Piranha extends AbstractIchthyologySchoolingFish {
	private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(Piranha.class, EntityDataSerializers.BOOLEAN);
	static final Predicate<ItemEntity> ALLOWED_ITEMS = (p_289438_) -> {
		return !p_289438_.hasPickUpDelay() && p_289438_.isAlive();
	};
	private int ticksSinceEaten;

	public Piranha(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(5, new PiranhaFollowFlockLeaderGoal(this));
		this.goalSelector.addGoal(1, new PiranhaMeleeAttackGoal(this, (double)1.2F, true));
		this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)).setAlertOthers());
		this.targetSelector.addGoal(0, new PiranhaAttackGoal(this, LivingEntity.class, true));
		this.goalSelector.addGoal(0, new PiranhaSearchForItemsGoal());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ANGRY, false);
	}

	public boolean isAngry() {
		return this.entityData.get(ANGRY);
	}

	public void setAngry(boolean isAngry) {
		this.entityData.set(ANGRY, isAngry);
	}



	public boolean doHurtTarget(Entity p_28319_) {
		boolean flag = p_28319_.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		if (flag) {
			this.doEnchantDamageEffects(this, p_28319_);
		}
		if (p_28319_ instanceof LivingEntity entity) {
			//entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100));
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
		}
		return flag;
	}

	class PiranhaFollowFlockLeaderGoal extends Goal {
		@SuppressWarnings("unused")
		private static final int INTERVAL_TICKS = 200;
		private final Piranha mob;
		private int timeToRecalcPath;
		private int nextStartTick;

		public PiranhaFollowFlockLeaderGoal(Piranha p_25249_) {
			this.mob = p_25249_;
			this.nextStartTick = this.nextStartTick(p_25249_);
		}

		protected int nextStartTick(Piranha p_25252_) {
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
				Predicate<Piranha> predicate = (p_25258_) -> {
					return p_25258_.canBeFollowed() || !p_25258_.isFollower();
				};
				List<? extends Piranha> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
				Piranha abstractschoolingfish = DataFixUtils.orElse(list.stream().filter(Piranha::canBeFollowed).findAny(), this.mob);
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
		return new ItemStack(IItems.PRINCESS_CICHLID_BUCKET.get());
	}

	public void aiStep() {
		super.aiStep();
		for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4, 4, 4))) {
			if ((entity.getHealth() < (entity.getMaxHealth() / 2) || entity.hasEffect(MobEffects.WEAKNESS) || entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) || entity instanceof Zombie) && entity != this) this.setTarget(entity);
			else if (this.getLastHurtByMob() != null) {
				if (!this.getLastHurtByMob().isAlive()) this.setTarget(null);
			}
		}
		if (this.getTarget() != null) {
			if (this.getTarget().isAlive()) {
				this.setAngry(true);	
			} else this.setTarget(null);
		} else this.setAngry(false);
		++this.ticksSinceEaten;
		ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
		if (this.ticksSinceEaten > 600) {
			ItemStack itemstack1 = itemstack.finishUsingItem(this.level(), this);
			if (!itemstack1.isEmpty()) {
				this.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
			}

			this.ticksSinceEaten = 0;
		} else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1F) {
			this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
			this.level().broadcastEntityEvent(this, (byte)45);
		}
	}

	@SuppressWarnings("rawtypes")
	public class PiranhaAttackGoal extends NearestAttackableTargetGoal {

		@SuppressWarnings("unchecked")
		public PiranhaAttackGoal(Mob p_26060_, Class p_26061_, boolean p_26062_) {
			super(p_26060_, p_26061_, p_26062_);
		}

		public boolean canUse() {
			return Piranha.this.isAngry();
		}

		public void tick() {
			super.tick();
			if (Piranha.this.getTarget() != null) {
				if (Piranha.this.getTarget() instanceof Zombie && Piranha.this.getTarget().getHealth() == 0 && Piranha.this.random.nextInt(20) == 0) {
					Skeleton skeleton = new Skeleton(EntityType.SKELETON, Piranha.this.level());
					skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
					skeleton.setPos(Piranha.this.getTarget().blockPosition().getX(), Piranha.this.getTarget().blockPosition().getY(), Piranha.this.getTarget().blockPosition().getZ());
					Piranha.this.level().addFreshEntity(skeleton);
				}
				if (Piranha.this.getTarget() instanceof ZombieHorse && Piranha.this.getTarget().getHealth() == 0 && Piranha.this.random.nextInt(20) == 0) {
					SkeletonHorse skeleton = new SkeletonHorse(EntityType.SKELETON_HORSE, Piranha.this.level());
					skeleton.setPos(Piranha.this.getTarget().blockPosition().getX(), Piranha.this.getTarget().blockPosition().getY(), Piranha.this.getTarget().blockPosition().getZ());
					Piranha.this.level().addFreshEntity(skeleton);
				}
				if (Piranha.this.getTarget().getHealth() == 0) {
					Piranha.this.setAngry(false);
				}
			}
		}

	}

	public class PiranhaMeleeAttackGoal extends MeleeAttackGoal {

		public PiranhaMeleeAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_) {
			super(p_25552_, p_25553_, p_25554_);
		}

		@Override
		protected double getAttackReachSqr(LivingEntity p_25556_) {
			return (double)(2.5);
		}

	}

	public boolean canTakeItem(ItemStack p_28552_) {
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(p_28552_);
		if (!this.getItemBySlot(equipmentslot).isEmpty()) {
			return false;
		} else {
			return equipmentslot == EquipmentSlot.MAINHAND && super.canTakeItem(p_28552_);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean canHoldItem(ItemStack p_28578_) {
		Item item = p_28578_.getItem();
		ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
		return itemstack.isEmpty() && this.ticksSinceEaten > 0 && (item.getFoodProperties().isMeat());
	}

	private void spitOutItem(ItemStack p_28602_) {
		if (!p_28602_.isEmpty() && !this.level().isClientSide) {
			ItemEntity itementity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, p_28602_);
			itementity.setPickUpDelay(40);
			itementity.setThrower(this.getUUID());
			this.level().addFreshEntity(itementity);
		}
	}

	private void dropItemStack(ItemStack p_28606_) {
		ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), p_28606_);
		this.level().addFreshEntity(itementity);
	}

	protected void pickUpItem(ItemEntity p_28514_) {
		ItemStack itemstack = p_28514_.getItem();
		if (this.canHoldItem(itemstack)) {
			int i = itemstack.getCount();
			if (i > 1) {
				this.dropItemStack(itemstack.split(i - 1));
			}

			this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
			this.onItemPickup(p_28514_);
			this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.split(1));
			this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
			this.take(p_28514_, itemstack.getCount());
			p_28514_.discard();
			this.ticksSinceEaten = 0;
		}

	}

	class PiranhaSearchForItemsGoal extends Goal {
		public PiranhaSearchForItemsGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (!Piranha.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
				return false;
			} else if (Piranha.this.getTarget() == null && Piranha.this.getLastHurtByMob() == null) {
				if (Piranha.this.getRandom().nextInt(reducedTickDelay(10)) != 0) {
					return false;
				} else {
					List<ItemEntity> list = Piranha.this.level().getEntitiesOfClass(ItemEntity.class, Piranha.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Piranha.ALLOWED_ITEMS);
					return !list.isEmpty() && Piranha.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
				}
			} else {
				return false;
			}
		}

		public void tick() {
			List<ItemEntity> list = Piranha.this.level().getEntitiesOfClass(ItemEntity.class, Piranha.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Piranha.ALLOWED_ITEMS);
			ItemStack itemstack = Piranha.this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (itemstack.isEmpty() && !list.isEmpty()) {
				Piranha.this.getNavigation().moveTo(list.get(0), (double)1.2F);
			}

		}

		public void start() {
			List<ItemEntity> list = Piranha.this.level().getEntitiesOfClass(ItemEntity.class, Piranha.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Piranha.ALLOWED_ITEMS);
			if (!list.isEmpty()) {
				Piranha.this.getNavigation().moveTo(list.get(0), (double)1.2F);
			}

		}
	}

}
