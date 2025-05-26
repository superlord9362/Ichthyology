package fuffles.ichthyology.common.entity.ai.sensing;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.item.ItemEntity;

//-F: Shallow copy of NearestItemSensor, but we pass the item entity rather than the item for wantsToPickUp
public class AdvancedNearestItemSensor extends Sensor<Mob>
{
    private static final float XZ_RANGE = 32F;
    private static final float Y_RANGE = 16F;
    private static final float MAX_DISTANCE_TO_WANTED_ITEM = 32F;

    public Set<MemoryModuleType<?>> requires()
    {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
    }

    protected void doTick(ServerLevel level, Mob entity)
    {
        Brain<?> brain = entity.getBrain();
        List<ItemEntity> potentialTargets = level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(XZ_RANGE, Y_RANGE, XZ_RANGE), itemEntity -> !(entity instanceof AdvancedNearestItemSensor.User user) || user.wantsToPickUp(itemEntity));
        potentialTargets.sort(Comparator.comparingDouble(entity::distanceToSqr));
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, potentialTargets.stream().filter(itemEntity -> entity.wantsToPickUp(itemEntity.getItem())).filter(itemEntity -> itemEntity.closerThan(entity, MAX_DISTANCE_TO_WANTED_ITEM)).filter(entity::hasLineOfSight).findFirst());
    }

    public static interface User
    {
        abstract boolean wantsToPickUp(ItemEntity itemEntity);
    }
}
