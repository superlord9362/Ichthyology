package fuffles.ichthyology.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBiomeTagsProvider extends BiomeTagsProvider
{
    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, provider, Ichthyology.ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider)
    {
        this.tag(ModBiomes.Tags.SPAWNS_JAPANESE_VARIANTS).add(Biomes.CHERRY_GROVE);
    }
}
