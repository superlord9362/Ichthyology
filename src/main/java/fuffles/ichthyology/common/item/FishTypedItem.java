package fuffles.ichthyology.common.item;

import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FishTypedItem extends Item implements FishTyped
{
    private final EntityType<?> entityType;

    public FishTypedItem(EntityType<?> entityType, Item.Properties properties)
    {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced)
    {
        this.applyFishTypeHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public EntityType<?> getFishTypedEntityType()
    {
        return this.entityType;
    }
}
