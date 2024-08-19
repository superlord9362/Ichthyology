package fuffles.ichthyology.common.item;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.AfricanCichlid;
import fuffles.ichthyology.common.entity.Carp;
import fuffles.ichthyology.common.entity.Goldfish;
import fuffles.ichthyology.init.ModEntityTypes;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

public interface FishTyped
{
    public static final Object2ObjectMap<EntityType<?>, Pair<ToIntFunction<CompoundTag>, FishTyped.Descriptor>> DESCRIPTOR_MAP = new Object2ObjectOpenHashMap<>();
    public static final Style DEFAULT_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);

    abstract EntityType<?> getFishTypedEntityType();

    default boolean applyFishTypeHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced)
    {
        if (FishTyped.DESCRIPTOR_MAP.containsKey(this.getFishTypedEntityType()))
        {
            if (!FishTyped.DESCRIPTOR_MAP.get(this.getFishTypedEntityType()).right().applyDescription(stack, level, tooltipComponents, isAdvanced, DEFAULT_STYLE))
                tooltipComponents.add(Component.translatable("entity.ichthyology.generic.variant").withStyle(DEFAULT_STYLE));
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void addMobDescriptor(EntityType<?> type, ToIntFunction<CompoundTag> toFishType, FishTyped.Descriptor descriptor)
    {
        FishTyped.DESCRIPTOR_MAP.computeIfAbsent(type, var -> Pair.of(toFishType, descriptor));
    }

    public static void bootstrap()
    {
        ItemProperties.registerGeneric(Ichthyology.id("fish_type"), (stack, level, entity, seed) -> {
            if (stack.getItem() instanceof FishTyped fishTyped && stack.hasTag())
            {
                return FishTyped.DESCRIPTOR_MAP.get(fishTyped.getFishTypedEntityType()).left().applyAsInt(stack.getTag());
            }
            return 0;
        });
        addMobDescriptor(EntityType.AXOLOTL, tag -> tag.contains(Axolotl.VARIANT_TAG, Tag.TAG_INT) ? tag.getInt(Axolotl.VARIANT_TAG) : 0, (stack, level, tooltipComponents, isAdvanced, defaultStyle) -> {
            Axolotl.Variant variant = (stack.hasTag() && stack.getTag().contains(Axolotl.VARIANT_TAG, Tag.TAG_INT)) ? Axolotl.Variant.byId(stack.getTag().getInt(Axolotl.VARIANT_TAG)) : Axolotl.Variant.LUCY;
            tooltipComponents.add(Component.translatable("entity.minecraft.axolotl.variant." + variant.getName()).withStyle(defaultStyle));
            return true;
        });
        addMobDescriptor(ModEntityTypes.AFRICAN_CICHLID, AfricanCichlid::createTagToFishType, AfricanCichlid::createBucketDescriptor);
        addMobDescriptor(ModEntityTypes.CARP, Carp::createTagToFishType, Carp::createBucketDescriptor);
        addMobDescriptor(ModEntityTypes.GOLDFISH, Goldfish::createTagToFishType, Goldfish::createBucketDescriptor);

    }

    @FunctionalInterface
    public static interface Descriptor
    {
        abstract boolean applyDescription(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle);
    }
}
