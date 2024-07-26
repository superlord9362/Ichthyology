package fuffles.ichthyology;

import java.util.ArrayList;
import java.util.List;

import fuffles.ichthyology.common.entity.*;
import fuffles.ichthyology.init.IBiomeModifiers;
import fuffles.ichthyology.init.IEntities;
import fuffles.ichthyology.init.IItems;
import fuffles.ichthyology.init.IPotions;
import fuffles.ichthyology.init.ITabs;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Ichthyology.MOD_ID)
public class Ichthyology {

	public static final String MOD_ID = "ichthyology";
    public static final List<Runnable> CALLBACKS = new ArrayList<>();
    
	public Ichthyology() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::commonSetup);
		bus.addListener(this::registerEntityAttributes);
        bus.addListener(this::registerClient);
        
		IEntities.REGISTER.register(bus);
		IItems.SPAWN_EGGS.register(bus);
		IItems.REGISTER.register(bus);
		ITabs.REGISTER.register(bus);
		IPotions.POTIONS.register(bus);
		IBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(bus);
	}
	
	@SuppressWarnings("deprecation")
	private void commonSetup(FMLCommonSetupEvent event) {
		SpawnPlacements.register(IEntities.BLIND_CAVE_TETRA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlindCaveTetra::checkBlindCaveTetraSpawnRules);
		SpawnPlacements.register(IEntities.GOLDFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.TILAPIA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.PRINCESS_CICHLID.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.SAULOSI_CICHLID.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.KASANGA_CICHLID.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.CARP.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.PIRANHA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.PERCH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.DISCUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.ANGELFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.NEON_TETRA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(IEntities.PLECO.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);

		IPotions.brewingRecipes();
	}
	
	private void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(IEntities.BLIND_CAVE_TETRA.get(), BlindCaveTetra.createAttributes().build());
		event.put(IEntities.GOLDFISH.get(), Goldfish.createAttributes().build());
		event.put(IEntities.TILAPIA.get(), Tilapia.createAttributes().build());
		event.put(IEntities.PRINCESS_CICHLID.get(), PrincessCichlid.createAttributes().build());
		event.put(IEntities.SAULOSI_CICHLID.get(), SaulosiCichlid.createAttributes().build());
		event.put(IEntities.KASANGA_CICHLID.get(), KasangaCichlid.createAttributes().build());
		event.put(IEntities.CARP.get(), Carp.createAttributes().build());
		event.put(IEntities.PIRANHA.get(), Piranha.createAttributes().build());
		event.put(IEntities.PERCH.get(), Perch.createAttributes().build());
		event.put(IEntities.DISCUS.get(), Discus.createAttributes().build());
		event.put(IEntities.ANGELFISH.get(), Angelfish.createAttributes().build());
		event.put(IEntities.NEON_TETRA.get(), NeonTetra.createAttributes().build());
		event.put(IEntities.PLECO.get(), NeonTetra.createAttributes().build());
	}
	
	private void registerClient(FMLClientSetupEvent event) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
    }
}
