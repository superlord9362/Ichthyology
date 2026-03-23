package fuffles.ichthyology.mixin.common.advancement;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(SimpleCriterionTrigger.class)
public interface SimpleCriterionTriggerAccessor<T extends AbstractCriterionTriggerInstance>
{
    @Invoker(value = "trigger")
    public abstract void ichthyology$trigger(ServerPlayer player, Predicate<T> testTrigger);
}
