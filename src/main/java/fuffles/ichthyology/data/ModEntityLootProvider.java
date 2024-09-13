package fuffles.ichthyology.data;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.AfricanCichlid;
import fuffles.ichthyology.common.world.level.storage.loot.functions.TransferTagValueFunction;
import fuffles.ichthyology.init.ModEntityTypes;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class ModEntityLootProvider extends EntityLootSubProvider
{
    public ModEntityLootProvider()
    {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    protected Stream<EntityType<?>> getKnownEntityTypes()
    {
        return super.getKnownEntityTypes().filter(entityType -> BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getNamespace().equals(Ichthyology.ID));
    }

    @Override
    public void generate()
    {
        this.add(ModEntityTypes.AFRICAN_CICHLID, LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.AFRICAN_CICHLID).apply(TransferTagValueFunction.transferred(new String[] { AfricanCichlid.TAG_VARIANT })).apply(TransferTagValueFunction.transferred(new String[] { AfricanCichlid.TAG_FEMALE })))
        ).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))
        ));
        this.add(ModEntityTypes.ANGELFISH, basicFish(ModItems.ANGELFISH));
        this.add(ModEntityTypes.BLIND_CAVE_TETRA, basicFish(ModItems.BLIND_CAVE_TETRA));
        this.add(ModEntityTypes.CARP, basicFish(ModItems.CARP, ModItems.COOKED_CARP));
        this.add(ModEntityTypes.DISCUS, basicFish(ModItems.DISCUS));
        this.add(ModEntityTypes.GOLDFISH, basicFish(ModItems.GOLDFISH));
        this.add(ModEntityTypes.NEON_TETRA, basicFish(ModItems.NEON_TETRA));
        this.add(ModEntityTypes.PERCH, basicFish(ModItems.PERCH, ModItems.COOKED_PERCH));
        this.add(ModEntityTypes.PIRANHA, basicFish(ModItems.PIRANHA, ModItems.COOKED_PIRANHA));
        this.add(ModEntityTypes.PLECO, basicFish(ModItems.PLECO, ModItems.COOKED_PLECO));
        this.add(ModEntityTypes.TILAPIA, basicFish(ModItems.TILAPIA, ModItems.COOKED_TILAPIA));
    }

    private static LootTable.Builder basicFish(Item drop)
    {
        return basicFish(drop, null);
    }

    private static LootTable.Builder basicFish(Item rawDrop, Item cookedDrop)
    {
        LootPoolSingletonContainer.Builder<?> dropBuilder = LootItem.lootTableItem(rawDrop);
        if (cookedDrop != null)
            dropBuilder.apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)));
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(dropBuilder)
            ).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))
            );
    }
}
