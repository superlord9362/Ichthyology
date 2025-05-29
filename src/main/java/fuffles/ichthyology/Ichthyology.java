package fuffles.ichthyology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fuffles.ichthyology.client.ClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import fuffles.ichthyology.common.entity.*;
import fuffles.ichthyology.common.entity.perch.Perch;
import fuffles.ichthyology.common.item.FishTyped;
import fuffles.ichthyology.data.*;
import fuffles.ichthyology.init.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Ichthyology.ID)
public class Ichthyology {

	public static final String ID = "ichthyology";
	public static final Logger LOG = LogUtils.getLogger();
    public static final List<Runnable> CALLBACKS = new ArrayList<>();

	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(ID, path);
	}

	public Ichthyology(IEventBus modBus)
	{
		this.registerEvents(modBus, MinecraftForge.EVENT_BUS);
	}

	private void registerEvents(IEventBus modBus, IEventBus forgeBus)
	{
		modBus.addListener(this::commonSetup);
		modBus.addListener(this::registerEntityAttributes);
		modBus.addListener(this::registerClient);

		ModCreativeTabs.REGISTER.register(modBus);
		ModPotions.POTIONS.register(modBus);
		ModBiomeModifiers.REGISTRY.register(modBus);
		modBus.addListener((RegisterEvent event) -> RegistryRelay.registerAll(event));

		modBus.addListener(this::onGatherData);
		modBus.addListener(this::onSpawnPlacementRegister);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		ModPotions.brewingRecipes();
		ModEntityDataSerializers.register();
	}

	private void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event)
	{
		SpawnPlacementRegisterEvent.Operation op = SpawnPlacementRegisterEvent.Operation.AND;
		event.register(ModEntityTypes.BLIND_CAVE_TETRA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlindCaveTetra::checkBlindCaveTetraSpawnRules, op);
		event.register(ModEntityTypes.GOLDFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.TILAPIA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.AFRICAN_CICHLID, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.CARP, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.PIRANHA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.PERCH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.DISCUS, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.ANGELFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.NEON_TETRA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.PLECO, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.ARCHERFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.MUDSKIPPER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mudskipper::checkMudskipperSpawnRules, op);
		event.register(ModEntityTypes.CRAYFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Crayfish::checkCrayfishSpawnRules, op);
		event.register(ModEntityTypes.CATFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Catfish::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.CATFISH_BABY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.PEACOCK_BASS, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PeacockBass::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.PEACOCK_BASS_BABY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.GAR, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Gar::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.GAR_BABY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.FIDDLER_CRAB, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, op);
		event.register(ModEntityTypes.STURGEON, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Sturgeon::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.STURGEON_BABY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, op);
		event.register(ModEntityTypes.OLM, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Olm::checkOlmSpawnRules, op);
	}

	private void onGatherData(GatherDataEvent event)
	{
		PackOutput out = event.getGenerator().getPackOutput();
		event.getGenerator().addProvider(event.includeServer(), new LootTableProvider(out, Collections.emptySet(), List.of(
				new LootTableProvider.SubProviderEntry(ModEntityLootProvider::new, LootContextParamSets.ENTITY)
		)));
		event.getGenerator().addProvider(event.includeServer(), new ModRecipeProvider(out));
		ModBlockTagsProvider blockTagsAccess = new ModBlockTagsProvider(out, event.getLookupProvider(), event.getExistingFileHelper());
		event.getGenerator().addProvider(event.includeServer(), blockTagsAccess);
		event.getGenerator().addProvider(event.includeServer(), new ModItemTagsProvider(out, event.getLookupProvider(), blockTagsAccess.contentsGetter(), event.getExistingFileHelper()));
		event.getGenerator().addProvider(event.includeServer(), new ModBiomeTagsProvider(out, event.getLookupProvider(), event.getExistingFileHelper()));
	}
	
	private void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntityTypes.BLIND_CAVE_TETRA, BlindCaveTetra.createAttributes().build());
		event.put(ModEntityTypes.GOLDFISH, Goldfish.createAttributes().build());
		event.put(ModEntityTypes.TILAPIA, Tilapia.createAttributes().build());
		event.put(ModEntityTypes.AFRICAN_CICHLID, AfricanCichlid.createAttributes().build());
		event.put(ModEntityTypes.CARP, Carp.createAttributes().build());
		event.put(ModEntityTypes.PIRANHA, Piranha.createAttributes().build());
		event.put(ModEntityTypes.PERCH, Perch.createAttributes().build());
		event.put(ModEntityTypes.DISCUS, Discus.createAttributes().build());
		event.put(ModEntityTypes.ANGELFISH, Angelfish.createAttributes().build());
		event.put(ModEntityTypes.NEON_TETRA, NeonTetra.createAttributes().build());
		event.put(ModEntityTypes.PLECO, Pleco.createAttributes().build());
		event.put(ModEntityTypes.ARCHERFISH, Archerfish.createAttributes().build());
		event.put(ModEntityTypes.MUDSKIPPER, Mudskipper.createAttributes().build());
		event.put(ModEntityTypes.CRAYFISH, Crayfish.createAttributes().build());
		event.put(ModEntityTypes.CATFISH, Catfish.createAttributes().build());
		event.put(ModEntityTypes.CATFISH_BABY, CatfishBaby.createAttributes().build());
		event.put(ModEntityTypes.PEACOCK_BASS, PeacockBass.createAttributes().build());
		event.put(ModEntityTypes.PEACOCK_BASS_BABY, PeacockBassBaby.createAttributes().build());
		event.put(ModEntityTypes.GAR, Gar.createAttributes().build());
		event.put(ModEntityTypes.GAR_BABY, GarBaby.createAttributes().build());
		event.put(ModEntityTypes.FIDDLER_CRAB, FiddlerCrab.createAttributes().build());
		event.put(ModEntityTypes.STURGEON, Sturgeon.createAttributes().build());
		event.put(ModEntityTypes.STURGEON_BABY, SturgeonBaby.createAttributes().build());
		event.put(ModEntityTypes.OLM, Olm.createAttributes().build());
	}
	
	private void registerClient(FMLClientSetupEvent event) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
		FishTyped.bootstrap();
	}
}
