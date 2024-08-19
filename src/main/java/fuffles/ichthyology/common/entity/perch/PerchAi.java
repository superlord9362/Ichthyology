package fuffles.ichthyology.common.entity.perch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import fuffles.ichthyology.init.brain.ModActivities;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import fuffles.ichthyology.init.brain.ModSensorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.GameRules;

import java.util.Optional;

public class PerchAi
{
    private static final float PANIC_SPEED_MULT = 2.0F;
    private static final float ITEM_FETCHING_SPEED_MULT = 1.0F;
    private static final int ITEM_FETCH_DIST = 8;
    private static final float IDLE_SPEED_MULT = 1.0F;
    private static final float AVOID_PLAYER_SPEED_MULT = 1.5F;
    private static final int AVOID_PLAYER_DIST = 4;
    private static final int BITES_TO_CONSUME = 5;
    protected static final UniformInt TIME_BETWEEN_BITES = UniformInt.of(30, 50);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            ModMemoryModuleTypes.HAS_MAINHAND_ITEM, // InHandSensor
            ModMemoryModuleTypes.HAS_OFFHAND_ITEM, // InHandSensor
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, // AdvancedNearestItemSensor, GoToWantedItem
            MemoryModuleType.NEAREST_PLAYERS, // PlayerSensor
            MemoryModuleType.NEAREST_VISIBLE_PLAYER, // PlayerSensor
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, // PlayerSensor
            MemoryModuleType.IS_PANICKING, // AnimalPanic
            MemoryModuleType.HURT_BY, // AnimalPanic
            MemoryModuleType.LOOK_TARGET, // LookAtTargetSink, GoToWantedItem
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, // MoveToTargetSink
            MemoryModuleType.PATH, // MoveToTargetSink
            MemoryModuleType.WALK_TARGET, // MoveToTargetSink, GoToWantedItem
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, // GoToWantedItem
            ModMemoryModuleTypes.BITE_COOLDOWN_TICKS, // ConsumeItem
            ModMemoryModuleTypes.BITES_TAKEN, // ConsumeItem
            ModMemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG // PerchSpecificSensor
    );

    protected static final ImmutableList<SensorType<? extends Sensor<? super Perch>>> SENSOR_TYPES = ImmutableList.of(
            ModSensorTypes.HAS_INHANDITEM,
            ModSensorTypes.ADVANCED_NEAREST_ITEMS, // GoToWantedItem
            SensorType.NEAREST_PLAYERS, // SetWalkTargetAwayFrom
            ModSensorTypes.PERCH_SPECIFIC_SENSOR
            //SensorType.NEAREST_LIVING_ENTITIES
    );


    protected static void initMemories(Perch perch, RandomSource random)
    {
        //perch.getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, TIME_BETWEEN_LONG_JUMPS.sample(pRandom));
    }

    protected static Brain<?> makeBrain(Brain<Perch> brain)
    {
        initCoreActivitiy(brain);
        initIdleActivity(brain);
        //initFightingActivity(brain);
        initEatingActivity(brain);
        initBlockEatingActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivitiy(Brain<Perch> brain)
    {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new AnimalPanic(PANIC_SPEED_MULT),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS),
                new CountDownCooldownTicks(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS)
        ));
    }

    private static boolean hasNoFood(Perch perch)
    {
        return !perch.hasItemInSlot(EquipmentSlot.MAINHAND);
    }

    private static boolean canGriefAndHasNoFood(Perch perch)
    {
        return perch.level().getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && hasNoFood(perch);
    }

    private static void initIdleActivity(Brain<Perch> brain)
    {
        brain.addActivity(Activity.IDLE, 1, ImmutableList.of(
                GoToWantedItem.create(PerchAi::canGriefAndHasNoFood, ITEM_FETCHING_SPEED_MULT, true, ITEM_FETCH_DIST),
                //StartAttacking.create(Predicate.not(PerchAi::shouldBeEating), PerchAi::findNearestValidAttackTarget),
                createIdleBehaviors()
        ));
    }

    private static boolean canTakeBite(Perch perch)
    {
        return perch.getBrain().checkMemory(ModMemoryModuleTypes.BITE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT);
    }

    private static void initEatingActivity(Brain<Perch> brain)
    {
        brain.addActivityWithConditions(ModActivities.EAT, ImmutableList.of(
                Pair.of(0, EraseMemoryIf.create(PerchAi::hasNoFood, ModMemoryModuleTypes.HAS_MAINHAND_ITEM)),
                Pair.of(1, BehaviorBuilder.triggerIf(PerchAi::canTakeBite, EatOverTime.item(BITES_TO_CONSUME, TIME_BETWEEN_BITES, Perch::eatItem, SoundEvents.PLAYER_BURP))),
                Pair.of(2, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_VISIBLE_PLAYER, AVOID_PLAYER_SPEED_MULT, AVOID_PLAYER_DIST, true)),
                Pair.of(3, createIdleBehaviors())
            ),
            ImmutableSet.of(
                    Pair.of(ModMemoryModuleTypes.HAS_MAINHAND_ITEM, MemoryStatus.VALUE_PRESENT)
            )
        );
    }

    private static boolean canTakeBiteAndIsWithinReach(Perch perch)
    {
        Optional<BlockPos> egg = perch.getBrain().getMemory(ModMemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG);
        return egg.isPresent() && perch.position().distanceToSqr(egg.get().getX(), egg.get().getY(), egg.get().getZ()) < 1F && canTakeBite(perch);
    }

    private static void initBlockEatingActivity(Brain<Perch> brain)
    {
        brain.addActivityWithConditions(ModActivities.EAT_BLOCK, ImmutableList.of(
                Pair.of(0, EatOverTime.newBlock(ModMemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG, IDLE_SPEED_MULT, BITES_TO_CONSUME, TIME_BETWEEN_BITES, Perch::eatBlockState, SoundEvents.PLAYER_BURP))
                //Pair.of(0, new MoveToTargetSink()),
                    //Pair.of(0, BehaviorBuilder.triggerIf(PerchAi::canGriefAndHasNoFood, GoToTargetLocation.create(IBrainData.MemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG.get(), 0, IDLE_SPEED_MULT))),
                    //Pair.of(1, BehaviorBuilder.triggerIf(PerchAi::canTakeBiteAndIsWithinReach, EatOverTime.block(IBrainData.MemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG.get(), BITES_TO_CONSUME, TIME_BETWEEN_BITES, Perch::eatBlockState, SoundEvents.PLAYER_BURP)))
            ),
            ImmutableSet.of(
                    Pair.of(ModMemoryModuleTypes.HAS_MAINHAND_ITEM, MemoryStatus.VALUE_ABSENT),
                    Pair.of(ModMemoryModuleTypes.NEAREST_AMPHIBIOUS_EGG, MemoryStatus.VALUE_PRESENT),
                    Pair.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_ABSENT)
            )
        );
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Perch perch)
    {
        Brain<Perch> brain = perch.getBrain();
        if (!hasNoFood(perch))
        {
            return Optional.empty();
        }
        else
        {

        }
        /*if (isNearZombified(p_35001_))
        {
            return Optional.empty();
        }
        else
        {
            Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(p_35001_, MemoryModuleType.ANGRY_AT);
            if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(p_35001_, optional.get()))
            {
                return optional;
            }
            else
            {
                if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER))
                {
                    Optional<Player> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                    if (optional1.isPresent())
                    {
                        return optional1;
                    }
                }

                Optional<Mob> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
                if (optional3.isPresent())
                {
                    return optional3;
                }
                else
                {
                    Optional<Player> optional2 = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
                    return optional2.isPresent() && Sensor.isEntityAttackable(p_35001_, optional2.get()) ? optional2 : Optional.empty();
                }
            }
        }*/
        return Optional.empty();
    }

    private static void initFightingActivity(Brain<Perch> brain)
    {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 2, ImmutableList.of(
                MeleeAttack.create(20)

            ),
            MemoryModuleType.ATTACK_TARGET
        );
    }

    private static RunOne<Perch> createIdleBehaviors()
    {
        return new RunOne<>(ImmutableList.of(
            Pair.of(RandomStroll.swim(IDLE_SPEED_MULT), 2),
            Pair.of(new DoNothing(30, 60), 1)
        ));
    }

    public static void updateActivity(Perch perch)
    {
        perch.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActivities.EAT_BLOCK, ModActivities.EAT, Activity.IDLE));
    }
}
