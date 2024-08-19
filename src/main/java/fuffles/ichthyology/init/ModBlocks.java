package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlocks
{
    @SuppressWarnings("deprecation")
    public static final RegistryRelay<Block> REGISTRY = new RegistryRelay<>(BuiltInRegistries.BLOCK, Ichthyology::id);



    public static final class Tags
    {
        public static final TagKey<Block> AMPHIBIOUS_EGGS = tag("amphibious_eggs");

        private static TagKey<Block> tag(String path)
        {
            return TagKey.create(Registries.BLOCK, Ichthyology.id(path));
        }
    }
}
