package fuffles.ichthyology.common.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import fuffles.ichthyology.init.IItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Perch extends AbstractIchthyologyFish {
	static final Predicate<ItemEntity> ALLOWED_ITEMS = (p_289438_) -> {
		return !p_289438_.hasPickUpDelay() && p_289438_.isAlive();
	};
	private int ticksSinceEaten;

	public Perch(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(IItems.PERCH_BUCKET.get());
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.2F, true));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(0, new PerchSearchForItemsGoal());
		this.goalSelector.addGoal(1, new PerchEatEggsGoal((double)1.2F, 12, 1));
	}

	public boolean doHurtTarget(Entity p_28319_) {
		boolean flag = p_28319_.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		if (flag) {
			this.doEnchantDamageEffects(this, p_28319_);
		}
		return flag;
	}

	public void aiStep() {
		super.aiStep();
		for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4, 4, 4))) {
			if ((entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight()) < (this.getBbWidth() * this.getBbWidth() * this.getBbHeight())) this.setTarget(entity);
			else if (this.getLastHurtByMob() != null) {
				if (!this.getLastHurtByMob().isAlive()) this.setTarget(null);
			}
		}
		if (this.getTarget() != null) {
			if (!this.getTarget().isAlive()) this.setTarget(null);
		} 
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

	public boolean canTakeItem(ItemStack p_28552_) {
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(p_28552_);
		if (!this.getItemBySlot(equipmentslot).isEmpty()) {
			return false;
		} else {
			return equipmentslot == EquipmentSlot.MAINHAND && super.canTakeItem(p_28552_);
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean canEatItem(ItemStack itemStackIn) {
		return itemStackIn.getItem().getFoodProperties().isMeat() || itemStackIn.getItem() == Items.FROGSPAWN;
	}

	public boolean canEquipItem(ItemStack stack) {
		ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
		return itemstack.isEmpty() && this.canEatItem(stack);
	}

	public boolean canHoldItem(ItemStack p_28578_) {
		Item item = p_28578_.getItem();
		ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
		return itemstack.isEmpty() || this.ticksSinceEaten > 0 && item.isEdible() && !itemstack.getItem().isEdible();
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
	
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		super.handleEntityEvent(id);
	}

	protected void pickUpItem(ItemEntity p_28514_) {
		ItemStack itemstack = p_28514_.getItem();
		if (this.canEquipItem(itemstack)) {
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

	class PerchSearchForItemsGoal extends Goal {
		public PerchSearchForItemsGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (!Perch.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
				return false;
			} else if (Perch.this.getTarget() == null && Perch.this.getLastHurtByMob() == null) {
				if (Perch.this.getRandom().nextInt(reducedTickDelay(10)) != 0) {
					return false;
				} else {
					List<ItemEntity> list = Perch.this.level().getEntitiesOfClass(ItemEntity.class, Perch.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Perch.ALLOWED_ITEMS);
					return !list.isEmpty() && Perch.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
				}
			} else {
				return false;
			}
		}

		public void tick() {
			List<ItemEntity> list = Perch.this.level().getEntitiesOfClass(ItemEntity.class, Perch.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Perch.ALLOWED_ITEMS);
			ItemStack itemstack = Perch.this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (itemstack.isEmpty() && !list.isEmpty()) {
				Perch.this.getNavigation().moveTo(list.get(0), (double)1.2F);
			}

		}

		public void start() {
			List<ItemEntity> list = Perch.this.level().getEntitiesOfClass(ItemEntity.class, Perch.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Perch.ALLOWED_ITEMS);
			if (!list.isEmpty()) {
				Perch.this.getNavigation().moveTo(list.get(0), (double)1.2F);
			}

		}
	}

	public class PerchEatEggsGoal extends MoveToBlockGoal {
		@SuppressWarnings("unused")
		private static final int WAIT_TICKS = 40;
		protected int ticksWaited;

		public PerchEatEggsGoal(double p_28675_, int p_28676_, int p_28677_) {
			super(Perch.this, p_28675_, p_28676_, p_28677_);
		}

		public double acceptedDistance() {
			return 2.0D;
		}

		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		protected boolean isValidTarget(LevelReader p_28680_, BlockPos p_28681_) {
			BlockState blockstate = p_28680_.getBlockState(p_28681_);
			return blockstate.is(Blocks.FROGSPAWN);
		}

		public void tick() {
			if (this.isReachedTarget()) {
				if (this.ticksWaited >= 40) {
					this.onReachedTarget();
				} else {
					++this.ticksWaited;
				}
			}

			super.tick();
		}

		protected void onReachedTarget() {
			if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(Perch.this.level(), Perch.this)) {
				BlockState blockstate = Perch.this.level().getBlockState(this.blockPos);
				this.pickEggs(blockstate);

			}
		}

		private void pickEggs(BlockState p_148929_) {
			ItemStack itemstack = Perch.this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (itemstack.isEmpty()) {
				Perch.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FROGSPAWN));
			}
			Perch.this.level().setBlock(this.blockPos, Blocks.AIR.defaultBlockState(), 0);
		}

		public boolean canUse() {
			return !Perch.this.isSleeping() && super.canUse();
		}

		public void start() {
			this.ticksWaited = 0;
			super.start();
		}
	}

}
