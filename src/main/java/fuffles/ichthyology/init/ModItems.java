package fuffles.ichthyology.init;

import java.util.function.Supplier;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.item.CrabItem;
import fuffles.ichthyology.common.item.FishTypedItem;
import fuffles.ichthyology.common.item.ShinyScaleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class ModItems
{
	public static final RegistryRelay<Item> SPAWN_EGG_REGISTRY = new RegistryRelay<>(Registries.ITEM, Ichthyology::id);
	public static final RegistryRelay<Item> ITEM_REGISTRY = new RegistryRelay<>(Registries.ITEM, Ichthyology::id);

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

	private static BlockItem registerRoe(String id, Block block) {
		return ITEM_REGISTRY.register(id + "_roe", new BlockItem(block, new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.5F).build())));
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

	private static FoodProperties.Builder rawLargeFish()
	{
		return meat().nutrition(3).saturationMod(0.3F);
	}

	private static FoodProperties.Builder cookedLargeFish()
	{
		return meat().nutrition(6).saturationMod(0.7F);
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
	public static final ForgeSpawnEggItem ARCHERFISH_SPAWN_EGG = registerEgg("archerfish", ModEntityTypes.ARCHERFISH, 0xC9C8BE, 0x272314);
	public static final ForgeSpawnEggItem MUDSKIPPER_SPAWN_EGG = registerEgg("mudskipper", ModEntityTypes.MUDSKIPPER, 0x7E6C48, 0x61CDB0);
	public static final ForgeSpawnEggItem CRAYFISH_SPAWN_EGG = registerEgg("crayfish", ModEntityTypes.CRAYFISH, 0xCB5111, 0x480103);
	public static final ForgeSpawnEggItem CATFISH_SPAWN_EGG = registerEgg("catfish", ModEntityTypes.CATFISH, 0x4F5344, 0xC7BCB4);
	public static final ForgeSpawnEggItem CATFISH_BABY_SPAWN_EGG = registerEgg("catfish_baby", ModEntityTypes.CATFISH_BABY, 0x4F5344, 0x595C4B);
	public static final ForgeSpawnEggItem PEACOCK_BASS_SPAWN_EGG = registerEgg("peacock_bass", ModEntityTypes.PEACOCK_BASS, 0x837C1C, 0xD7AF00);
	public static final ForgeSpawnEggItem PEACOCK_BASS_BABY_SPAWN_EGG = registerEgg("peacock_bass_baby", ModEntityTypes.PEACOCK_BASS_BABY, 0xA29822, 0x5C9541);
	public static final ForgeSpawnEggItem GAR_SPAWN_EGG = registerEgg("gar", ModEntityTypes.GAR, 0x606A4E, 0xBCAF9A);
	public static final ForgeSpawnEggItem GAR_BABY_SPAWN_EGG = registerEgg("gar_baby", ModEntityTypes.GAR_BABY, 0x878124, 0xBCAF9A);
	public static final ForgeSpawnEggItem FIDDLER_CRAB_SPAWN_EGG = registerEgg("fiddler_crab", ModEntityTypes.FIDDLER_CRAB, 0x16A2C3, 0xAE4C27);
	public static final ForgeSpawnEggItem STURGEON_SPAWN_EGG = registerEgg("sturgeon", ModEntityTypes.STURGEON, 0x555045, 0x91897C);
	public static final ForgeSpawnEggItem STURGEON_BABY_SPAWN_EGG = registerEgg("sturgeon_baby", ModEntityTypes.STURGEON_BABY, 0x555045, 0xB4AEA7);
	public static final ForgeSpawnEggItem OLM_SPAWN_EGG = registerEgg("olm", ModEntityTypes.OLM, 0xE6DBC0, 0xB55053);

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
	public static final MobBucketItem ARCHERFISH_BUCKET = registerBucket("archerfish", ModEntityTypes.ARCHERFISH);
	public static final MobBucketItem MUDSKIPPER_BUCKET = registerBucket("mudskipper", ModEntityTypes.MUDSKIPPER);
	public static final MobBucketItem CRAYFISH_BUCKET = registerBucket("crayfish", ModEntityTypes.CRAYFISH);
	public static final MobBucketItem CATFISH_BABY_BUCKET = registerBucket("catfish_baby", ModEntityTypes.CATFISH_BABY);
	public static final MobBucketItem PEACOCK_BASS_BABY_BUCKET = registerBucket("peacock_bass_baby", ModEntityTypes.PEACOCK_BASS_BABY);
	public static final MobBucketItem GAR_BABY_BUCKET = registerBucket("gar_baby", ModEntityTypes.GAR_BABY);
	public static final MobBucketItem FIDDLER_CRAB_BUCKET = registerBucket("fiddler_crab", ModEntityTypes.FIDDLER_CRAB);
	public static final MobBucketItem STURGEON_BABY_BUCKET = registerBucket("sturgeon_baby", ModEntityTypes.STURGEON_BABY);
	public static final MobBucketItem OLM_BUCKET = registerBucket("olm", ModEntityTypes.OLM);

	public static final Item BLIND_CAVE_TETRA = ITEM_REGISTRY.register("blind_cave_tetra", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item GOLDFISH = ITEM_REGISTRY.register("goldfish", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item TILAPIA = ITEM_REGISTRY.register("tilapia", new Item(new Item.Properties().food(meat().nutrition(2).saturationMod(0.3F).build())));
	public static final Item COOKED_TILAPIA = ITEM_REGISTRY.register("cooked_tilapia", new Item(new Item.Properties().food(meat().nutrition(5).saturationMod(0.9F).build())));
	public static final Item AFRICAN_CICHLID = ITEM_REGISTRY.register("african_cichlid", new FishTypedItem(ModEntityTypes.AFRICAN_CICHLID, new Item.Properties().food(rawSmallFish().build())));
	public static final Item CARP = ITEM_REGISTRY.register("carp", new Item(new Item.Properties().food(rawMediumFish().build())));
	public static final Item COOKED_CARP = ITEM_REGISTRY.register("cooked_carp", new Item(new Item.Properties().food(cookedMediumFish().build())));
	public static final Item PIRANHA = ITEM_REGISTRY.register("piranha", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item COOKED_PIRANHA = ITEM_REGISTRY.register("cooked_piranha", new Item(new Item.Properties().food(cookedSmallFish().effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100), 1F).build())));
	public static final Item PERCH = ITEM_REGISTRY.register("perch", new FishTypedItem(ModEntityTypes.PERCH, new Item.Properties().food(rawMediumFish().build())));
	public static final Item COOKED_PERCH = ITEM_REGISTRY.register("cooked_perch", new Item(new Item.Properties().food(cookedMediumFish().build())));
	public static final Item DISCUS = ITEM_REGISTRY.register("discus", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item ANGELFISH = ITEM_REGISTRY.register("angelfish", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item NEON_TETRA = ITEM_REGISTRY.register("neon_tetra", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item PLECO = ITEM_REGISTRY.register("pleco", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item COOKED_PLECO = ITEM_REGISTRY.register("cooked_pleco", new Item(new Item.Properties().food(cookedSmallFish().effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100), 1F).build())));
	public static final Item ARCHERFISH = ITEM_REGISTRY.register("archerfish", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item MUDSKIPPER = ITEM_REGISTRY.register("mudskipper", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item CRAYFISH = ITEM_REGISTRY.register("crayfish", new FishTypedItem(ModEntityTypes.CRAYFISH, new Item.Properties().food(rawSmallFish().build())));
	public static final Item COOKED_CRAYFISH = ITEM_REGISTRY.register("cooked_crayfish", new Item(new Item.Properties().food(cookedSmallFish().build())));
	@SuppressWarnings("deprecation")
	public static final Item CATFISH = ITEM_REGISTRY.register("catfish", new Item(new Item.Properties().food(new FoodProperties.Builder().meat().nutrition(3).saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.POISON), 1).build())));
	public static final Item CATFISH_BABY = ITEM_REGISTRY.register("catfish_baby", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item COOKED_CATFISH_BABY = ITEM_REGISTRY.register("cooked_catfish_baby", new Item(new Item.Properties().food(cookedSmallFish().build())));
	public static final Item PEACOCK_BASS_BABY = ITEM_REGISTRY.register("peacock_bass_baby", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item PEACOCK_BASS_FILET = ITEM_REGISTRY.register("peacock_bass_fillet", new Item(new Item.Properties().food(rawLargeFish().build())));
	public static final Item COOKED_PEACOCK_BASS_FILET = ITEM_REGISTRY.register("cooked_peacock_bass_fillet", new Item(new Item.Properties().food(cookedLargeFish().build())));
	public static final Item GAR_BABY = ITEM_REGISTRY.register("gar_baby", new Item(new Item.Properties().food(rawSmallFish().build())));
	public static final Item GAR = ITEM_REGISTRY.register("gar", new Item(new Item.Properties().food(rawLargeFish().build())));
	public static final Item COOKED_GAR = ITEM_REGISTRY.register("cooked_gar", new Item(new Item.Properties().food(cookedLargeFish().build())));
	public static final Item STURGEON_BABY = ITEM_REGISTRY.register("sturgeon_baby", new Item(new Item.Properties().food(rawSmallFish().build())));
	@SuppressWarnings("deprecation")
	public static final Item GROUND_FISH = ITEM_REGISTRY.register("ground_fish_meat", new Item(new Item.Properties().food(new FoodProperties.Builder().meat().nutrition(1).saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.HUNGER, 200), 1).effect(new MobEffectInstance(MobEffects.CONFUSION), 1).build())));
	public static final Item FISH_STICKS = ITEM_REGISTRY.register("fish_sticks", new Item(new Item.Properties().food(new FoodProperties.Builder().meat().nutrition(5).saturationMod(0.6F).build())));
	
	public static final Item SHINY_SCALE = ITEM_REGISTRY.register("shiny_scale", new ShinyScaleItem(new Item.Properties()));

	public static final BlockItem CATFISH_ROE = registerRoe("catfish", ModBlocks.CATFISH_ROE);
	public static final BlockItem PEACOCK_BASS_ROE = registerRoe("peacock_bass", ModBlocks.PEACOCK_BASS_ROE);
	@SuppressWarnings("deprecation")
	public static final BlockItem GAR_ROE = ITEM_REGISTRY.register("gar_roe", new BlockItem(ModBlocks.GAR_ROE, new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.5F).effect(new MobEffectInstance(MobEffects.POISON, 200), 1).build())));
	public static final BlockItem STURGEON_ROE = registerRoe("sturgeon", ModBlocks.STURGEON_ROE);
	@SuppressWarnings("deprecation")
	public static final Item CAVIAR = ITEM_REGISTRY.register("caviar", new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).effect(new MobEffectInstance(MobEffects.REGENERATION, 200), 1).build())));
	public static final BlockItem OLM_EGGS = ITEM_REGISTRY.register("olm_eggs", new BlockItem(ModBlocks.OLM_EGGS, new Item.Properties()));
	public static final Item OLMLETTE = ITEM_REGISTRY.register("olmlette", new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build())));
	public static final CrabItem FIDDLER_CRAB = ITEM_REGISTRY.register("fiddler_crab", new CrabItem(new Item.Properties().stacksTo(1)));
	
	public static final class Tags
	{
		public static final TagKey<Item> CARP_FOOD = tag("carp_food");
		public static final TagKey<Item> PERCH_EDIBLES = tag("perch_edibles");
		public static final TagKey<Item> TUBE_CORAL_FRONDS = tag("tube_coral_fronds");
		public static final TagKey<Item> BRAIN_CORAL_FRONDS = tag("brain_coral_fronds");
		public static final TagKey<Item> BUBBLE_CORAL_FRONDS = tag("bubble_coral_fronds");
		public static final TagKey<Item> FIRE_CORAL_FRONDS = tag("fire_coral_fronds");
		public static final TagKey<Item> HORN_CORAL_FRONDS = tag("horn_coral_fronds");
		public static final TagKey<Item> SMALL_RAW_FISH = tag("small_raw_fish");
		public static final TagKey<Item> MEDIUM_RAW_FISH = tag("medium_raw_fish");
		public static final TagKey<Item> LARGE_RAW_FISH = tag("large_raw_fish");

		private static TagKey<Item> tag(String path)
		{
			return TagKey.create(Registries.ITEM, Ichthyology.id(path));
		}
	}
}