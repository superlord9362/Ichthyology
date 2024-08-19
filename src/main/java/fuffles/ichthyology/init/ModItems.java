package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.item.FishTypedItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems
{
	@SuppressWarnings("deprecation")
	public static final RegistryRelay<Item> SPAWN_EGG_REGISTRY = new RegistryRelay<>(BuiltInRegistries.ITEM, Ichthyology::id);
	@SuppressWarnings("deprecation")
	public static final RegistryRelay<Item> ITEM_REGISTRY = new RegistryRelay<>(BuiltInRegistries.ITEM, Ichthyology::id);

	private static final Supplier<Fluid> DEFAULT_FLUID = () -> Fluids.WATER;
	private static final Supplier<SoundEvent> DEFAULT_SOUND = () -> SoundEvents.BUCKET_EMPTY_FISH;


	private static ForgeSpawnEggItem registerEgg(String id, EntityType<? extends Mob> toSpawn, int eggColor, int dotColor)
	{
		return SPAWN_EGG_REGISTRY.register(id + "_spawn_egg", new ForgeSpawnEggItem(() -> toSpawn, eggColor, dotColor, new Item.Properties()));
	}

	private static MobBucketItem registerBucket(String id, EntityType<? extends Mob> bucketMob)
	{
		return ITEM_REGISTRY.register(id + "_bucket", new MobBucketItem(() -> bucketMob, DEFAULT_FLUID, DEFAULT_SOUND, new Item.Properties().stacksTo(1)));
	}

	private static FoodProperties.Builder meat()
	{
		return new FoodProperties.Builder().meat();
	}

	private static FoodProperties.Builder rawSmallFish()
	{
		return meat().nutrition(1).saturationMod(0.3F);
	}

	private static FoodProperties.Builder cookedSmallFish()
	{
		return meat().nutrition(4).saturationMod(0.7F);
	}

	private static FoodProperties.Builder rawMediumFish()
	{
		return meat().nutrition(2).saturationMod(0.3F);
	}

	private static FoodProperties.Builder cookedMediumFish()
	{
		return meat().nutrition(5).saturationMod(0.7F);
	}

	public static final ForgeSpawnEggItem BLIND_CAVE_TETRA_SPAWN_EGG = registerEgg("blind_cave_tetra", ModEntityTypes.BLIND_CAVE_TETRA, 0xDFD0AE, 0xC8675E);
	public static final ForgeSpawnEggItem GOLDFISH_SPAWN_EGG = registerEgg("goldfish", ModEntityTypes.GOLDFISH, 0xCE4D29, 0xDD8433);
	public static final ForgeSpawnEggItem TILAPIA_SPAWN_EGG = registerEgg("tilapia", ModEntityTypes.TILAPIA, 0x6E7771, 0xC80322D);
	public static final ForgeSpawnEggItem AFRICAN_CICHLID_SPAWN_EGG = registerEgg("african_cichlid", ModEntityTypes.AFRICAN_CICHLID, 0xB19480, 0x75AAA0);
	public static final ForgeSpawnEggItem CARP_SPAWN_EGG = registerEgg("carp", ModEntityTypes.CARP, 0x4A3F39, 0xB1A796);
	public static final ForgeSpawnEggItem PIRANHA_SPAWN_EGG = registerEgg("piranha", ModEntityTypes.PIRANHA, 0x534E42, 0xA63211);
	public static final ForgeSpawnEggItem PERCH_SPAWN_EGG = registerEgg("perch", ModEntityTypes.PERCH, 0x87722D, 0x3C3D14);
	public static final ForgeSpawnEggItem DISCUS_SPAWN_EGG = registerEgg("discus", ModEntityTypes.DISCUS, 0xB65E16, 0x4DABD9);
	public static final ForgeSpawnEggItem ANGELFISH_SPAWN_EGG = registerEgg("angelfish", ModEntityTypes.ANGELFISH, 0x766124, 0x1B1B1B);
	public static final ForgeSpawnEggItem NEON_TETRA_SPAWN_EGG = registerEgg("neon_tetra", ModEntityTypes.NEON_TETRA, 0xC40C15, 0x70C0D6);
	public static final ForgeSpawnEggItem PLECO_SPAWN_EGG = registerEgg("pleco", ModEntityTypes.PLECO, 0x151515, 0x29292A);

	public static final MobBucketItem BLIND_CAVE_TETRA_BUCKET = registerBucket("blind_cave_tetra", ModEntityTypes.BLIND_CAVE_TETRA);
	public static final MobBucketItem GOLDFISH_BUCKET = registerBucket("goldfish", ModEntityTypes.GOLDFISH);
	public static final MobBucketItem TILAPIA_BUCKET = registerBucket("tilapia", ModEntityTypes.TILAPIA);
	public static final MobBucketItem AFRICAN_CICHLID_BUCKET = registerBucket("african_cichlid", ModEntityTypes.AFRICAN_CICHLID);
	public static final MobBucketItem CARP_BUCKET = registerBucket("carp", ModEntityTypes.CARP);
	public static final MobBucketItem PIRANHA_BUCKET = registerBucket("piranha", ModEntityTypes.PIRANHA);
	public static final MobBucketItem PERCH_BUCKET = registerBucket("perch", ModEntityTypes.PERCH);
	public static final MobBucketItem DISCUS_BUCKET = registerBucket("discus", ModEntityTypes.DISCUS);
	public static final MobBucketItem ANGELFISH_BUCKET = registerBucket("angelfish", ModEntityTypes.ANGELFISH);
	public static final MobBucketItem NEON_TETRA_BUCKET = registerBucket("neon_tetra", ModEntityTypes.NEON_TETRA);
	public static final MobBucketItem PLECO_BUCKET = registerBucket("pleco", ModEntityTypes.PLECO);

	public static final Item BLIND_CAVE_TETRA = ITEM_REGISTRY.register("blind_cave_tetra", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item GOLDFISH = ITEM_REGISTRY.register("goldfish", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item TILAPIA = ITEM_REGISTRY.register("tilapia", new Item(new Item.Properties().food(meat().nutrition(2).saturationMod(0.3F).build())));
	public static final Item COOKED_TILAPIA = ITEM_REGISTRY.register("cooked_tilapia", new Item(new Item.Properties().food(meat().nutrition(5).saturationMod(0.9F).build())));
	public static final Item AFRICAN_CICHLID = ITEM_REGISTRY.register("african_cichlid", new FishTypedItem(ModEntityTypes.AFRICAN_CICHLID, new Item.Properties().food(rawSmallFish().build())));
	public static final Item CARP = ITEM_REGISTRY.register("carp", new Item(new Item.Properties().food(rawMediumFish().build())));
	public static final Item COOKED_CARP = ITEM_REGISTRY.register("cooked_carp", new Item(new Item.Properties().food(cookedMediumFish().build())));
	public static final Item PIRANHA = ITEM_REGISTRY.register("piranha", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item COOKED_PIRANHA = ITEM_REGISTRY.register("cooked_piranha", new Item(new Item.Properties().food(cookedSmallFish().effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100), 1F).build())));
	public static final Item PERCH = ITEM_REGISTRY.register("perch", new Item(new Item.Properties().food(rawMediumFish().build())));
	public static final Item COOKED_PERCH = ITEM_REGISTRY.register("cooked_perch", new Item(new Item.Properties().food(cookedMediumFish().build())));
	public static final Item DISCUS = ITEM_REGISTRY.register("discus", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item ANGELFISH = ITEM_REGISTRY.register("angelfish", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item NEON_TETRA = ITEM_REGISTRY.register("neon_tetra", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item PLECO = ITEM_REGISTRY.register("pleco", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item COOKED_PLECO = ITEM_REGISTRY.register("cooked_pleco", new Item(new Item.Properties().food(cookedSmallFish().effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100), 1F).build())));

	public static final Item SHINY_SCALE = ITEM_REGISTRY.register("shiny_scale", new Item(new Item.Properties()));

	/*
	public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, Ichthyology.ID);
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Ichthyology.ID);

	public static final RegistryObject<Item> BLIND_CAVE_TETRA_SPAWN_EGG = SPAWN_EGGS.register("blind_cave_tetra_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.BLIND_CAVE_TETRA, 0xDFD0AE, 0xC8675E, new Item.Properties()));
	public static final RegistryObject<Item> GOLDFISH_SPAWN_EGG = SPAWN_EGGS.register("goldfish_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.GOLDFISH, 0xCE4D29, 0xDD8433, new Item.Properties()));
	public static final RegistryObject<Item> TILAPIA_SPAWN_EGG = SPAWN_EGGS.register("tilapia_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.TILAPIA, 0x6E7771, 0xC80322D, new Item.Properties()));
	public static final RegistryObject<Item> AFRICAN_CICHLID_SPAWN_EGG = SPAWN_EGGS.register("african_cichlid_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.AFRICAN_CICHLID, 0xB19480, 0x75AAA0, new Item.Properties()));
	public static final RegistryObject<Item> CARP_SPAWN_EGG = SPAWN_EGGS.register("carp_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.CARP, 0x4A3F39, 0xB1A796, new Item.Properties()));
	public static final RegistryObject<Item> PIRANHA_SPAWN_EGG = SPAWN_EGGS.register("piranha_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.PIRANHA, 0x534E42, 0xA63211, new Item.Properties()));
	public static final RegistryObject<Item> PERCH_SPAWN_EGG = SPAWN_EGGS.register("perch_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.PERCH, 0x87722D, 0x3C3D14, new Item.Properties()));
	public static final RegistryObject<Item> DISCUS_SPAWN_EGG = SPAWN_EGGS.register("discus_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.DISCUS, 0xB65E16, 0x4DABD9, new Item.Properties()));
	public static final RegistryObject<Item> ANGELFISH_SPAWN_EGG = SPAWN_EGGS.register("angelfish_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.ANGELFISH, 0x766124, 0x1B1B1B, new Item.Properties()));
	public static final RegistryObject<Item> NEON_TETRA_SPAWN_EGG = SPAWN_EGGS.register("neon_tetra_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.NEON_TETRA, 0xC40C15, 0x70C0D6, new Item.Properties()));
	public static final RegistryObject<Item> PLECO_SPAWN_EGG = SPAWN_EGGS.register("pleco_spawn_egg", () -> new ForgeSpawnEggItem(() -> ModEntityTypes.PLECO, 0x151515, 0x29292A, new Item.Properties()));

	public static final RegistryObject<Item> BLIND_CAVE_TETRA_BUCKET = REGISTER.register("blind_cave_tetra_bucket", () -> new IBucketItem(() -> ModEntityTypes.BLIND_CAVE_TETRA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GOLDFISH_BUCKET = REGISTER.register("goldfish_bucket", () -> new IBucketItem(() -> ModEntityTypes.GOLDFISH, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TILAPIA_BUCKET = REGISTER.register("tilapia_bucket", () -> new IBucketItem(() -> ModEntityTypes.TILAPIA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> AFRICAN_CICHLID_BUCKET = REGISTER.register("african_cichlid_bucket", () -> new MobBucketItem(() -> ModEntityTypes.AFRICAN_CICHLID, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CARP_BUCKET = REGISTER.register("carp_bucket", () -> new IBucketItem(() -> ModEntityTypes.CARP, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PIRANHA_BUCKET = REGISTER.register("piranha_bucket", () -> new IBucketItem(() -> ModEntityTypes.PIRANHA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PERCH_BUCKET = REGISTER.register("perch_bucket", () -> new IBucketItem(() -> ModEntityTypes.PERCH, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> DISCUS_BUCKET = REGISTER.register("discus_bucket", () -> new IBucketItem(() -> ModEntityTypes.DISCUS, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> ANGELFISH_BUCKET = REGISTER.register("angelfish_bucket", () -> new IBucketItem(() -> ModEntityTypes.ANGELFISH, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> NEON_TETRA_BUCKET = REGISTER.register("neon_tetra_bucket", () -> new IBucketItem(() -> ModEntityTypes.NEON_TETRA, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PLECO_BUCKET = REGISTER.register("pleco_bucket", () -> new IBucketItem(() -> ModEntityTypes.PLECO, () -> Fluids.WATER, new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> BLIND_CAVE_TETRA = REGISTER.register("blind_cave_tetra", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> GOLDFISH = REGISTER.register("goldfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> TILAPIA = REGISTER.register("tilapia", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_TILAPIA = REGISTER.register("cooked_tilapia", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.9F).meat().build())));
	public static final RegistryObject<Item> AFRICAN_CICHLID = REGISTER.register("african_cichlid", () -> new FishTypedItem(ModEntityTypes.AFRICAN_CICHLID, new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> CARP = REGISTER.register("carp", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_CARP = REGISTER.register("cooked_carp", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).meat().build())));
	public static final RegistryObject<Item> PIRANHA = REGISTER.register("piranha", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	@SuppressWarnings("deprecation")
	public static final RegistryObject<Item> COOKED_PIRANHA = REGISTER.register("cooked_piranha", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.7F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100), 1).meat().build())));
	public static final RegistryObject<Item> PERCH = REGISTER.register("perch", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_PERCH = REGISTER.register("cooked_perch", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).meat().build())));
	public static final RegistryObject<Item> DISCUS = REGISTER.register("discus", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> ANGELFISH = REGISTER.register("angelfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> NEON_TETRA = REGISTER.register("neon_tetra", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> PLECO = REGISTER.register("pleco", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).meat().build())));
	@SuppressWarnings("deprecation")
	public static final RegistryObject<Item> COOKED_PLECO = REGISTER.register("cooked_pleco", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.7F).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100), 1).meat().build())));

	public static final RegistryObject<Item> SHINY_SCALE = REGISTER.register("shiny_scale", () -> new Item(new Item.Properties()));
	*/

	public static final class Tags
	{
		public static final TagKey<Item> CARP_FOOD = tag("carp_food");
		public static final TagKey<Item> PERCH_EDIBLES = tag("perch_edibles");

		private static TagKey<Item> tag(String path)
		{
			return TagKey.create(Registries.ITEM, Ichthyology.id(path));
		}
	}
}
