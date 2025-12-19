package fuffles.ichthyology.data;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.AfricanCichlid;
import fuffles.ichthyology.common.entity.Crayfish;
import fuffles.ichthyology.common.entity.perch.Perch;
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
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
	public void generate() {
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
		this.add(ModEntityTypes.PERCH, LootTable.lootTable().withPool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.PERCH).apply(TransferTagValueFunction.transferred(new String[] { Perch.TAG_VARIANT })).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
				).withPool(
						LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))
						));
		this.add(ModEntityTypes.PIRANHA, basicFish(ModItems.PIRANHA, ModItems.COOKED_PIRANHA));
		this.add(ModEntityTypes.PLECO, basicFish(ModItems.PLECO, ModItems.COOKED_PLECO));
		this.add(ModEntityTypes.TILAPIA, basicFish(ModItems.TILAPIA, ModItems.COOKED_TILAPIA));
		this.add(ModEntityTypes.ARCHERFISH, basicFish(ModItems.ARCHERFISH));
		this.add(ModEntityTypes.MUDSKIPPER, basicFish(ModItems.MUDSKIPPER));
		this.add(ModEntityTypes.CRAYFISH, LootTable.lootTable().withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.CRAYFISH).apply(TransferTagValueFunction.transferred(new String[] { Crayfish.TAG_VARIANT })).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
		).withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))
		));
		this.add(ModEntityTypes.CATFISH, basicFish(ModItems.CATFISH));
		this.add(ModEntityTypes.CATFISH_BABY, basicFish(ModItems.CATFISH_BABY, ModItems.COOKED_CATFISH_BABY));
		this.add(ModEntityTypes.PEACOCK_BASS, LootTable.lootTable().withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.PEACOCK_BASS_FILET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
		).withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))
		));
		this.add(ModEntityTypes.PEACOCK_BASS_BABY, basicFish(ModItems.PEACOCK_BASS_BABY));
		this.add(ModEntityTypes.GAR, LootTable.lootTable().withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.GAR).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
		).withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))
		));
		this.add(ModEntityTypes.GAR_BABY, basicFish(ModItems.GAR_BABY));
		this.add(ModEntityTypes.FIDDLER_CRAB, basicFish(Items.AIR));
		this.add(ModEntityTypes.STURGEON, basicFish(ModItems.STURGEON_ROE));
		this.add(ModEntityTypes.STURGEON_BABY, basicFish(ModItems.STURGEON_BABY));
		this.add(ModEntityTypes.OLM, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.AIR))));
		this.add(ModEntityTypes.FLOWERHORN, basicFish(ModItems.FLOWERHORN));
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
