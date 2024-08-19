package fuffles.ichthyology.common.world;

import com.mojang.serialization.Codec;

import fuffles.ichthyology.init.ModBiomeModifiers;
import fuffles.ichthyology.init.ModEntityTypes;
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
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.GOLDFISH, 8, 2, 5));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.CARP, 7, 2, 5));
				}
				if (biome.is(Biomes.CHERRY_GROVE)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.GOLDFISH, 8, 2, 5));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.CARP, 7, 2, 5));
				}
				if (biome.is(Biomes.PLAINS)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.GOLDFISH, 8, 2, 5));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.CARP, 7, 2, 5));
				}
				if (biome.is(BiomeTags.IS_SAVANNA)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.TILAPIA, 6, 1, 3));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.AFRICAN_CICHLID, 8, 4, 8));
				}
				if (biome.is(Biomes.DESERT)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.AFRICAN_CICHLID, 8, 4, 8));
				}
				if (biome.is(BiomeTags.IS_JUNGLE)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.PIRANHA, 8, 3, 7));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.DISCUS, 12, 3, 6));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.ANGELFISH, 10, 2, 4));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.NEON_TETRA, 14, 6, 12));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.PLECO, 6, 1, 2));
				}
				if (biome.is(Biomes.RIVER)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.PERCH, 8, 1, 2));
				}
				if (biome.is(Biomes.FROZEN_RIVER)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.PERCH, 8, 1, 2));
				}
				if (biome.is(Biomes.SWAMP)) {
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.PERCH, 3, 1, 2));
					builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.PLECO, 6, 1, 2));
				}
				builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ModEntityTypes.BLIND_CAVE_TETRA, 10, 3, 6));
			}
		}
	}
	
	@Override
	public Codec<? extends BiomeModifier> codec() {
		return ModBiomeModifiers.MAIN.get();
	}
	
}
