package fuffles.ichthyology.common.entity;

import javax.annotation.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModBiomeModifiers;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModItems;
import fuffles.ichthyology.init.ModSoundEvents;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

public class Carp extends AbstractModFish {
	private static final EntityDataAccessor<Carp.Variant> VARIANT_ID = SynchedEntityData.defineId(Carp.class, ModEntityDataSerializers.CARP_VARIANT);
	private static final String TAG_VARIANT = "Variant";

	public Carp(EntityType<? extends AbstractModFish> entityType, Level level)
	{
		super(entityType, level);
	}
	public static AttributeSupplier.Builder createAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
	}

	public static int createTagToFishType(CompoundTag tag)
	{
		Carp.Variant variant = Carp.Variant.readVariant(tag);
		return variant != null ? variant.getId() : 0;
	}

	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle)
	{
		if (stack.hasTag())
		{
			Carp.Variant variant = Carp.Variant.readVariant(stack.getTag());
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
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(ModItems.Tags.CARP_FOOD), false));
	}

	@NotNull
	public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		Optional<Variant> special = Arrays.stream(Variant.SPECIAL).filter(variant -> variant.getAscensionIngredient().equals(stack.getItem())).findFirst();

		if (!this.getVariant().isSpecial() && special.isPresent())
		{
			if (!this.level().isClientSide)
				this.setVariant(special.get());

			if (!player.getAbilities().instabuild)
			{
				stack.shrink(1);
				if (stack.hasCraftingRemainingItem())
				{
					player.addItem(stack.getCraftingRemainingItem());
				}
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}
		else if (stack.is(ModItems.Tags.CARP_FOOD))
		{
			if (!this.level().isClientSide() && this.random.nextInt(20) == 0)
			{
				this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.SHINY_SCALE)));
			}
			if (!player.getAbilities().instabuild)
			{
				stack.shrink(1);
				if (stack.hasCraftingRemainingItem())
				{
					player.addItem(stack.getCraftingRemainingItem());
				}
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}
		else
		{
			return super.mobInteract(player, hand);
		}
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(VARIANT_ID, Carp.Variant.WILD);
	}

	public void addAdditionalSaveData(@NotNull CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		this.getVariant().writeVariant(compound);
	}

	public void readAdditionalSaveData(@NotNull CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		Carp.Variant variant = Carp.Variant.readVariant(compound);
		if (variant != null)
			this.setVariant(variant);
	}

	public Carp.Variant getVariant()
	{
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Carp.Variant variant)
	{
		this.entityData.set(VARIANT_ID, variant);
	}

	@Nullable
	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag)
	{
		Ichthyology.LOG.info("finalizing carp spawn");
		if (reason == MobSpawnType.BUCKET)
		{
			return spawnData;
		}
		else
		{
			Carp.Variant[] picker = Carp.Variant.NON_SPECIAL;
			if (reason == MobSpawnType.NATURAL)
			{
				if (level.getBiome(this.blockPosition()).is(ModBiomeModifiers.Tags.SPAWNS_JAPANESE_VARIANTS))
					picker = Carp.Variant.JAPANESE;
				else
					picker = Carp.Variant.GLOBAL;
			}
			this.setVariant(Util.getRandom(picker, level.getRandom()));
			Ichthyology.LOG.info("getVariant => " + this.getVariant().getName());
			return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
		}
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
		Carp.Variant variant = Carp.Variant.readVariant(tag);
		if (variant != null)
			this.setVariant(variant);
		else
			this.setVariant(Util.getRandom(Carp.Variant.NON_SPECIAL, this.level().getRandom()));
	}

	@NotNull
	@Override
	public ItemStack getBucketItemStack()
	{
		return new ItemStack(ModItems.CARP_BUCKET);
	}

	/*-F: Technically unused?
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.CARP_AMBIENT;
    }
    */

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.CARP_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource)
	{
		return ModSoundEvents.CARP_HURT;
	}

	@NotNull
	@Override
	protected SoundEvent getFlopSound() {
		return ModSoundEvents.CARP_FLOP;
	}

	public static enum Variant
	{
		WILD(false, null, Ichthyology.id("textures/entity/carp/wild.png")),
		ASAGI(true, null, Ichthyology.id("textures/entity/carp/asagi.png")),
		HI_UTSURI(true, null, Ichthyology.id("textures/entity/carp/hi_utsuri.png")),
		KARASUGOI(true, null, Ichthyology.id("textures/entity/carp/karasugoi.png")),
		KI_UTSURI(true, null, Ichthyology.id("textures/entity/carp/ki_utsuri.png")),
		KOHAKU(true, null, Ichthyology.id("textures/entity/carp/kohaku.png")),
		KUJAKU(true, null, Ichthyology.id("textures/entity/carp/kujaku.png")),
		ORENJI_OGON(true, null, Ichthyology.id("textures/entity/carp/orenji_ogon.png")),
		PLATINUM_OGON(true, null, Ichthyology.id("textures/entity/carp/platinum_ogon.png")),
		SANKE(true, null, Ichthyology.id("textures/entity/carp/sanke.png")),
		SHIRO_UTSURI(true, null, Ichthyology.id("textures/entity/carp/shiro_utsuri.png")),
		TANCHO(true, null, Ichthyology.id("textures/entity/carp/tancho.png")),
		YAMABUKI_OGON(true, null, Ichthyology.id("textures/entity/carp/yamabuki_ogon.png")),
		KOIRYUJIN(false, Items.ENCHANTED_GOLDEN_APPLE, Ichthyology.id("textures/entity/carp/dragon.png")),
		ENDARYUJIN(false, Items.DRAGON_BREATH, Ichthyology.id("textures/entity/carp/ender_dragon.png"));

		public static final Carp.Variant[] VALUES = values();
		public static final Carp.Variant[] NON_SPECIAL = Arrays.stream(values()).filter(variant -> !variant.isSpecial()).toList().toArray(new Carp.Variant[0]);
		public static final Carp.Variant[] GLOBAL = Arrays.stream(values()).filter(variant -> !variant.isJapanese() && !variant.isSpecial()).toList().toArray(new Carp.Variant[0]);
		public static final Carp.Variant[] JAPANESE = Arrays.stream(values()).filter(Carp.Variant::isJapanese).toList().toArray(new Carp.Variant[0]);
		public static final Carp.Variant[] SPECIAL = Arrays.stream(values()).filter(Carp.Variant::isSpecial).toList().toArray(new Carp.Variant[0]);
		private static final IntFunction<Carp.Variant> BY_ID = ByIdMap.continuous(Carp.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

		private final boolean japanese;
		private final Item ascendsWith;
		private final ResourceLocation texture;

		private Variant(boolean japanese, Item ascendsWith, ResourceLocation texture)
		{
			this.japanese = japanese;
			this.ascendsWith = ascendsWith;
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

		public boolean isSpecial()
		{
			return this.ascendsWith != null;
		}

		public Item getAscensionIngredient()
		{
			return this.ascendsWith;
		}

		@OnlyIn(Dist.CLIENT)
		public ResourceLocation getTexture()
		{
			return this.texture;
		}

		public String getVariantDescriptionId()
		{
			return "entity." + Ichthyology.ID + ".carp.variant." +  this.getName();
		}

		public void writeVariant(CompoundTag tag)
		{
			tag.putString(TAG_VARIANT, this.getName());
		}

		public static Carp.Variant byId(int i)
		{
			return BY_ID.apply(i);
		}

		@Nullable
		public static Carp.Variant byData(String name)
		{
			for (Carp.Variant variant : VALUES)
			{
				if (variant.getName().equals(name))
					return variant;
			}
			return null;
		}

		@Nullable
		public static Carp.Variant readVariant(CompoundTag tag)
		{
			return byData(tag.getString(TAG_VARIANT));
		}
	}
}
