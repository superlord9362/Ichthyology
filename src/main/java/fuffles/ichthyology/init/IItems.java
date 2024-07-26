package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.item.IBucketItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IItems {
	public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, Ichthyology.MOD_ID);
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Ichthyology.MOD_ID);

	public static final RegistryObject<Item> BLIND_CAVE_TETRA_SPAWN_EGG = SPAWN_EGGS.register("blind_cave_tetra_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.BLIND_CAVE_TETRA, 0xDFD0AE, 0xC8675E, new Item.Properties()));
	public static final RegistryObject<Item> GOLDFISH_SPAWN_EGG = SPAWN_EGGS.register("goldfish_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.GOLDFISH, 0xCE4D29, 0xDD8433, new Item.Properties()));
	public static final RegistryObject<Item> TILAPIA_SPAWN_EGG = SPAWN_EGGS.register("tilapia_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.TILAPIA, 0x6E7771, 0xC80322D, new Item.Properties()));
	public static final RegistryObject<Item> PRINCESS_CICHLID_SPAWN_EGG = SPAWN_EGGS.register("princess_cichlid_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.PRINCESS_CICHLID, 0xB19480, 0x75AAA0, new Item.Properties()));
	public static final RegistryObject<Item> SAULOSI_CICHLID_SPAWN_EGG = SPAWN_EGGS.register("saulosi_cichlid_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.SAULOSI_CICHLID, 0x1C63BF, 0x102744, new Item.Properties()));
	public static final RegistryObject<Item> KASANGA_CICHLID_SPAWN_EGG = SPAWN_EGGS.register("kasanga_cichlid_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.KASANGA_CICHLID, 0x2E1F36, 0xAF8721, new Item.Properties()));
	public static final RegistryObject<Item> CARP_SPAWN_EGG = SPAWN_EGGS.register("carp_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.CARP, 0x4A3F39, 0xB1A796, new Item.Properties()));
	public static final RegistryObject<Item> PIRANHA_SPAWN_EGG = SPAWN_EGGS.register("piranha_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.PIRANHA, 0x534E42, 0xA63211, new Item.Properties()));
	public static final RegistryObject<Item> PERCH_SPAWN_EGG = SPAWN_EGGS.register("perch_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.PERCH, 0x87722D, 0x3C3D14, new Item.Properties()));
	public static final RegistryObject<Item> DISCUS_SPAWN_EGG = SPAWN_EGGS.register("discus_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.DISCUS, 0xB65E16, 0x4DABD9, new Item.Properties()));
	public static final RegistryObject<Item> ANGELFISH_SPAWN_EGG = SPAWN_EGGS.register("angelfish_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.ANGELFISH, 0x766124, 0x1B1B1B, new Item.Properties()));
	public static final RegistryObject<Item> NEON_TETRA_SPAWN_EGG = SPAWN_EGGS.register("neon_tetra_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.NEON_TETRA, 0xC40C15, 0x70C0D6, new Item.Properties()));
	public static final RegistryObject<Item> PLECO_SPAWN_EGG = SPAWN_EGGS.register("pleco_spawn_egg", () -> new ForgeSpawnEggItem(IEntities.PLECO, 0x151515, 0x29292A, new Item.Properties()));

	public static final RegistryObject<Item> BLIND_CAVE_TETRA_BUCKET = REGISTER.register("blind_cave_tetra_bucket", () -> new IBucketItem(IEntities.BLIND_CAVE_TETRA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GOLDFISH_BUCKET = REGISTER.register("goldfish_bucket", () -> new IBucketItem(IEntities.GOLDFISH, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TILAPIA_BUCKET = REGISTER.register("tilapia_bucket", () -> new IBucketItem(IEntities.TILAPIA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PRINCESS_CICHLID_BUCKET = REGISTER.register("princess_cichlid_bucket", () -> new IBucketItem(IEntities.PRINCESS_CICHLID, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SAULOSI_CICHLID_BUCKET = REGISTER.register("saulosi_cichlid_bucket", () -> new IBucketItem(IEntities.SAULOSI_CICHLID, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> KASANGA_CICHLID_BUCKET = REGISTER.register("kasanga_cichlid_bucket", () -> new IBucketItem(IEntities.KASANGA_CICHLID, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CARP_BUCKET = REGISTER.register("carp_bucket", () -> new IBucketItem(IEntities.CARP, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PIRANHA_BUCKET = REGISTER.register("piranha_bucket", () -> new IBucketItem(IEntities.PIRANHA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PERCH_BUCKET = REGISTER.register("perch_bucket", () -> new IBucketItem(IEntities.PERCH, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> DISCUS_BUCKET = REGISTER.register("discus_bucket", () -> new IBucketItem(IEntities.DISCUS, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> ANGELFISH_BUCKET = REGISTER.register("angelfish_bucket", () -> new IBucketItem(IEntities.ANGELFISH, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> NEON_TETRA_BUCKET = REGISTER.register("neon_tetra_bucket", () -> new IBucketItem(IEntities.NEON_TETRA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PLECO_BUCKET = REGISTER.register("pleco_bucket", () -> new IBucketItem(IEntities.PLECO, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> BLIND_CAVE_TETRA = REGISTER.register("blind_cave_tetra", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> GOLDFISH = REGISTER.register("goldfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> TILAPIA = REGISTER.register("raw_tilapia", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_TILAPIA = REGISTER.register("cooked_tilapia", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.9F).meat().build())));
	public static final RegistryObject<Item> PRINCESS_CICHLID = REGISTER.register("princess_cichlid", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> SAULOSI_CICHLID = REGISTER.register("saulosi_cichlid", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> KASANGA_CICHLID = REGISTER.register("kasanga_cichlid", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> CARP = REGISTER.register("raw_carp", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_CARP = REGISTER.register("cooked_carp", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).meat().build())));
	public static final RegistryObject<Item> PIRANHA = REGISTER.register("raw_piranha", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	@SuppressWarnings("deprecation")
	public static final RegistryObject<Item> COOKED_PIRANHA = REGISTER.register("cooked_piranha", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.7F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100), 1).meat().build())));
	public static final RegistryObject<Item> PERCH = REGISTER.register("raw_perch", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_PERCH = REGISTER.register("cooked_perch", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).meat().build())));
	public static final RegistryObject<Item> DISCUS = REGISTER.register("discus", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> ANGELFISH = REGISTER.register("angelfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> NEON_TETRA = REGISTER.register("neon_tetra", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> PLECO = REGISTER.register("raw_pleco", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	@SuppressWarnings("deprecation")
	public static final RegistryObject<Item> COOKED_PLECO = REGISTER.register("cooked_pleco", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.7F).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100), 1).meat().build())));
	
	public static final RegistryObject<Item> SHINY_SCALE = REGISTER.register("shiny_scale", () -> new Item(new Item.Properties()));
	
}
