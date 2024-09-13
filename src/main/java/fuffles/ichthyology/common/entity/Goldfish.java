package fuffles.ichthyology.common.entity;

import javax.annotation.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class Goldfish extends AbstractModFish {
	private static final EntityDataAccessor<Goldfish.Variant> VARIANT_ID = SynchedEntityData.defineId(Goldfish.class, ModEntityDataSerializers.GOLDFISH_VARIANT);
	private static final String TAG_VARIANT = "Variant";

	public Goldfish(EntityType<? extends AbstractModFish> entityType, Level level) {
		super(entityType, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
	}

	public static int createTagToFishType(CompoundTag tag)
	{
		Goldfish.Variant variant = Goldfish.Variant.readVariant(tag);
		return variant != null ? variant.getId() : 0;
	}

	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle)
	{
		if (stack.hasTag())
		{
			Goldfish.Variant variant = Goldfish.Variant.readVariant(stack.getTag());
			if (variant != null)
			{
				tooltipComponents.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
				return true;
			}
		}
		return false;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT_ID, Goldfish.Variant.WILD);
	}

	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.getVariant().writeVariant(compound);
	}

	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		Goldfish.Variant variant = Goldfish.Variant.readVariant(compound);
		if (variant != null)
			this.setVariant(variant);
	}

	public Goldfish.Variant getVariant()
	{
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Goldfish.Variant variant)
	{
		this.entityData.set(VARIANT_ID, variant);
	}

	@Nullable
	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag)
	{
		if (reason == MobSpawnType.BUCKET)
		{
			return spawnData;
		}
		else
		{
			Goldfish.Variant[] picker = Goldfish.Variant.VALUES;
			if (reason == MobSpawnType.NATURAL)
			{
				if (level.getBiome(this.blockPosition()).is(ModBiomes.Tags.SPAWNS_JAPANESE_VARIANTS))
					picker = Goldfish.Variant.JAPANESE;
				else
					picker = Goldfish.Variant.GLOBAL;
			}
			this.setVariant(picker[level.getRandom().nextInt(picker.length)]);
			return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
		}

		/*super.finalizeSpawn(p_27528_, p_27529_, p_27530_, p_27531_, p_27532_);
		if (p_27530_ == MobSpawnType.NATURAL) {
			if (p_27528_.getBiome(this.blockPosition()).is(Biomes.CHERRY_GROVE)) {
				if (random.nextInt() != 0) {
					int fancy = random.nextInt(6);
					if (fancy == 0) this.setColor(1);
					if (fancy == 1) this.setColor(2);
					if (fancy == 2) this.setColor(3);
					if (fancy == 3) this.setColor(4);
					if (fancy == 4) this.setColor(5);
					if (fancy == 5) this.setColor(6);
				} else this.setColor(0);
			} else {
				if (this.random.nextInt(10) <= 7) this.setColor(0);
				else this.setColor(1);
			}
		} else this.setColor(random.nextInt(7));
		return p_27531_;*/
	}

	@Override
	public void saveToBucketTag(@NotNull ItemStack bucket)
	{
		super.saveToBucketTag(bucket);
		this.getVariant().writeVariant(bucket.getOrCreateTag());
	}

	@Override
	public void loadFromBucketTag(@NotNull CompoundTag tag)
	{
		super.loadFromBucketTag(tag);
		Goldfish.Variant variant = Goldfish.Variant.readVariant(tag);
		if (variant != null)
			this.setVariant(variant);
		else
			this.setVariant(Goldfish.Variant.VALUES[this.level().getRandom().nextInt(Goldfish.Variant.VALUES.length)]);
	}

	@NotNull
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.GOLDFISH_BUCKET);
	}

	/*-F: Technically unused?
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.GOLDFISH_AMBIENT;
    }
    */

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.GOLDFISH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource)
	{
		return ModSoundEvents.GOLDFISH_HURT;
	}

	@NotNull
	@Override
	protected SoundEvent getFlopSound() {
		return ModSoundEvents.GOLDFISH_FLOP;
	}

	public static enum Variant
	{
		WILD(false, Ichthyology.id("textures/entity/goldfish/wild.png")),
		ORANGE(false, Ichthyology.id("textures/entity/goldfish/orange.png")),
		COMET(true, Ichthyology.id("textures/entity/goldfish/comet.png")),
		PEARLSCALE(true, Ichthyology.id("textures/entity/goldfish/pearlscale.png")),
		RANCHU(true, Ichthyology.id("textures/entity/goldfish/ranchu.png")),
		REDCAP(true, Ichthyology.id("textures/entity/goldfish/redcap.png")),
		TELESCOPE(true, Ichthyology.id("textures/entity/goldfish/telescope.png"));

		public static final Goldfish.Variant[] VALUES = values();
		public static final Goldfish.Variant[] GLOBAL = Arrays.stream(values()).filter(variant -> !variant.isJapanese()).toList().toArray(new Goldfish.Variant[0]);
		public static final Goldfish.Variant[] JAPANESE = Arrays.stream(values()).filter(Goldfish.Variant::isJapanese).toList().toArray(new Goldfish.Variant[0]);
		private static final IntFunction<Goldfish.Variant> BY_ID = ByIdMap.continuous(Goldfish.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

		private final boolean japanese;
		private final ResourceLocation texture;

		private Variant(boolean japanese, ResourceLocation texture)
		{
			this.japanese = japanese;
			this.texture = texture;
		}

		public int getId()
		{
			return this.ordinal();
		}

		public String getName()
		{
			return this.toString().toLowerCase();
		}

		public boolean isJapanese()
		{
			return this.japanese;
		}

		@OnlyIn(Dist.CLIENT)
		public ResourceLocation getTexture()
		{
			return this.texture;
		}

		public String getVariantDescriptionId()
		{
			return "entity." + Ichthyology.ID + ".goldfish.variant." +  this.getName();
		}

		public void writeVariant(CompoundTag tag)
		{
			tag.putString(TAG_VARIANT, this.getName());
		}

		public static Goldfish.Variant byId(int i)
		{
			return BY_ID.apply(i);
		}

		@Nullable
		public static Goldfish.Variant byData(String name)
		{
			for (Goldfish.Variant variant : VALUES)
			{
				if (variant.getName().equals(name))
					return variant;
			}
			return null;
		}

		@Nullable
		public static Goldfish.Variant readVariant(CompoundTag tag)
		{
			return byData(tag.getString(TAG_VARIANT));
		}
	}
}
