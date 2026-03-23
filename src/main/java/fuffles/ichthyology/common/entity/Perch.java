package fuffles.ichthyology.common.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.ai.sensing.AdvancedNearestItemSensor;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModItems;
import fuffles.ichthyology.init.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class Perch extends AbstractIchthyologyFish implements AdvancedNearestItemSensor.User {
	private static final EntityDataAccessor<Perch.Variant> VARIANT_ID = SynchedEntityData.defineId(Perch.class,
			ModEntityDataSerializers.PERCH_VARIANT);
	private static final EntityDataAccessor<Boolean> HUNGRY = SynchedEntityData.defineId(Perch.class,
			EntityDataSerializers.BOOLEAN);
	public static final String TAG_VARIANT = "Variant";
	int hungerTick = 2400;
	int activeEatTime = 400;
	static final Predicate<ItemEntity> ALLOWED_ITEMS = (p_289438_) -> {
		return !p_289438_.hasPickUpDelay() && p_289438_.isAlive();
	};

	public Perch(EntityType<? extends AbstractIchthyologyFish> entityType, Level level) {
		super(entityType, level);
		this.setCanPickUpLoot(true);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(0, new PerchEatEggsGoal(1, 10, 10));
		this.goalSelector.addGoal(2, new PerchSearchForItemsGoal());
		this.goalSelector.addGoal(1, new PerchMeleeAttackGoal(1.1F, false));
		this.targetSelector.addGoal(1, new PerchNearestAttackableTargetGoal<>(this, LivingEntity.class, 10, false, false, (p_28605_) -> {
			return p_28605_.getBbWidth() <= this.getBbWidth() && !(p_28605_ instanceof Perch);
		}));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT_ID, Perch.Variant.COMMON);
		this.entityData.define(HUNGRY, false);
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.getVariant().writeVariant(compound);
		compound.putBoolean("IsHungry", this.isHungry());
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		Perch.Variant variant = Perch.Variant.readVariant(compound);
		if (variant != null)
			this.setVariant(variant);
		this.setHungry(compound.getBoolean("IsHungry"));
	}

	@Override
	public void saveToBucketTag(ItemStack pStack) {
		super.saveToBucketTag(pStack);
		this.getVariant().writeVariant(pStack.getOrCreateTag());
	}

	@Override
	public void loadFromBucketTag(CompoundTag pTag) {
		super.loadFromBucketTag(pTag);
		Perch.Variant variant = Perch.Variant.readVariant(pTag);
		if (variant != null)
			this.setVariant(variant);
		else
			this.setVariant(Perch.Variant.byId(this.level().getRandom().nextInt(Perch.Variant.VALUES.length)));
	}

	public Perch.Variant getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Perch.Variant variant) {
		this.entityData.set(VARIANT_ID, variant);
	}

	public boolean isHungry() {
		return this.entityData.get(HUNGRY);
	}

	public void setHungry(boolean isHungry) {
		this.entityData.set(HUNGRY, isHungry);
	}

	@NotNull
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.PERCH_BUCKET);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	public static int createTagToFishType(CompoundTag tag) {
		Perch.Variant variant = Perch.Variant.readVariant(tag);
		return variant != null ? variant.getId() : 0;
	}

	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level,
			@NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle) {
		if (stack.hasTag()) {
			Perch.Variant variant = Perch.Variant.readVariant(stack.getTag());
			if (variant != null) {
				tooltipComponents
				.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
				return true;
			}
		}
		return false;
	}

	public void aiStep() {
		super.aiStep();
		if (hungerTick != 0 && !this.isHungry()) {
			hungerTick--;
		}
		if (hungerTick == 0) {
			this.setHungry(true);
			hungerTick = 2400;
		}
		if (this.hasItemInSlot(EquipmentSlot.MAINHAND)) {
			activeEatTime--;
			if (activeEatTime % 50 == 0) {
				this.playSound(SoundEvents.DOLPHIN_EAT);
				this.level().broadcastEntityEvent(this, (byte)45);
			}
			if (activeEatTime == 0) {
				eatItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
				activeEatTime = 400;
			}
		}
	}

	@Override
	public boolean wantsToPickUp(ItemEntity itemEntity) {
		return itemEntity.isInWater();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean wantsToPickUp(ItemStack stack) {
		return (stack.isEdible() && stack.getItem().getFoodProperties().isMeat()) || stack.is(ModItems.Tags.PERCH_EDIBLES);
	}

	@Override
	public boolean canPickUpLoot() {
		return !this.hasItemInSlot(EquipmentSlot.MAINHAND);
	}

	private void spitOut(ItemStack stack) {
		if (!stack.isEmpty()) {
			ItemEntity resultEntity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY(), this.getZ() + this.getLookAngle().z, stack);
			resultEntity.setPickUpDelay(40);
			resultEntity.setThrower(this.getUUID());
			this.level().addFreshEntity(resultEntity);
		}
	}

	private void dropItemStack(ItemStack pStack) {
		ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), pStack);
		this.level().addFreshEntity(itementity);
	}

	protected void pickUpItem(ItemEntity pItemEntity) {
		ItemStack itemstack = pItemEntity.getItem();
		if (this.canHoldItem(itemstack)) {
			int i = itemstack.getCount();
			if (i > 1) {
				this.dropItemStack(itemstack.split(i - 1));
			}

			this.spitOut(this.getItemBySlot(EquipmentSlot.MAINHAND));
			this.onItemPickup(pItemEntity);
			this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.split(1));
			this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
			this.take(pItemEntity, itemstack.getCount());
			pItemEntity.discard();
		}

	}


	public void eatItem(ItemStack stack) {
		if (stack.is(Tags.Items.GLASS_PANES)) {
			this.kill();
		} else {
			spitOut(stack.getItem().finishUsingItem(stack, this.level(), this));
			if (!this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
				this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.heal(0.5F);
		}
	}

	@Override
	public void handleEntityEvent(byte eventID) {
		switch (eventID) {
		case EntityEvent.FOX_EAT:
			ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (!stack.isEmpty()) {
				for (int i = 0; i < 5; ++i) {
					Vector3f delta = new Vector3f((this.random.nextFloat() - 0.5F) * 0.1F,
							this.random.nextFloat() * 0.1F + 0.1F, 0F).rotateX(-this.getXRot() * Mth.DEG_TO_RAD)
							.rotateY(-this.getYRot() * Mth.DEG_TO_RAD);
					this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack),
							this.getX() + this.getLookAngle().x * 0.33D, this.getY() - 0.15D,
							this.getZ() + this.getLookAngle().z * 0.33D, delta.x, delta.y + 0.05D, delta.z);
				}
			}
			break;
		default:
			super.handleEntityEvent(eventID);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
			@NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		if (reason == MobSpawnType.BUCKET)
			return spawnData;
		else {
			if (reason == MobSpawnType.SPAWN_EGG)
				setVariant(Perch.Variant.byId(level.getRandom().nextInt(Perch.Variant.VALUES.length)));
			else {
				if (level.getBiome(this.blockPosition()).is(Biomes.RIVER))
					this.setVariant(Variant.COMMON);
				else
					this.setVariant(Variant.YELLOW);
			}
		}
		return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
	}

	public SoundEvent getEatSound() {
		return ModSoundEvents.PERCH_EAT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.PERCH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
		return ModSoundEvents.PERCH_HURT;
	}

	@NotNull
	@Override
	protected SoundEvent getFlopSound() {
		return ModSoundEvents.PERCH_FLOP;
	}

	public static enum Variant {
		COMMON("common", Ichthyology.id("textures/entity/perch/common.png")),
		YELLOW("yellow", Ichthyology.id("textures/entity/perch/yellow.png"));

		public static final Perch.Variant[] VALUES = values();
		private static final IntFunction<Perch.Variant> BY_ID = ByIdMap.continuous(Perch.Variant::getId, VALUES,
				ByIdMap.OutOfBoundsStrategy.ZERO);

		private final String name;
		private final ResourceLocation texture;

		private Variant(String name, ResourceLocation texture) {
			this.name = name;
			this.texture = texture;
		}

		public int getId() {
			return this.ordinal();
		}

		public String getName() {
			return this.name;
		}

		@OnlyIn(Dist.CLIENT)
		public ResourceLocation getTexture() {
			return this.texture;
		}

		public String getVariantDescriptionId() {
			return "entity." + Ichthyology.ID + ".perch.variant." + this.getName();
		}

		public void writeVariant(CompoundTag tag) {
			tag.putString(TAG_VARIANT, this.getName());
		}

		public static Perch.Variant byId(int i) {
			return BY_ID.apply(i);
		}

		@Nullable
		public static Perch.Variant byData(String name) {
			for (Perch.Variant variant : VALUES) {
				if (variant.getName().equals(name)) {
					return variant;
				}
			}
			return null;
		}

		@Nullable
		public static Perch.Variant readVariant(CompoundTag tag) {
			return byData(tag.getString(TAG_VARIANT));
		}
	}

	public class PerchEatEggsGoal extends MoveToBlockGoal {
		protected int ticksWaited;

		public PerchEatEggsGoal(double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
			super(Perch.this, pSpeedModifier, pSearchRange, pVerticalSearchRange);
		}

		public double acceptedDistance() {
			return 2.0D;
		}

		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		
		public boolean canContinueToUse() {
			return super.canContinueToUse() && Perch.this.isHungry();
		}
		/**
		 * Return {@code true} to set given position as destination
		 */
		protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
			BlockState blockstate = pLevel.getBlockState(pPos);
			return blockstate.is(ModBlocks.Tags.AMPHIBIOUS_EGGS);
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
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
				Perch.this.playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
				ItemStack itemstack = Perch.this.getItemBySlot(EquipmentSlot.MAINHAND);
				if (itemstack.isEmpty()) {
					Perch.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Perch.this.level().getBlockState(this.blockPos).getBlock().asItem()));
					Perch.this.level().setBlock(this.blockPos, Blocks.AIR.defaultBlockState(), 2);
				}
			}
			stop();
		}

		public boolean canUse() {
			return Perch.this.isHungry() && super.canUse();
		}

		public void stop() {
			Perch.this.setHungry(false);
			super.stop();
		}

		public void start() {
			this.ticksWaited = 0;
			super.start();
		}
	}

	class PerchSearchForItemsGoal extends Goal {
		public PerchSearchForItemsGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			if (!Perch.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
				return false;
			} else if (Perch.this.getTarget() == null && Perch.this.getLastHurtByMob() == null) {
				if (!Perch.this.isHungry()) {
					return false;
				} else if (Perch.this.getRandom().nextInt(reducedTickDelay(10)) != 0) {
					return false;
				} else {
					List<ItemEntity> list = Perch.this.level().getEntitiesOfClass(ItemEntity.class, Perch.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Perch.ALLOWED_ITEMS);
					return !list.isEmpty() && Perch.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
				}
			} else {
				return false;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			List<ItemEntity> list = Perch.this.level().getEntitiesOfClass(ItemEntity.class, Perch.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Perch.ALLOWED_ITEMS);
			ItemStack itemstack = Perch.this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (itemstack.isEmpty() && !list.isEmpty()) {
				Perch.this.getNavigation().moveTo(list.get(0), (double)1.2F);
			}

		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			List<ItemEntity> list = Perch.this.level().getEntitiesOfClass(ItemEntity.class, Perch.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Perch.ALLOWED_ITEMS);
			if (!list.isEmpty()) {
				Perch.this.getNavigation().moveTo(list.get(0), (double)1.2F);
			}

		}
	}

	class PerchMeleeAttackGoal extends MeleeAttackGoal {
		public PerchMeleeAttackGoal(double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
			super(Perch.this, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
		}

		protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
			double d0 = this.getAttackReachSqr(pEnemy);
			if (pDistToEnemySqr <= d0 && this.isTimeToAttack()) {
				this.resetAttackCooldown();
				this.mob.doHurtTarget(pEnemy);
			}

		}

		public void start() {
			super.start();
		}

		public boolean canUse() {
			return super.canUse();
		}
	}

	class PerchNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
		public PerchNearestAttackableTargetGoal(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
			super(mob, targetType, randomInterval, mustSee, mustReach, targetPredicate);
		}
		
		public void tick() {
			super.tick();
		}

		public boolean canUse() {
			return super.canUse() && Perch.this.isHungry();
		}
	}

}
