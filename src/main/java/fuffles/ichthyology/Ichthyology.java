package fuffles.ichthyology;

import java.util.ArrayList;
import java.util.List;

import com.mojang.logging.LogUtils;
import fuffles.ichthyology.common.entity.*;
import fuffles.ichthyology.common.entity.perch.Perch;
import fuffles.ichthyology.common.item.FishTyped;
import fuffles.ichthyology.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(Ichthyology.ID)
public class Ichthyology {

	public static final String ID = "ichthyology";
	public static final Logger LOG = LogUtils.getLogger();
    public static final List<Runnable> CALLBACKS = new ArrayList<>();

	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(ID, path);
	}

	public Ichthyology() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::commonSetup);
		bus.addListener(this::registerEntityAttributes);
        bus.addListener(this::registerClient);

		//ModItems.SPAWN_EGG_REGISTRY.register(bus);
		//ModItems.ITEM_REGISTRY.register(bus);
		ModCreativeTabs.REGISTER.register(bus);
		ModPotions.POTIONS.register(bus);
		ModBiomeModifiers.REGISTRY.register(bus);
		bus.addListener((RegisterEvent event) -> RegistryRelay.registerAll(event));
	}
	
	@SuppressWarnings("deprecation")
	private void commonSetup(FMLCommonSetupEvent event) {
		SpawnPlacements.register(ModEntityTypes.BLIND_CAVE_TETRA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlindCaveTetra::checkBlindCaveTetraSpawnRules);
		SpawnPlacements.register(ModEntityTypes.GOLDFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.TILAPIA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.AFRICAN_CICHLID, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.CARP, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.PIRANHA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.PERCH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.DISCUS, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.ANGELFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.NEON_TETRA, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(ModEntityTypes.PLECO, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);

		ModPotions.brewingRecipes();
		ModEntityDataSerializers.register();
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
		event.put(ModEntityTypes.PLECO, NeonTetra.createAttributes().build());
	}
	
	private void registerClient(FMLClientSetupEvent event) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
		FishTyped.bootstrap();
	}
}
