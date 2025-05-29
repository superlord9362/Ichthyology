package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.world.level.storage.loot.functions.TransferTagValueFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ModLootItemFunctions
{
    public static final RegistryRelay<LootItemFunctionType> REGISTRY = new RegistryRelay<>(Registries.LOOT_FUNCTION_TYPE, Ichthyology::id);

    public static final LootItemFunctionType TRANSFER_TAG_VALUE = REGISTRY.register("transfer_tag_value", new LootItemFunctionType(new TransferTagValueFunction.Serializer()));
}
