package fuffles.ichthyology.common.blocks;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;

import fuffles.ichthyology.common.entity.Olm;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FishRoeBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);
	private static int minHatchTickDelay = 3600;
	private static int maxHatchTickDelay = 12000;
	private final java.util.function.Supplier<? extends EntityType<?>> entityTypeSupplier;

	public FishRoeBlock(java.util.function.Supplier<? extends EntityType<?>> entitySupplier, BlockBehaviour.Properties pProperties) {
		super(pProperties);
		this.entityTypeSupplier = entitySupplier;
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(true)));
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	protected boolean mayPlaceOn(BlockState p_154539_, BlockGetter p_154540_, BlockPos p_154541_) {
		return p_154539_.isFaceSturdy(p_154540_, p_154541_, Direction.UP) && p_154539_.getBlock() != this && p_154540_.getBlockState(p_154541_.above()).getBlock() == Blocks.WATER && !p_154539_.is(Blocks.MAGMA_BLOCK);
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		BlockPos blockpos = pPos.below();
		return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, pPos, Direction.UP) && (pLevel.getBlockState(pPos).is(Blocks.WATER) || pLevel.getBlockState(pPos).is(this));
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext p_154503_) {
		FluidState fluidstate = p_154503_.getLevel().getFluidState(p_154503_.getClickedPos());
		boolean flag = fluidstate.getType() == Fluids.WATER;
		return super.getStateForPlacement(p_154503_).setValue(WATERLOGGED, Boolean.valueOf(flag));
	}

	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState p_154530_, Direction p_154531_, BlockState p_154532_, LevelAccessor p_154533_, BlockPos p_154534_, BlockPos p_154535_) {
		if (!p_154530_.canSurvive(p_154533_, p_154534_)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if (p_154530_.getValue(WATERLOGGED)) {
				p_154533_.scheduleTick(p_154534_, Fluids.WATER, Fluids.WATER.getTickDelay(p_154533_));
			}

			return super.updateShape(p_154530_, p_154531_, p_154532_, p_154533_, p_154534_, p_154535_);
		}
	}

	public boolean canPlaceLiquid(BlockGetter p_154505_, BlockPos p_154506_, BlockState p_154507_, Fluid p_154508_) {
		return false;
	}

	public boolean placeLiquid(LevelAccessor p_154520_, BlockPos p_154521_, BlockState p_154522_, FluidState p_154523_) {
		return false;
	}

	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
		pLevel.scheduleTick(pPos, this, getFishHatchDelay(pLevel.getRandom()));
	}

	private static int getFishHatchDelay(RandomSource pRandom) {
		return pRandom.nextInt(minHatchTickDelay, maxHatchTickDelay);
	}

	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!this.canSurvive(pState, pLevel, pPos)) {
			this.destroyBlock(pLevel, pPos);
		} else {
			this.hatchFish(pLevel, pPos, pRandom);
		}
	}

	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (pEntity.getType().equals(EntityType.FALLING_BLOCK)) {
			this.destroyBlock(pLevel, pPos);
		}
	}

	private void hatchFish(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		this.destroyBlock(pLevel, pPos);
		pLevel.playSound((Player)null, pPos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
		this.spawnFish(pLevel, pPos, pRandom);
	}

	private void destroyBlock(Level pLevel, BlockPos pPos) {
		pLevel.destroyBlock(pPos, false);
	}

	private void spawnFish(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		int i = pRandom.nextInt(2, 6);

		for(int j = 1; j <= i; ++j) {
			Entity fish = getFishType().create(pLevel);
			if (fish != null) {
				double d0 = (double)pPos.getX() + this.getRandomTadpolePositionOffset(pRandom);
				double d1 = (double)pPos.getZ() + this.getRandomTadpolePositionOffset(pRandom);
				int k = pRandom.nextInt(1, 361);
				fish.moveTo(d0, (double)pPos.getY() + 0.5D, d1, (float)k, 0.0F);
				if (fish.getType() == ModEntityTypes.OLM) ((Olm)fish).setAge(-24000); 
				pLevel.addFreshEntity(fish);
			}
		}

	}

	private double getRandomTadpolePositionOffset(RandomSource pRandom) {
		double d0 = (double)(Tadpole.HITBOX_WIDTH / 2.0F);
		return Mth.clamp(pRandom.nextDouble(), d0, 1.0D - d0);
	}

	@VisibleForTesting
	public static void setHatchDelay(int pMinHatchDelay, int pMaxHatchDelay) {
		minHatchTickDelay = pMinHatchDelay;
		maxHatchTickDelay = pMaxHatchDelay;
	}

	@VisibleForTesting
	public static void setDefaultHatchDelay() {
		minHatchTickDelay = 3600;
		maxHatchTickDelay = 12000;
	}

	protected EntityType<?> getFishType() {
		return entityTypeSupplier.get();
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(WATERLOGGED);
	}

}
