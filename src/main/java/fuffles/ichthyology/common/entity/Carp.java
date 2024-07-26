package fuffles.ichthyology.common.entity;

import javax.annotation.Nullable;

import fuffles.ichthyology.init.IItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;

public class Carp extends AbstractIchthyologyFish {
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(Carp.class, EntityDataSerializers.INT);
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD, Items.POTATO, Items.CARROT);

	public Carp(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
	}
	
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();
		if (item == Items.ENCHANTED_GOLDEN_APPLE) {
			this.setColor(13);
			if (!player.isCreative()) stack.shrink(1);
			return InteractionResult.SUCCESS;
		} else if (item == Items.DRAGON_BREATH) {
			this.setColor(14);
			if (!player.isCreative()) { 
				stack.shrink(1);
				player.addItem(new ItemStack(Items.GLASS_BOTTLE));
			}
			return InteractionResult.SUCCESS;
		} else if (FOOD_ITEMS.test(stack)) {
			if (random.nextInt(20) == 0) {
				ItemEntity scale = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(IItems.SHINY_SCALE.get()));
				this.level().addFreshEntity(scale);
			}
			if (!player.isCreative()) { 
				stack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		} else {
			return super.mobInteract(player, hand);
		}
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

	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_27528_, DifficultyInstance p_27529_, MobSpawnType p_27530_, @Nullable SpawnGroupData p_27531_, @Nullable CompoundTag p_27532_) {
		super.finalizeSpawn(p_27528_, p_27529_, p_27530_, p_27531_, p_27532_);
		if (p_27530_ == MobSpawnType.NATURAL) {
			if (p_27528_.getBiome(this.blockPosition()).is(Biomes.CHERRY_GROVE)) {
				this.setColor(random.nextInt(12) + 1);
			} else this.setColor(0);
		} else this.setColor(random.nextInt(13));
		return p_27531_;
	}

	@Override
	public void saveToBucketTag(ItemStack bucket) {
		CompoundTag nbt = bucket.getOrCreateTag();
		nbt.putInt("Color", this.getColor());
		if (this.hasCustomName()) {
			bucket.setHoverName(this.getCustomName());
		}
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(IItems.CARP_BUCKET.get());
	}
}
