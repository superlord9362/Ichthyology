package fuffles.ichthyology.common.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class InHandSensor extends Sensor<Mob>
{
    @Override
    public Set<MemoryModuleType<?>> requires()
    {
        return ImmutableSet.of(ModMemoryModuleTypes.HAS_MAINHAND_ITEM, ModMemoryModuleTypes.HAS_OFFHAND_ITEM);
    }

    @Override
    protected void doTick(ServerLevel level, Mob entity)
    {
        Brain<?> brain = entity.getBrain();
        if (entity.hasItemInSlot(EquipmentSlot.MAINHAND))
            brain.setMemory(ModMemoryModuleTypes.HAS_MAINHAND_ITEM, true);
        if (entity.hasItemInSlot(EquipmentSlot.OFFHAND))
            brain.setMemory(ModMemoryModuleTypes.HAS_OFFHAND_ITEM, true);
    }
}
