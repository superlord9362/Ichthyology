package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes
{
    public static class Tags
    {
        public static final TagKey<Biome> SPAWNS_JAPANESE_VARIANTS = tag("spawns_japanese_variants");

        private static TagKey<Biome> tag(String path)
        {
            return TagKey.create(Registries.BIOME, Ichthyology.id(path));
        }
    }
}
