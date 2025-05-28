package fuffles.ichthyology.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTagsProvider, Ichthyology.ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider)
    {
        this.tag(ModItems.Tags.CARP_FOOD).addTag(Tags.Items.SEEDS).add(Items.POTATO, Items.CARROT, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
        this.tag(ModItems.Tags.PERCH_EDIBLES).add(Items.EGG, Items.FROGSPAWN, ModItems.CATFISH_ROE, ModItems.PEACOCK_BASS_ROE, ModItems.OLM_EGGS, ModItems.STURGEON_ROE, ModItems.GAR_ROE);
        this.tag(ModItems.Tags.TUBE_CORAL_FRONDS).add(Items.TUBE_CORAL, Items.TUBE_CORAL_FAN);
        this.tag(ModItems.Tags.BRAIN_CORAL_FRONDS).add(Items.BRAIN_CORAL, Items.BRAIN_CORAL_FAN);
        this.tag(ModItems.Tags.BUBBLE_CORAL_FRONDS).add(Items.BUBBLE_CORAL, Items.BUBBLE_CORAL_FAN);
        this.tag(ModItems.Tags.FIRE_CORAL_FRONDS).add(Items.FIRE_CORAL, Items.FIRE_CORAL_FAN);
        this.tag(ModItems.Tags.HORN_CORAL_FRONDS).add(Items.HORN_CORAL, Items.HORN_CORAL_FAN);
        this.tag(ModItems.Tags.SMALL_RAW_FISH).add(Items.COD, Items.TROPICAL_FISH, ModItems.BLIND_CAVE_TETRA, ModItems.GOLDFISH, ModItems.TILAPIA, ModItems.AFRICAN_CICHLID, ModItems.PIRANHA, ModItems.DISCUS, ModItems.ANGELFISH, ModItems.NEON_TETRA, ModItems.PLECO, ModItems.ARCHERFISH, ModItems.MUDSKIPPER, ModItems.CATFISH_BABY, ModItems.GAR_BABY, ModItems.STURGEON_BABY);
        this.tag(ModItems.Tags.MEDIUM_RAW_FISH).add(Items.SALMON, Items.PUFFERFISH, ModItems.CARP, ModItems.PERCH);
        this.tag(ModItems.Tags.LARGE_RAW_FISH).add(ModItems.CATFISH, ModItems.PEACOCK_BASS_FILET, ModItems.GAR);
    }
}
