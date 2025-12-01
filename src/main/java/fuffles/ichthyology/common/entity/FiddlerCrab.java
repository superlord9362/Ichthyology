package fuffles.ichthyology.common.entity;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import fuffles.ichthyology.common.blocks.CrabBurrowingBlock;
import fuffles.ichthyology.common.entity.navigator.DirectPathNavigator;
import fuffles.ichthyology.common.entity.navigator.FlightMoveController;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class FiddlerCrab extends Animal implements Bucketable {
	private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(FiddlerCrab.class, EntityDataSerializers.DIRECTION);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(FiddlerCrab.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(FiddlerCrab.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> WAVING = SynchedEntityData.defineId(FiddlerCrab.class, EntityDataSerializers.BOOLEAN);
	private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	public float attachChangeProgress = 0F;
	public float prevAttachChangeProgress = 0F;
	private Direction prevAttachDir = Direction.DOWN;
	private boolean isUpsideDownNavigator;

	public FiddlerCrab(EntityType<? extends FiddlerCrab> type, Level world) {
		super(type, world);
		switchNavigator(true);
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	private void switchNavigator(boolean rightsideUp) {
		if (rightsideUp) {
			this.moveControl = new MoveControl(this);
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

	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	public Direction getAttachmentFacing() {
		return this.entityData.get(ATTACHED_FACE);
	}

	protected PathNavigation createNavigation(Level worldIn) {
		return new WallClimberNavigation(this, worldIn);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2D));
		this.goalSelector.addGoal(2, new FiddlerCrabBurrowGoal(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new FiddlerCrabAvoidGoal(this, Player.class, 6, 1, 1.2D));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
	}
	
	public void aiStep() {
		super.aiStep();
		List<FiddlerCrab> fiddlerCrab = this.level().getEntitiesOfClass(FiddlerCrab.class, this.getBoundingBox().inflate(4, 4, 4));
		if (!fiddlerCrab.isEmpty() && fiddlerCrab.get(0) != this) {
			if (this.random.nextInt(200) == 98) {
				this.setWaving(this.isWaving() ? false : true);
			}
			if (fiddlerCrab.get(0).distanceTo(this) > 3) fiddlerCrab.remove(0); 
		} else {
			this.setWaving(false);
		}
		if (this.getLastHurtByMob() != null) this.setWaving(false);
	}

	public void tick() {
		super.tick();
		if (attachChangeProgress > 0F) {
			attachChangeProgress -= 0.25F;
		}
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
	}

	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.fromBucket();
	}

	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.FIDDLER_CRAB_BUCKET);
	}

	@Override
	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	@Override
	public void setFromBucket(boolean pFromBucket) {
		this.entityData.set(FROM_BUCKET, pFromBucket);		
	}
	
	public boolean isWaving() {
		return this.entityData.get(WAVING);
	}
	
	public void setWaving(boolean isWaving) {
		this.entityData.set(WAVING, isWaving);
	}

	@Override
	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack stack = pPlayer.getItemInHand(pHand);
		Item item = stack.getItem();
		if (item == Items.AIR) {
			ItemStack newStack = new ItemStack(ModItems.FIDDLER_CRAB);
			if (this.hasCustomName()) newStack.setHoverName(this.getCustomName());
			pPlayer.setItemInHand(pHand, newStack);
			this.discard();
			return InteractionResult.SUCCESS;
		} else return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
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

	@SuppressWarnings("deprecation")
	@Override
	public void saveToBucketTag(ItemStack pStack) {
		Bucketable.saveDefaultDataToBucketTag(this, pStack);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadFromBucketTag(CompoundTag pTag) {
		Bucketable.loadDefaultDataFromBucketTag(this, pTag);
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
		this.entityData.define(FROM_BUCKET, false);
		this.entityData.define(WAVING, false);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setFromBucket(compound.getBoolean("FromBucket"));
		this.entityData.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
		this.setWaving(compound.getBoolean("IsWaving"));
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("FromBucket", this.fromBucket());
		compound.putByte("AttachFace", (byte) this.entityData.get(ATTACHED_FACE).get3DDataValue());
		compound.putBoolean("IsWaving", isWaving());
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
		return null;
	}

	static class FiddlerCrabBurrowGoal extends RandomStrollGoal {
		@Nullable
		private Direction selectedDirection;
		private boolean doMerge;

		public FiddlerCrabBurrowGoal(FiddlerCrab pFiddlerCrab) {
			super(pFiddlerCrab, 1.0D, 10);
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (this.mob.getTarget() != null) {
				return false;
			} else if (!this.mob.getNavigation().isDone()) {
				return false;
			} else {
				RandomSource randomsource = this.mob.getRandom();
				if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.mob.level(), this.mob) && randomsource.nextInt(reducedTickDelay(10)) == 0) {
					this.selectedDirection = Direction.getRandom(randomsource);
					BlockPos blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
					BlockState blockstate = this.mob.level().getBlockState(blockpos);
					if (CrabBurrowingBlock.isCompatibleHostBlock(blockstate)) {
						this.doMerge = true;
						return true;
					}
				}

				this.doMerge = false;
				return super.canUse();
			}
		}

		public boolean canContinueToUse() {
			return this.doMerge ? false : super.canContinueToUse();
		}

		public void start() {
			if (!this.doMerge) {
				super.start();
			} else {
				LevelAccessor levelaccessor = this.mob.level();
				BlockPos blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
				BlockState blockstate = levelaccessor.getBlockState(blockpos);
				if (CrabBurrowingBlock.isCompatibleHostBlock(blockstate)) {
					levelaccessor.setBlock(blockpos, CrabBurrowingBlock.infestedStateByHost(blockstate), 3);
					this.mob.spawnAnim();
					this.mob.discard();
				}
			}
		}
	}
	
	public class FiddlerCrabAvoidGoal extends AvoidEntityGoal<Player> {
		
		Player player;

		public FiddlerCrabAvoidGoal(PathfinderMob pMob, Class<Player> pEntityClassToAvoid, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier) {
			super(pMob, pEntityClassToAvoid, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier);
			this.player = this.toAvoid;
		}
		
		public boolean canUse() {
			if (player != null) return super.canUse() && player.isSprinting();
			else return false;
		}
		
	}

}
