package fuffles.ichthyology.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider
{
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, Ichthyology.ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider)
    {
        this.tag(ModBlocks.Tags.AMPHIBIOUS_EGGS).add(Blocks.FROGSPAWN).add(ModBlocks.CATFISH_ROE).add(ModBlocks.PEACOCK_BASS_ROE).add(ModBlocks.GAR_ROE);
    }
}
