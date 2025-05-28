package fuffles.ichthyology.data;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

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
        cookedFood(writer, ModItems.CRAYFISH, ModItems.COOKED_CRAYFISH);
        cookedFood(writer, ModItems.CATFISH_BABY, ModItems.COOKED_CATFISH_BABY);
        cookedFood(writer, ModItems.PEACOCK_BASS_FILET, ModItems.COOKED_PEACOCK_BASS_FILET);
        cookedFood(writer, ModItems.GAR, ModItems.COOKED_GAR);
        mono2x2(writer, ModItems.Tags.TUBE_CORAL_FRONDS, Items.TUBE_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.BRAIN_CORAL_FRONDS, Items.BRAIN_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.BUBBLE_CORAL_FRONDS, Items.BUBBLE_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.FIRE_CORAL_FRONDS, Items.FIRE_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        mono2x2(writer, ModItems.Tags.HORN_CORAL_FRONDS, Items.HORN_CORAL_BLOCK, RecipeCategory.BUILDING_BLOCKS);
        shapelessTag(writer, ModItems.Tags.SMALL_RAW_FISH, ModItems.GROUND_FISH, 1, RecipeCategory.FOOD);
        shapelessTag(writer, ModItems.Tags.MEDIUM_RAW_FISH, ModItems.GROUND_FISH, 2, RecipeCategory.FOOD);
        shapelessTag(writer, ModItems.Tags.LARGE_RAW_FISH, ModItems.GROUND_FISH, 3, RecipeCategory.FOOD);
        shapelessItem(writer, ModItems.PEACOCK_BASS_BABY, ModItems.PEACOCK_BASS_FILET, 1, RecipeCategory.FOOD);
        shapelessItem(writer, ModItems.GAR_BABY, ModItems.GAR, 1, RecipeCategory.FOOD);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.OLMLETTE).requires(Tags.Items.MUSHROOMS).requires(Tags.Items.MUSHROOMS).requires(ModItems.OLM_EGGS).unlockedBy("has_olmlette", has(ModItems.OLMLETTE)).unlockedBy("has_olmspawn", has(ModItems.OLM_EGGS)).unlockedBy("has_brown_mushroom", has(Blocks.BROWN_MUSHROOM)).unlockedBy("has_red_mushroom", has(Blocks.RED_MUSHROOM)).save(writer, Ichthyology.id(getItemName(ModItems.OLMLETTE)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CAVIAR).requires(ModItems.STURGEON_ROE).requires(ModItems.STURGEON_ROE).requires(Items.BREAD).unlockedBy("has_caviar", has(ModItems.CAVIAR)).unlockedBy("has_sturgeon_roe", has(ModItems.STURGEON_ROE)).unlockedBy("has_bread", has(Items.BREAD)).save(writer, Ichthyology.id(getItemName(ModItems.CAVIAR)));
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
    
    private static void shapelessTag(Consumer<FinishedRecipe> writer, TagKey<Item> tag, Item result, int count, RecipeCategory category) {
    	ShapelessRecipeBuilder.shapeless(category, result, count).requires(tag).unlockedBy("has_" + tag.location().getPath(), has(tag)).save(writer, Ichthyology.id(getItemName(result) + "_from_" + tag.location().getPath()));;
    }
    
    private static void shapelessItem(Consumer<FinishedRecipe> writer, Item item, Item result, int count, RecipeCategory category) {
        Ingredient ingredient = Ingredient.of(item);
    	ShapelessRecipeBuilder.shapeless(category, result, count).requires(ingredient).unlockedBy(getHasName(item), has(item)).save(writer, Ichthyology.id(getItemName(result)));
    }
    
    @SuppressWarnings("deprecation")
    protected static String getItemId(ItemLike itemLike)
    {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString();
    }
}
