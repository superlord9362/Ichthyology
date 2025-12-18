package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.common.entity.navigator.DirectPathNavigator;
import fuffles.ichthyology.common.entity.navigator.FlightMoveController;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class Pleco extends AbstractIchthyologyFish {
	private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.DIRECTION);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> FRENZY = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LOOKING = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HORIZONTAL = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.BOOLEAN);
	private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	public float attachChangeProgress = 0F;
	public float prevAttachChangeProgress = 0F;
	private Direction prevAttachDir = Direction.DOWN;
	private boolean isUpsideDownNavigator;
	private int frenzyTime = 1200;
	private int blockAttractionCooldownTime = 3600;
	private int blockAttractionTime = 600;

	public Pleco(EntityType<? extends WaterAnimal> type, Level world) {
		super(type, world);
		switchNavigator(true);
	}

	private void switchNavigator(boolean rightsideUp) {
		if (rightsideUp) {
			this.moveControl =  new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
			this.navigation = new WallClimberNavigation(this, level());
			this.isUpsideDownNavigator = false;
		} else {
			this.moveControl = new FlightMoveController(this, 0.6F, false);
			this.navigation = new DirectPathNavigator(this, level());
			this.isUpsideDownNavigator = true;
		}
	}

	@SuppressWarnings("unused")
	private static boolean isSideSolid(BlockGetter reader, BlockPos pos, Entity entityIn, Direction direction) {
		return Block.isFaceFull(reader.getBlockState(pos).getCollisionShape(reader, pos, CollisionContext.of(entityIn)), direction);
	}

	public Direction getAttachmentFacing() {
		return this.entityData.get(ATTACHED_FACE);
	}

	protected PathNavigation createNavigation(Level worldIn) {
		return new WallClimberNavigation(this, worldIn);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 1));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new PlecoFeedGoal(this.isFrenzying() ? (double)1.2F : 1, 12, 1));
	}
	
	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (itemstack.getItem() == Items.MOSS_BLOCK && !this.isFrenzying()) {
			if (!pPlayer.getAbilities().instabuild) {
				itemstack.shrink(1);
			}
			this.setFrenzying(true);
			return InteractionResult.sidedSuccess(this.level().isClientSide());
		} else {
			return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
		}
	}

	@SuppressWarnings("deprecation")
	public void tick() {
		super.tick();
		if (attachChangeProgress > 0F) {
			attachChangeProgress -= 0.25F;
		}
		if (this.horizontalCollision) {
			this.setHorizontal(true);
		} else {
			this.setHorizontal(false);
		}
		this.setMaxUpStep(0.5F);
		Vec3 vector3d = this.getDeltaMovement();
		if (!this.level().isClientSide()) {
			this.setBesideClimbableBlock(this.horizontalCollision || this.verticalCollision && !this.onGround());
			if (this.onGround() || this.isInWaterOrBubble() || this.isInLava()) {
				this.entityData.set(ATTACHED_FACE, Direction.DOWN);
			} else  if (this.verticalCollision) {
				this.entityData.set(ATTACHED_FACE, Direction.UP);
			}else {
				Direction closestDirection = Direction.DOWN;
				double closestDistance = 100;
				for (Direction dir : HORIZONTALS) {
					BlockPos antPos = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ()));
					BlockPos offsetPos = antPos.relative(dir);
					Vec3 offset = Vec3.atCenterOf(offsetPos);
					if (closestDistance > this.position().distanceTo(offset) && level().loadedAndEntityCanStandOnFace(offsetPos, this, dir.getOpposite())) {
						closestDistance = this.position().distanceTo(offset);
						closestDirection = dir;
					}
				}
				this.entityData.set(ATTACHED_FACE, closestDirection);
			}
		}
		boolean flag = false;
		if (this.getAttachmentFacing() != Direction.DOWN) {
			if(this.getAttachmentFacing() == Direction.UP){
				this.setDeltaMovement(this.getDeltaMovement().add(0, 1, 0));
			}else{
				if (!this.horizontalCollision && this.getAttachmentFacing() != Direction.UP) {
					Vec3 vec = Vec3.atLowerCornerOf(this.getAttachmentFacing().getNormal());
					this.setDeltaMovement(this.getDeltaMovement().add(vec.normalize().multiply(0.1F, 0.1F, 0.1F)));
				}
				if (!this.onGround() && vector3d.y < 0.0D) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.5D, 1.0D));
					flag = true;
				}
			}
		}
		if(this.getAttachmentFacing() == Direction.UP) {
			this.setNoGravity(true);
			this.setDeltaMovement(vector3d.multiply(0.7D, 1D, 0.7D));
		}else{
			this.setNoGravity(false);
		}
		if (!flag) {
			if (this.onClimbable()) {
				this.setDeltaMovement(vector3d.multiply(1.0D, 0.4D, 1.0D));
			}
		}
		if (prevAttachDir != this.getAttachmentFacing()) {
			attachChangeProgress = 1F;
		}
		this.prevAttachDir = this.getAttachmentFacing();
		if (!this.level().isClientSide()) {
			if (this.getAttachmentFacing() == Direction.UP && !this.isUpsideDownNavigator) {
				switchNavigator(false);
			}
			if (this.getAttachmentFacing() != Direction.UP && this.isUpsideDownNavigator) {
				switchNavigator(true);
			}
		}
		if (this.isFrenzying()) {
			frenzyTime--;
		}
		if (frenzyTime <= 0) {
			this.setFrenzying(false);
			frenzyTime = 1200;
		}
		if (blockAttractionCooldownTime >= 0 && !this.isFrenzying()) {
			blockAttractionCooldownTime--;
		} else {
			this.setLooking(true);
		}
		if (blockAttractionTime <= 0) {
			blockAttractionTime = 600;
		}
	}
	
	public boolean isLooking() {
		return this.entityData.get(LOOKING);
	}
	
	private void setLooking(boolean isLooking) {
		this.entityData.set(LOOKING, isLooking);
	}
	
	public boolean isHorizontal() {
		return this.entityData.get(HORIZONTAL);
	}
	
	private void setHorizontal(boolean isHorizontal) {
		this.entityData.set(HORIZONTAL, isHorizontal);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ARMOR, 2);
	}

	@SuppressWarnings("unused")
	private boolean isClimeableFromSide(BlockPos offsetPos, Direction opposite) {
		return false;
	}

	protected void onInsideBlock(BlockState state) {

	}

	public boolean onClimbable() {
		return this.isBesideClimbableBlock();
	}

	public boolean isBesideClimbableBlock() {
		return (this.entityData.get(CLIMBING) & 1) != 0;
	}

	public void setBesideClimbableBlock(boolean climbing) {
		byte b0 = this.entityData.get(CLIMBING);
		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.entityData.set(CLIMBING, b0);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CLIMBING, (byte) 0);
		this.entityData.define(ATTACHED_FACE, Direction.DOWN);
		this.entityData.define(FRENZY, false);
		this.entityData.define(LOOKING, false);
		this.entityData.define(HORIZONTAL, false);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
		this.setFrenzying(compound.getBoolean("IsFrenzying"));
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);	
		compound.putByte("AttachFace", (byte) this.entityData.get(ATTACHED_FACE).get3DDataValue());
		compound.putBoolean("IsFrenzying", this.isFrenzying());
	}
	
	public boolean isFrenzying() {
		return this.entityData.get(FRENZY);
	}
	
	private void setFrenzying(boolean isFrenzying) {
		this.entityData.set(FRENZY, isFrenzying);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.PLECO_BUCKET);
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(ModItems.PLECO_SPAWN_EGG);
	}

	public class PlecoFeedGoal extends MoveToBlockGoal {
		@SuppressWarnings("unused")
		private static final int WAIT_TICKS = 40;
		protected int ticksWaited;

		public PlecoFeedGoal(double p_28675_, int p_28676_, int p_28677_) {
			super(Pleco.this, p_28675_, p_28676_, p_28677_);
		}

		public double acceptedDistance() {
			return 2.0D;
		}

		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		protected boolean isValidTarget(LevelReader p_28680_, BlockPos p_28681_) {
			BlockState blockstate = p_28680_.getBlockState(p_28681_);
			return (blockstate.is(BlockTags.LOGS) && !blockstate.is(ModBlocks.Tags.STRIPPED_LOGS) || blockstate.is(Blocks.MOSSY_COBBLESTONE) || blockstate.is(Blocks.MOSSY_COBBLESTONE_SLAB) || blockstate.is(Blocks.MOSSY_COBBLESTONE_STAIRS) || blockstate.is(Blocks.MOSSY_COBBLESTONE_WALL) || blockstate.is(Blocks.MOSSY_STONE_BRICK_SLAB) || blockstate.is(Blocks.MOSSY_STONE_BRICK_STAIRS) || blockstate.is(Blocks.MOSSY_STONE_BRICK_WALL) || blockstate.is(Blocks.MOSSY_STONE_BRICKS));
		}

		public void tick() {
			if (this.isReachedTarget()) {
				if (this.ticksWaited >= 40) {
					this.onReachedTarget();
				} else {
					++this.ticksWaited;
				}
				if (this.ticksWaited % 5 == 0) {
					level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MOSS_BLOCK.defaultBlockState()), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.0D, 0.0D, 0.0D);
				}
				if (Pleco.this.blockAttractionTime >= 0) {
					Pleco.this.blockAttractionTime--;
				} else if (!Pleco.this.isFrenzying()) {
					Pleco.this.setLooking(false);
					stop();
				}
				
			}
			super.tick();
		}



		@javax.annotation.Nullable
		public static BlockState getAxeStrippingState(BlockState originalState) {
			Block block = AxeItem.STRIPPABLES.get(originalState.getBlock());
			return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)) : null;
		}

		protected void onReachedTarget() {
			if (Pleco.this.isFrenzying()) {
				if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(Pleco.this.level(), Pleco.this)) {
					BlockState blockstate = level().getBlockState(this.blockPos);
					if (AxeItem.STRIPPABLES.containsKey(level().getBlockState(this.blockPos).getBlock())) {
						level().setBlockAndUpdate(this.blockPos, getAxeStrippingState(level().getBlockState(blockPos)));
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						level().setBlockAndUpdate(this.blockPos, Blocks.COBBLESTONE.defaultBlockState());
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_STONE_BRICKS) {
						level().setBlockAndUpdate(this.blockPos, Blocks.STONE_BRICKS.defaultBlockState());
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_COBBLESTONE_STAIRS) {
						level().setBlockAndUpdate(this.blockPos, Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, blockstate.getValue(StairBlock.FACING)).setValue(StairBlock.HALF, blockstate.getValue(StairBlock.HALF)).setValue(StairBlock.SHAPE, blockstate.getValue(StairBlock.SHAPE)).setValue(StairBlock.WATERLOGGED, blockstate.getValue(StairBlock.WATERLOGGED)));
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_STONE_BRICK_STAIRS) {
						level().setBlockAndUpdate(this.blockPos, Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, blockstate.getValue(StairBlock.FACING)).setValue(StairBlock.HALF, blockstate.getValue(StairBlock.HALF)).setValue(StairBlock.SHAPE, blockstate.getValue(StairBlock.SHAPE)).setValue(StairBlock.WATERLOGGED, blockstate.getValue(StairBlock.WATERLOGGED)));
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_COBBLESTONE_SLAB) {
						level().setBlockAndUpdate(this.blockPos, Blocks.COBBLESTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, blockstate.getValue(SlabBlock.TYPE)).setValue(SlabBlock.WATERLOGGED, blockstate.getValue(SlabBlock.WATERLOGGED)));
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_STONE_BRICK_SLAB) {
						level().setBlockAndUpdate(this.blockPos, Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, blockstate.getValue(SlabBlock.TYPE)).setValue(SlabBlock.WATERLOGGED, blockstate.getValue(SlabBlock.WATERLOGGED)));
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_COBBLESTONE_WALL) {
						level().setBlockAndUpdate(this.blockPos, Blocks.COBBLESTONE_WALL.defaultBlockState().setValue(WallBlock.EAST_WALL, blockstate.getValue(WallBlock.EAST_WALL)).setValue(WallBlock.NORTH_WALL, blockstate.getValue(WallBlock.NORTH_WALL)).setValue(WallBlock.SOUTH_WALL, blockstate.getValue(WallBlock.SOUTH_WALL)).setValue(WallBlock.UP, blockstate.getValue(WallBlock.UP)).setValue(WallBlock.WATERLOGGED, blockstate.getValue(WallBlock.WATERLOGGED)).setValue(WallBlock.WEST_WALL, blockstate.getValue(WallBlock.WEST_WALL)));
					}
					if (level().getBlockState(this.blockPos).getBlock() == Blocks.MOSSY_STONE_BRICK_WALL) {
						level().setBlockAndUpdate(this.blockPos, Blocks.STONE_BRICK_WALL.defaultBlockState().setValue(WallBlock.EAST_WALL, blockstate.getValue(WallBlock.EAST_WALL)).setValue(WallBlock.NORTH_WALL, blockstate.getValue(WallBlock.NORTH_WALL)).setValue(WallBlock.SOUTH_WALL, blockstate.getValue(WallBlock.SOUTH_WALL)).setValue(WallBlock.UP, blockstate.getValue(WallBlock.UP)).setValue(WallBlock.WATERLOGGED, blockstate.getValue(WallBlock.WATERLOGGED)).setValue(WallBlock.WEST_WALL, blockstate.getValue(WallBlock.WEST_WALL)));
					}
				}
			}
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && (Pleco.this.isLooking() || Pleco.this.isFrenzying());
		}

		public boolean canUse() {
			return super.canUse() && (Pleco.this.isLooking() || Pleco.this.isFrenzying());
		}

		public void start() {
			this.ticksWaited = 0;
			super.start();
		}
		
		public void stop() {
			Pleco.this.blockAttractionCooldownTime = 3600;
			Pleco.this.blockAttractionTime = 600;
			super.stop();
		}
	}

}
