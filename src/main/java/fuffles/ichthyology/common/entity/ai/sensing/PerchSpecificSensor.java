package fuffles.ichthyology.common.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import fuffles.ichthyology.common.entity.perch.Perch;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.GameRules;

import java.util.Optional;
import java.util.Set;

public class PerchSpecificSensor extends Sensor<Perch>
{
    private static final int SEARCH_XZ = 8;
    private static final int SEARCH_Y = 4;

    @Override
    public Set<MemoryModuleType<?>> requires()
    {
        return ImmutableSet.of(
                ModMemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG
        );
    }

    @Override
    protected void doTick(ServerLevel level, Perch perch)
    {
        Brain<?> brain = perch.getBrain();
        if (level.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
        {
            Optional<BlockPos> pos = this.findNearestEdible(level, perch);
            brain.setMemory(ModMemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG, pos);
            if (pos.isPresent())
            {
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos.get()));
            }
        }

    }

    private Optional<BlockPos> findNearestEdible(ServerLevel level, Perch perch)
    {
        Optional<BlockPos> pos = BlockPos.findClosestMatch(perch.blockPosition(), SEARCH_XZ, SEARCH_Y, blockPos -> level.getBlockState(blockPos).is(ModBlocks.Tags.AMPHIBIOUS_EGGS));
        /*if (pos.isPresent() && !level.getFluidState(pos.get()).is(FluidTags.WATER) && level.getFluidState(pos.get().below()).is(FluidTags.WATER))
        {
            pos = Optional.of(pos.get().below());
        }*/
        return pos;
    }
}
