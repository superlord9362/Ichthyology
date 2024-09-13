package fuffles.ichthyology.data;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

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
        this.tag(ModItems.Tags.PERCH_EDIBLES).add(Items.EGG, Items.FROGSPAWN);
        this.tag(ModItems.Tags.TUBE_CORAL_FRONDS).add(Items.TUBE_CORAL, Items.TUBE_CORAL_FAN);
        this.tag(ModItems.Tags.BRAIN_CORAL_FRONDS).add(Items.BRAIN_CORAL, Items.BRAIN_CORAL_FAN);
        this.tag(ModItems.Tags.BUBBLE_CORAL_FRONDS).add(Items.BUBBLE_CORAL, Items.BUBBLE_CORAL_FAN);
        this.tag(ModItems.Tags.FIRE_CORAL_FRONDS).add(Items.FIRE_CORAL, Items.FIRE_CORAL_FAN);
        this.tag(ModItems.Tags.HORN_CORAL_FRONDS).add(Items.HORN_CORAL, Items.HORN_CORAL_FAN);
    }
}
