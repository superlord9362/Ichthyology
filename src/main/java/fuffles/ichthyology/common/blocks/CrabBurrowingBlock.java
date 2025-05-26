package fuffles.ichthyology.common.blocks;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import fuffles.ichthyology.common.entity.FiddlerCrab;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CrabBurrowingBlock extends Block {
	private final Block hostBlock;
	private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK = Maps.newIdentityHashMap();
	private static final Map<BlockState, BlockState> HOST_TO_BURROWED_STATES = Maps.newIdentityHashMap();
	private static final Map<BlockState, BlockState> BURROWED_TO_HOST_STATES = Maps.newIdentityHashMap();

	public CrabBurrowingBlock(Block pHostBlock, BlockBehaviour.Properties pProperties) {
		super(pProperties.destroyTime(pHostBlock.defaultDestroyTime() / 2.0F).explosionResistance(0.75F));
		this.hostBlock = pHostBlock;
		BLOCK_BY_HOST_BLOCK.put(pHostBlock, this);
	}

	public Block getHostBlock() {
		return this.hostBlock;
	}

	public static boolean isCompatibleHostBlock(BlockState pState) {
		return BLOCK_BY_HOST_BLOCK.containsKey(pState.getBlock());
	}

	private void spawnCrab(ServerLevel pLevel, BlockPos pPos) {
		FiddlerCrab fiddlerCrab = ModEntityTypes.FIDDLER_CRAB.create(pLevel);
		if (fiddlerCrab != null) {
			fiddlerCrab.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
			pLevel.addFreshEntity(fiddlerCrab);
			fiddlerCrab.spawnAnim();
		}

	}

	@SuppressWarnings("deprecation")
	public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pStack, boolean pDropExperience) {
		super.spawnAfterBreak(pState, pLevel, pPos, pStack, pDropExperience);
		if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			this.spawnCrab(pLevel, pPos);
		}

	}

	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		this.spawnCrab(pLevel, new BlockPos(pPos.getX(), pPos.getY() + 1, pPos.getZ()));
		if (pLevel.getBlockState(pPos).is(ModBlocks.MUD_WITH_CRAB)) pLevel.setBlock(pPos, Blocks.MUD.defaultBlockState(), 2);
		else pLevel.setBlock(pPos, Blocks.SAND.defaultBlockState(), 2);
		//		pLevel.setBlock(pPos, BLOCK_BY_HOST_BLOCK.get(pLevel.getBlockState(pPos)).defaultBlockState(), 2);
	}

	public static BlockState infestedStateByHost(BlockState pHost) {
		return getNewStateWithProperties(HOST_TO_BURROWED_STATES, pHost, () -> {
			return BLOCK_BY_HOST_BLOCK.get(pHost.getBlock()).defaultBlockState();
		});
	}

	public BlockState hostStateByInfested(BlockState pInfested) {
		return getNewStateWithProperties(BURROWED_TO_HOST_STATES, pInfested, () -> {
			return this.getHostBlock().defaultBlockState();
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static BlockState getNewStateWithProperties(Map<BlockState, BlockState> pStateMap, BlockState pState, Supplier<BlockState> pSupplier) {
		return pStateMap.computeIfAbsent(pState, (p_153429_) -> {
			BlockState blockstate = pSupplier.get();

			for(Property property : p_153429_.getProperties()) {
				blockstate = blockstate.hasProperty(property) ? blockstate.setValue(property, p_153429_.getValue(property)) : blockstate;
			}

			return blockstate;
		});
	}
}
