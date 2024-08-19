package fuffles.ichthyology.common.entity.perch;

import com.google.common.collect.ImmutableMap;
import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public final class EatOverTime extends Behavior<Mob>
{
    public static <T extends LivingEntity> OneShot<T> item(int consumeIn, UniformInt getTimeBetweenBites, BiConsumer<T, ItemStack> onConsume, SoundEvent sound)
    {
        return item(consumeIn, getTimeBetweenBites, onConsume, sound, sound);
    }

    public static <T extends LivingEntity> OneShot<T> item(int consumeIn, UniformInt getTimeBetweenBites, BiConsumer<T, ItemStack> onConsume, SoundEvent eatSound, SoundEvent consumeSound)
    {
        return BehaviorBuilder.create(instance -> instance.group(instance.registered(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS), instance.registered(ModMemoryModuleTypes.BITES_TAKEN), instance.registered(ModMemoryModuleTypes.HAS_MAINHAND_ITEM), instance.registered(ModMemoryModuleTypes.HAS_OFFHAND_ITEM)).apply(instance, (biteCooldownTicksAccessor, bitesTakenAccessor, hasMainhandItemAccessor, hasOffhandItemAccessor) -> (level, owner, gameTime) -> {
            ItemStack mainStack = owner.getItemBySlot(EquipmentSlot.MAINHAND);
            ItemStack offStack = owner.getItemBySlot(EquipmentSlot.OFFHAND);
            if (mainStack.isEmpty() && offStack.isEmpty())
            {
                return false;
            }
            int bitesTaken = instance.tryGet(bitesTakenAccessor).orElse(0);
            owner.level().broadcastEntityEvent(owner, EntityEvent.FOX_EAT);
            if (bitesTaken >= consumeIn)
            {
                owner.playSound(consumeSound);
                if (!mainStack.isEmpty())
                {
                    onConsume.accept(owner, mainStack);
                    hasMainhandItemAccessor.erase();
                }
                else
                {
                    onConsume.accept(owner, offStack);
                    hasOffhandItemAccessor.erase();
                }
                bitesTakenAccessor.erase();
            }
            else
            {
                owner.playSound(eatSound);
                bitesTakenAccessor.set(bitesTaken + 1);
            }
            biteCooldownTicksAccessor.set(getTimeBetweenBites.sample(level.random));
            return true;
        }));
    }

    public static <T extends LivingEntity> OneShot<T> block(MemoryModuleType<BlockPos> eatPos, int consumeIn, UniformInt getTimeBetweenBites, BiConsumer<T, BlockState> onConsume, SoundEvent sound)
    {
        return block(eatPos, consumeIn, getTimeBetweenBites, onConsume, sound, sound);
    }

    public static <T extends LivingEntity> OneShot<T> block(MemoryModuleType<BlockPos> eatPos, int consumeIn, UniformInt getTimeBetweenBites, BiConsumer<T, BlockState> onConsume, SoundEvent eatSound, SoundEvent consumeSound)
    {
        return BehaviorBuilder.create(instance -> instance.group(instance.registered(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS), instance.registered(ModMemoryModuleTypes.BITES_TAKEN), instance.present(eatPos)).apply(instance, (biteCooldownTicksAccessor, bitesTakenAccessor, eatPosAccessor) -> (level, owner, gameTime) -> {
            BlockPos pos = instance.get(eatPosAccessor);
            BlockState state = level.getBlockState(pos);
            if (state.isAir()/* && owner.position().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 1F*/)
            {
                return false;
            }
            int bitesTaken = instance.tryGet(bitesTakenAccessor).orElse(0);
            owner.level().broadcastEntityEvent(owner, EntityEvent.EAT_GRASS); //add
            if (bitesTaken >= consumeIn)
            {
                owner.playSound(consumeSound);
                onConsume.accept(owner, state);
                level.destroyBlock(instance.get(eatPosAccessor), false, owner);
                bitesTakenAccessor.erase();
                eatPosAccessor.erase();
            }
            else
            {
                owner.playSound(eatSound);
                bitesTakenAccessor.set(bitesTaken + 1);
                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
            }
            biteCooldownTicksAccessor.set(getTimeBetweenBites.sample(level.random));
            return true;
        }));
    }

    public static <T extends Mob> EatOverTime newBlock(MemoryModuleType<BlockPos> eatPos, float speed, int consumeIn, UniformInt getTimeBetweenBites, BiConsumer<T, BlockState> onConsume, SoundEvent sound)
    {
        return newBlock(eatPos, speed, consumeIn, getTimeBetweenBites, onConsume, sound, sound);
    }

    public static <T extends Mob> EatOverTime newBlock(MemoryModuleType<BlockPos> eatPos, float speed, int consumeIn, UniformInt getTimeBetweenBites, BiConsumer<T, BlockState> onConsume, SoundEvent eatSound, SoundEvent consumeSound)
    {
        return new EatOverTime(eatPos, speed, consumeIn, getTimeBetweenBites, eatSound, consumeSound);
    }

    private final MemoryModuleType<BlockPos> eatPos;
    private final int consumeIn;
    private final UniformInt getTimeBetweenBites;

    private final SoundEvent eatSound;
    private final SoundEvent consumeSound;
    private final float speed;

    private EatOverTime(MemoryModuleType<BlockPos> eatPos, float speed, int consumeIn, UniformInt getTimeBetweenBites, SoundEvent eatSound, SoundEvent consumeSound)
    {
        super(ImmutableMap.of(
                ModMemoryModuleTypes.BITE_COOLDOWN_TICKS, MemoryStatus.REGISTERED,
                ModMemoryModuleTypes.BITES_TAKEN, MemoryStatus.REGISTERED,
                eatPos, MemoryStatus.VALUE_PRESENT)
        );
        this.eatPos = eatPos;
        this.consumeIn = consumeIn;
        this.getTimeBetweenBites = getTimeBetweenBites;
        this.eatSound = eatSound;
        this.consumeSound = consumeSound;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, Mob owner)
    {
        return owner.getBrain().hasMemoryValue(this.eatPos);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, Mob owner, long gameTime)
    {
        return owner.getBrain().hasMemoryValue(this.eatPos);
    }

    @Override
    protected boolean timedOut(long pGameTime)
    {
        return false;
    }

    @Override
    protected void start(@NotNull ServerLevel level, Mob owner, long gameTime)
    {
        Brain<?> brain = owner.getBrain();
        BehaviorUtils.setWalkAndLookTargetMemories(owner, brain.getMemory(this.eatPos).get(), this.speed, 1);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull Mob owner, long gameTime)
    {
        Brain<?> brain = owner.getBrain();
        BlockPos pos = brain.getMemory(this.eatPos).get();
        if (level.getBlockState(pos).isAir())
        {
            brain.eraseMemory(this.eatPos);
            return;
        }
        if (owner.position().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= 1F && !brain.hasMemoryValue(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS))
        {
            int bitesTaken = brain.getMemory(ModMemoryModuleTypes.BITES_TAKEN).orElse(0);
            if (bitesTaken >= this.consumeIn)
            {
                owner.playSound(this.consumeSound);
                //onConsume.accept(owner, state);
                level.destroyBlock(pos, false, owner);
                owner.getBrain().eraseMemory(ModMemoryModuleTypes.BITES_TAKEN);
                owner.getBrain().eraseMemory(this.eatPos);
            }
            else
            {
                owner.playSound(eatSound);
                brain.setMemory(ModMemoryModuleTypes.BITES_TAKEN, bitesTaken + 1);
                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(level.getBlockState(pos)));
            }
            brain.setMemory(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS, this.getTimeBetweenBites.sample(level.random));
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel pLevel, @NotNull Mob owner, long pGameTime)
    {
        Ichthyology.LOG.info("stopping EatOverTime");
        owner.getBrain().eraseMemory(ModMemoryModuleTypes.BITES_TAKEN);
    }
}
