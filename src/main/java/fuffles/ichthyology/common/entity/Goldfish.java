package fuffles.ichthyology.common.entity;

import javax.annotation.Nullable;

import fuffles.ichthyology.init.IItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;

public class Goldfish extends AbstractIchthyologyFish {
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(Goldfish.class, EntityDataSerializers.INT);

	public Goldfish(EntityType<? extends AbstractIchthyologyFish> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
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
		return new ItemStack(IItems.GOLDFISH_BUCKET.get());
	}

}
