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
        this.tag(ModBlocks.Tags.STRIPPED_LOGS).add(Blocks.STRIPPED_ACACIA_LOG).add(Blocks.STRIPPED_ACACIA_WOOD).add(Blocks.STRIPPED_BAMBOO_BLOCK).add(Blocks.STRIPPED_BIRCH_LOG).add(Blocks.STRIPPED_BIRCH_WOOD).add(Blocks.STRIPPED_CHERRY_LOG).add(Blocks.STRIPPED_CHERRY_WOOD).add(Blocks.STRIPPED_CRIMSON_HYPHAE).add(Blocks.STRIPPED_CRIMSON_STEM).add(Blocks.STRIPPED_DARK_OAK_LOG).add(Blocks.STRIPPED_DARK_OAK_WOOD).add(Blocks.STRIPPED_JUNGLE_LOG).add(Blocks.STRIPPED_JUNGLE_WOOD).add(Blocks.STRIPPED_MANGROVE_LOG).add(Blocks.STRIPPED_MANGROVE_WOOD).add(Blocks.STRIPPED_OAK_LOG).add(Blocks.STRIPPED_OAK_WOOD).add(Blocks.STRIPPED_SPRUCE_LOG).add(Blocks.STRIPPED_SPRUCE_WOOD).add(Blocks.STRIPPED_WARPED_HYPHAE).add(Blocks.STRIPPED_WARPED_STEM);
    }
}
