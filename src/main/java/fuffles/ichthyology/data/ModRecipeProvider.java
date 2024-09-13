package fuffles.ichthyology.data;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider
{
    public ModRecipeProvider(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer)
    {
        cookedFood(writer, ModItems.CARP, ModItems.COOKED_CARP);
        cookedFood(writer, ModItems.PERCH, ModItems.COOKED_PERCH);
        cookedFood(writer, ModItems.PIRANHA, ModItems.COOKED_PIRANHA);
        cookedFood(writer, ModItems.PLECO, ModItems.COOKED_PLECO);
        cookedFood(writer, ModItems.TILAPIA, ModItems.COOKED_TILAPIA);
        mono2x2(writer, ModItems.Tags.TUBE_CORAL_FRONDS, Items.TUBE_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.BRAIN_CORAL_FRONDS, Items.BRAIN_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.BUBBLE_CORAL_FRONDS, Items.BUBBLE_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.FIRE_CORAL_FRONDS, Items.FIRE_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.HORN_CORAL_FRONDS, Items.HORN_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
    }

    private static void mono2x2(Consumer<FinishedRecipe> writer, TagKey<Item> tag, Item result, RecipeCategory category)
    {
        ShapedRecipeBuilder.shaped(category, result, 1).define('#', tag).pattern("##").pattern("##").unlockedBy("has_" + tag.location().getPath(), has(tag)).save(writer, Ichthyology.id(getItemName(result)));
    }

    private static void cookedFood(Consumer<FinishedRecipe> writer, Item raw, Item cooked)
    {
        Ingredient rawIngredient = Ingredient.of(raw);
        SimpleCookingRecipeBuilder.smelting(rawIngredient, RecipeCategory.FOOD, cooked, 0.35F, 200).unlockedBy(getHasName(raw), has(raw)).save(writer);
        SimpleCookingRecipeBuilder.smoking(rawIngredient, RecipeCategory.FOOD, cooked, 0.35F, 100).unlockedBy(getHasName(raw), has(raw)).save(writer, getItemId(cooked) + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(rawIngredient, RecipeCategory.FOOD, cooked, 0.35F, 600).unlockedBy(getHasName(raw), has(raw)).save(writer, getItemId(cooked) + "_from_campfire_cooking");
    }

    @SuppressWarnings("deprecation")
    protected static String getItemId(ItemLike itemLike)
    {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString();
    }
}
