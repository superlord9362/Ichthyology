package fuffles.ichthyology.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ChumBucketItem extends Item {

	public ChumBucketItem(Properties pProperties) {
		super(pProperties);
	}

	@SuppressWarnings("deprecation")
	public InteractionResult useOn(UseOnContext pContext) {
		Level level = pContext.getLevel();
		Player player = pContext.getPlayer();
		BlockPos pos = pContext.getClickedPos().relative(pContext.getClickedFace());
		Random random = new Random();
		List<EntityType<?>> spawnableEntities = new ArrayList<>();
		if (level.getBlockState(pos).is(Blocks.WATER)) {
			int spawnAmount = random.nextInt(4);
			if (level.getBiome(pos).is(BiomeTags.IS_FOREST)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.FOREST_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.CHERRY_GROVE)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.CHERY_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.PLAINS)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.PLAINS_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(BiomeTags.IS_SAVANNA)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.SAVANNA_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.DESERT)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.DESERT_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(BiomeTags.IS_JUNGLE)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.JUNGLE_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.RIVER)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.RIVER_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.FROZEN_RIVER)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.FROZEN_RIVER_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.SWAMP)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.SWAMP_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.MANGROVE_SWAMP)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.MANGROVE_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.DRIPSTONE_CAVES)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.DRIPSTONE_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (level.getBiome(pos).is(Biomes.DEEP_COLD_OCEAN)) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.DEEP_COLD_OCEAN_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (pos.getY() <= level.getSeaLevel() - 33) spawnableEntities = BuiltInRegistries.ENTITY_TYPE.getTag(ModEntityTypes.Tags.CAVE_FISH).stream().flatMap(holders -> holders.stream()).map(Holder::value).collect(Collectors.toList());
			if (spawnableEntities.isEmpty()) return InteractionResult.FAIL;
			else {
				for (int i = 0; i < spawnAmount; i++) {
					if (level instanceof ServerLevel serverLevel) {
						EntityType<?> entityType = spawnableEntities.get(random.nextInt(spawnableEntities.size()));
						entityType.spawn(serverLevel, pos, MobSpawnType.NATURAL);	
					}
				}
				for(int i = 0; i < 10; ++i) {
					double d0 = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;
					level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0xAA574E).toVector3f(), 2.5F), pos.getX() + (2 * random.nextDouble() - 1), pos.getY() + random.nextDouble(), pos.getZ() + (2 * random.nextDouble() - 1), d0, d1, d2);
				}
				if (!player.isCreative())player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
				return InteractionResult.SUCCESS;
			}
		}
		return super.useOn(pContext);
	}

	public boolean useOnRelease(ItemStack pStack) {
		return true;
	}

}
