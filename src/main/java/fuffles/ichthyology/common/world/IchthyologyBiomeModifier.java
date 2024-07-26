package fuffles.ichthyology.common.world;

import com.mojang.serialization.Codec;

import fuffles.ichthyology.init.IBiomeModifiers;
import fuffles.ichthyology.init.IEntities;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class IchthyologyBiomeModifier implements BiomeModifier {
	public static final IchthyologyBiomeModifier INSTANCE = new IchthyologyBiomeModifier();

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD) {
			if (biome.is(BiomeTags.IS_OVERWORLD)) {
				if (biome.is(BiomeTags.IS_FOREST)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.GOLDFISH.get(), 8, 2, 5));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.CARP.get(), 7, 2, 5));
				}
				if (biome.is(Biomes.CHERRY_GROVE)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.GOLDFISH.get(), 8, 2, 5));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.CARP.get(), 7, 2, 5));
				}
				if (biome.is(Biomes.PLAINS)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.GOLDFISH.get(), 8, 2, 5));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.CARP.get(), 7, 2, 5));
				}
				if (biome.is(BiomeTags.IS_SAVANNA)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.TILAPIA.get(), 6, 1, 3));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PRINCESS_CICHLID.get(), 8, 4, 10));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.KASANGA_CICHLID.get(), 8, 3, 7));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.SAULOSI_CICHLID.get(), 8, 4, 8));
				}
				if (biome.is(Biomes.DESERT)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PRINCESS_CICHLID.get(), 8, 4, 10));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.KASANGA_CICHLID.get(), 8, 3, 7));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.SAULOSI_CICHLID.get(), 8, 4, 8));
				}
				if (biome.is(BiomeTags.IS_JUNGLE)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PIRANHA.get(), 8, 3, 7));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.DISCUS.get(), 12, 3, 6));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.ANGELFISH.get(), 10, 2, 4));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.NEON_TETRA.get(), 14, 6, 12));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PLECO.get(), 6, 1, 2));
				}
				if (biome.is(Biomes.RIVER)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PERCH.get(), 8, 1, 2));
				}
				if (biome.is(Biomes.FROZEN_RIVER)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PERCH.get(), 8, 1, 2));
				}
				if (biome.is(Biomes.SWAMP)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PERCH.get(), 3, 1, 2));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.PLECO.get(), 6, 1, 2));
				}
				builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(IEntities.BLIND_CAVE_TETRA.get(), 10, 3, 6));
			}
		}
	}
	
	@Override
	public Codec<? extends BiomeModifier> codec() {
		return IBiomeModifiers.ICHTHYOLOGY_BIOME_MODIFIER_TYPE.get();
	}
	
}
