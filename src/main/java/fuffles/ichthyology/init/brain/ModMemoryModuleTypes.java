package fuffles.ichthyology.init.brain;

import java.util.Optional;

import com.mojang.serialization.Codec;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.RegistryRelay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class ModMemoryModuleTypes
{
    public static final RegistryRelay<MemoryModuleType<?>> REGISTRY = new RegistryRelay<>(Registries.MEMORY_MODULE_TYPE, Ichthyology::id);

    public static final MemoryModuleType<Boolean> HAS_MAINHAND_ITEM = REGISTRY.register("has_mainhand_item", new MemoryModuleType<>(Optional.of(Codec.BOOL)));
    public static final MemoryModuleType<Boolean> HAS_OFFHAND_ITEM = REGISTRY.register("has_offhand_item", new MemoryModuleType<>(Optional.of(Codec.BOOL)));
    public static final MemoryModuleType<Integer> BITE_COOLDOWN_TICKS = REGISTRY.register("bite_cooldown_ticks", new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final MemoryModuleType<Integer> BITES_TAKEN = REGISTRY.register("bites_taken", new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final MemoryModuleType<BlockPos> NEAREST_AMPHIBIOUS_EGG = REGISTRY.register("nearest_amphibious_egg", new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));

}