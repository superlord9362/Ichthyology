package fuffles.ichthyology.data;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModBiomeModifierProvider
{
    public static void bootstrap(BootstapContext<BiomeModifier> context)
    {
        registerSpawn(context, BiomeTags.IS_FOREST,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.GOLDFISH, 8, 2, 5),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CARP, 7, 2, 5)
        );
        registerSpawn(context, Biomes.CHERRY_GROVE,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.GOLDFISH, 8, 2, 5),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CARP, 7, 2, 5)
        );
        registerSpawn(context, Biomes.PLAINS,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.GOLDFISH, 8, 2, 5),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CARP, 7, 2, 5)
        );
        registerSpawn(context, BiomeTags.IS_SAVANNA,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.TILAPIA, 6, 1, 3),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.AFRICAN_CICHLID, 8, 4, 8)
        );
        registerSpawn(context, Biomes.DESERT,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.AFRICAN_CICHLID, 8, 4, 8)
        );
        registerSpawn(context, BiomeTags.IS_JUNGLE,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PIRANHA, 8, 3, 7),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.DISCUS, 12, 3, 6),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.ANGELFISH, 10, 2, 4),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.NEON_TETRA, 14, 6, 12),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PLECO, 6, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PEACOCK_BASS, 7, 1, 2)
        );
        registerSpawn(context, Biomes.RIVER,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PERCH, 8, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CRAYFISH, 10, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CATFISH, 7, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.STURGEON, 10, 1, 2)
        );
        registerSpawn(context, Biomes.FROZEN_RIVER,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PERCH, 8, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.STURGEON, 10, 1, 2)
        );
        registerSpawn(context, Biomes.SWAMP,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PERCH, 3, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.PLECO, 6, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CRAYFISH, 7, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.GAR, 8, 1, 2)
        );
        registerSpawn(context, Biomes.MANGROVE_SWAMP,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.ARCHERFISH, 7, 1, 3),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.MUDSKIPPER, 10, 1, 3),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.FIDDLER_CRAB, 8, 2, 4)
        );
        registerSpawn(context, Biomes.DRIPSTONE_CAVES,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.OLM, 5, 1, 2)
        );
        registerSpawn(context, Biomes.DEEP_COLD_OCEAN,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.STURGEON, 100, 1, 2)
        );
        registerSpawn(context, BiomeTags.IS_OVERWORLD,
                new MobSpawnSettings.SpawnerData(ModEntityTypes.BLIND_CAVE_TETRA, 10, 3, 6),
                new MobSpawnSettings.SpawnerData(ModEntityTypes.CRAYFISH, 5, 1, 2)
        );
    }

    private static void registerSpawn(BootstapContext<BiomeModifier> context, TagKey<Biome> biome, MobSpawnSettings.SpawnerData... spawns)
    {
        //-F: Substituting the usual # with _ for biome tags just in case the game complains
        registerSpawn(context, biome.location().getNamespace() + "/_" + biome.location().getPath() + "/add_spawns", context.lookup(Registries.BIOME).getOrThrow(biome), spawns);
    }

    private static void registerSpawn(BootstapContext<BiomeModifier> context, ResourceKey<Biome> biome, MobSpawnSettings.SpawnerData... spawns)
    {
        registerSpawn(context, biome.location().getNamespace() + "/" + biome.location().getPath() + "/add_spawns", HolderSet.direct(context.lookup(Registries.BIOME).getOrThrow(biome)), spawns);
    }

    private static void registerSpawn(BootstapContext<BiomeModifier> context, String id, HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData... spawns)
    {
        registerSpawn(context, Ichthyology.id(id), biomes, spawns);
    }

    private static void registerSpawn(BootstapContext<BiomeModifier> context, ResourceLocation id, HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData... spawns)
    {
        context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, id), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, List.of(spawns)));
    }
}