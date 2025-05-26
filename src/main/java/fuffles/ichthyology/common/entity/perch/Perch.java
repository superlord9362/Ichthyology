package fuffles.ichthyology.common.entity.perch;

import java.util.List;
import java.util.function.IntFunction;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.mojang.serialization.Dynamic;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.AbstractModFish;
import fuffles.ichthyology.common.entity.ai.sensing.AdvancedNearestItemSensor;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModItems;
import fuffles.ichthyology.init.ModSoundEvents;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class Perch extends AbstractModFish implements AdvancedNearestItemSensor.User {
	private static final EntityDataAccessor<Perch.Variant> VARIANT_ID = SynchedEntityData.defineId(Perch.class, ModEntityDataSerializers.PERCH_VARIANT);
	public static final String TAG_VARIANT = "Variant";


	public Perch(EntityType<? extends AbstractModFish> entityType, Level level)
	{
		super(entityType, level);
		this.setCanPickUpLoot(true);
	}
	
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT_ID, Perch.Variant.COMMON);
	}
	
	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.getVariant().writeVariant(compound);
	}
	
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		Perch.Variant variant = Perch.Variant.readVariant(compound);
		if (variant != null) this.setVariant(variant);
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
		if (variant != null) this.setVariant(variant);
		else this.setVariant(Perch.Variant.byId(this.level().getRandom().nextInt(Perch.Variant.VALUES.length)));
	}

	public Perch.Variant getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Perch.Variant variant) {
		this.entityData.set(VARIANT_ID, variant);
	}
	
	@NotNull
	@Override
	protected Brain.Provider<Perch> brainProvider()
	{
		return Brain.provider(PerchAi.MEMORY_TYPES, PerchAi.SENSOR_TYPES);
	}

	@NotNull
	protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic)
	{
		return PerchAi.makeBrain(this.brainProvider().makeBrain(dynamic));
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public Brain<Perch> getBrain()
	{
		return (Brain<Perch>)super.getBrain();
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
	
	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle) {
		if (stack.hasTag()) {
			Perch.Variant variant = Perch.Variant.readVariant(stack.getTag());
			if (variant != null) {
				tooltipComponents.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
				return true;
			}
		}
		return false;
	}

	@Override
	protected void sendDebugPackets()
	{
		super.sendDebugPackets();
		DebugPackets.sendEntityBrain(this);
	}

	@Override
	protected void customServerAiStep()
	{
		this.level().getProfiler().push("perchBrain");
		this.getBrain().tick((ServerLevel)this.level(), this);
		this.level().getProfiler().pop();
		this.level().getProfiler().push("perchActivityUpdate");
		PerchAi.updateActivity(this);
		this.level().getProfiler().pop();
		//BrainUtil.dumpBrain(this.getBrain());
		super.customServerAiStep();
	}

	public void aiStep() {
		super.aiStep();
	}

	@Override
	public boolean wantsToPickUp(ItemEntity itemEntity)
	{
		return itemEntity.isInWater();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean wantsToPickUp(ItemStack stack)
	{
		return (stack.isEdible() && stack.getItem().getFoodProperties().isMeat()) || stack.is(ModItems.Tags.PERCH_EDIBLES);
	}

	@Override
	public boolean canPickUpLoot()
	{
		return !this.hasItemInSlot(EquipmentSlot.MAINHAND);
	}

	@Override
	public void onItemPickup(ItemEntity itemEntity)
	{
		super.onItemPickup(itemEntity);
		this.getBrain().setMemory(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS, PerchAi.TIME_BETWEEN_BITES.sample(this.level().getRandom()));
	}

	private void spitOut(ItemStack stack)
	{
		if (!stack.isEmpty())
		{
			ItemEntity resultEntity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY(), this.getZ() + this.getLookAngle().z, stack);
			resultEntity.setPickUpDelay(40);
			resultEntity.setThrower(this.getUUID());
			this.level().addFreshEntity(resultEntity);
		}
	}

	public void eatItem(ItemStack stack)
	{
		if (stack.is(Tags.Items.GLASS_PANES))
		{
			this.kill();
		}
		else
		{
			spitOut(stack.getItem().finishUsingItem(stack, this.level(), this));
			if (!this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
				this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.heal(0.5F);
		}
	}

	public void eatBlockState(BlockState state)
	{
		this.heal(0.5F);
	}

	@Override
	public void handleEntityEvent(byte eventID)
	{
		switch(eventID)
		{
			case EntityEvent.FOX_EAT:
				ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
				if (!stack.isEmpty())
				{
					for(int i = 0; i < 5; ++i)
					{
						Vector3f delta = new Vector3f((this.random.nextFloat() - 0.5F) * 0.1F, this.random.nextFloat() * 0.1F + 0.1F, 0F).rotateX(-this.getXRot() * Mth.DEG_TO_RAD).rotateY(-this.getYRot() * Mth.DEG_TO_RAD);
						this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), this.getX() + this.getLookAngle().x * 0.33D, this.getY() - 0.15D, this.getZ() + this.getLookAngle().z * 0.33D, delta.x, delta.y + 0.05D, delta.z);
					}
				}
				break;
			default:
				super.handleEntityEvent(eventID);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag)
	{
		PerchAi.initMemories(this, level.getRandom());
		return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
	}

	public SoundEvent getEatSound()
	{
		return ModSoundEvents.PERCH_EAT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.PERCH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource)
	{
		return ModSoundEvents.PERCH_HURT;
	}

	@NotNull
	@Override
	protected SoundEvent getFlopSound()
	{
		return ModSoundEvents.PERCH_FLOP;
	}
	
	public static enum Variant {
		COMMON("common", Ichthyology.id("textures/entity/perch/common.png")),
		YELLOW("yellow", Ichthyology.id("textures/entity/perch/yellow.png"));

		public static final Perch.Variant[] VALUES = values();
		private static final IntFunction<Perch.Variant> BY_ID = ByIdMap.continuous(Perch.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

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
	
}
