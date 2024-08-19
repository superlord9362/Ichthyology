package fuffles.ichthyology.mixin.common.item;

import fuffles.ichthyology.common.item.FishTyped;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

@Mixin(MobBucketItem.class)
public class MobBucketItemMixin extends BucketItem implements FishTyped
{
    @Final
    @Shadow(remap = false)
    private Supplier<? extends EntityType<?>> entityTypeSupplier;

    @Deprecated
    private MobBucketItemMixin() { super((Fluid)null, null); }

    @Inject(method = "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("HEAD"), cancellable = true)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo cb)
    {
        if (this.applyFishTypeHoverText(stack, level, tooltipComponents, isAdvanced))
        {
            cb.cancel();
        }
    }

    @Override
    public EntityType<?> getFishTypedEntityType()
    {
        return this.entityTypeSupplier.get();
    }
}
