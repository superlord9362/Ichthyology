package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class Pleco extends AbstractIchthyologyFish {
	private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.DIRECTION);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> PANICING = SynchedEntityData.defineId(Pleco.class, EntityDataSerializers.BOOLEAN);
	private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	public float attachChangeProgress = 0F;
	public float prevAttachChangeProgress = 0F;
	private Direction prevAttachDir = Direction.DOWN;
	
	public Pleco(EntityType<? extends WaterAnimal> p_30341_, Level p_30342_) {
		super(p_30341_, p_30342_);
		switchNavigator(true);
	}

	private void switchNavigator(boolean rightsideUp) {
		if (rightsideUp) {
			this.moveControl =  new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
			this.navigation = new WaterBoundPathNavigation(this, level());
		} else {
			this.moveControl = new FlightMoveController(this, 0.6F, false);
			this.navigation = new DirectPathNavigator(this, level());
		}
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1, 10));
		this.goalSelector.addGoal(1, new PlecoPanicGoal(this, 1.2D));
		this.goalSelector.addGoal(1, new PlecoSwimGoal(this, LivingEntity.class, 10, 1.1D, 1.2D));
	}
	
	@SuppressWarnings("unused")
	private static boolean isSideSolid(BlockGetter reader, BlockPos pos, Entity entityIn, Direction direction) {
		return Block.isFaceFull(reader.getBlockState(pos).getCollisionShape(reader, pos, CollisionContext.of(entityIn)), direction);
	}
	
	public Direction getAttachmentFacing() {
		return this.entityData.get(ATTACHED_FACE);
	}
	
	@SuppressWarnings({ "unused", "resource" })
	public void tick() {
		super.tick();
		if (attachChangeProgress > 0F) {
			attachChangeProgress -= 0.25F;
		}
		this.setMaxUpStep(0.5F);
		Vec3 vector3d = this.getDeltaMovement();
		if (!this.level().isClientSide) {
			this.setBesideClimbableBlock(this.horizontalCollision || this.verticalCollision && !this.onGround());
			if (this.onGround() || this.isInWaterOrBubble() || this.isInLava()) {
				this.entityData.set(ATTACHED_FACE, Direction.DOWN);
			} else  if (this.verticalCollision) {
				this.entityData.set(ATTACHED_FACE, Direction.UP);
			}else {
				boolean flag = false;
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
		if (!this.level().isClientSide) {
			if (this.isPanicing()) {
				switchNavigator(true);
			} else {
				switchNavigator(false);
			}
		}
	}
	
	public void aiStep() {
		super.aiStep();
		for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2, 2, 2))) {
			if ((entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight()) > (this.getBbWidth() * this.getBbWidth() * this.getBbHeight())) this.setPanicing(true);
			else this.setPanicing(false);
		}
		if (this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2, 2, 2)) == null) this.setPanicing(false);
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
		this.entityData.define(PANICING, false);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putByte("AttachFace", (byte) this.entityData.get(ATTACHED_FACE).get3DDataValue());
	}
	
	public boolean isPanicing() {
		return this.entityData.get(PANICING);
	}
	
	public void setPanicing(boolean isPanicing) {
		this.entityData.set(PANICING, isPanicing);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return ModItems.PLECO_BUCKET.getDefaultInstance();
	}
	
	@SuppressWarnings("rawtypes")
	public class PlecoSwimGoal extends AvoidEntityGoal {

		@SuppressWarnings("unchecked")
		public PlecoSwimGoal(PathfinderMob p_25027_, Class p_25028_, float p_25029_, double p_25030_, double p_25031_) {
			super(p_25027_, p_25028_, p_25029_, p_25030_, p_25031_);
		}
		
		public boolean canUse() {
			return super.canUse() && Pleco.this.isPanicing();
		}
		
	}
	
	public class PlecoPanicGoal extends PanicGoal {

		public PlecoPanicGoal(PathfinderMob p_25691_, double p_25692_) {
			super(p_25691_, p_25692_);
		}
		
		public boolean canUse() {
			return super.canUse() && Pleco.this.isPanicing();
		}
		
	}

	class FlightMoveController extends MoveControl {
		private final Mob parentEntity;
		private final float speedGeneral;
		private final boolean shouldLookAtTarget;
		private final boolean needsYSupport;


		public FlightMoveController(Mob bird, float speedGeneral, boolean shouldLookAtTarget, boolean needsYSupport) {
			super(bird);
			this.parentEntity = bird;
			this.shouldLookAtTarget = shouldLookAtTarget;
			this.speedGeneral = speedGeneral;
			this.needsYSupport = needsYSupport;
		}

		public FlightMoveController(Mob bird, float speedGeneral, boolean shouldLookAtTarget) {
			this(bird, speedGeneral, shouldLookAtTarget, false);
		}

		public FlightMoveController(Mob bird, float speedGeneral) {
			this(bird, speedGeneral, true);
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Vec3 vector3d = new Vec3(this.wantedX - parentEntity.getX(), this.wantedY - parentEntity.getY(), this.wantedZ - parentEntity.getZ());
				double d0 = vector3d.length();
				if (d0 < parentEntity.getBoundingBox().getSize()) {
					this.operation = MoveControl.Operation.WAIT;
					parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().scale(0.5D));
				} else {
					parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().add(vector3d.scale(this.speedModifier * speedGeneral * 0.05D / d0)));
					if (needsYSupport) {
						double d1 = this.wantedY - parentEntity.getY();
						parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().add(0.0D, (double) parentEntity.getSpeed() * speedGeneral * Mth.clamp(d1, -1, 1) * 0.6F, 0.0D));
					}
					if (parentEntity.getTarget() == null || !shouldLookAtTarget) {
						Vec3 vector3d1 = parentEntity.getDeltaMovement();
						parentEntity.setYRot(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI));
						parentEntity.yBodyRot = parentEntity.getYRot();
					} else {
						double d2 = parentEntity.getTarget().getX() - parentEntity.getX();
						double d1 = parentEntity.getTarget().getZ() - parentEntity.getZ();
						parentEntity.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
						parentEntity.yBodyRot = parentEntity.getYRot();
					}
				}

			} else if (this.operation == Operation.STRAFE) {
				this.operation = Operation.WAIT;
			}
		}
		@SuppressWarnings("unused")
		private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
			AABB axisalignedbb = this.parentEntity.getBoundingBox();

			for (int i = 1; i < p_220673_2_; ++i) {
				axisalignedbb = axisalignedbb.move(p_220673_1_);
				if (!this.parentEntity.level().noCollision(this.parentEntity, axisalignedbb)) {
					return false;
				}
			}

			return true;
		}
	}
	
	public class DirectPathNavigator extends GroundPathNavigation {

	    private Mob mob;
	    private float yMobOffset = 0;

	    public DirectPathNavigator(Mob mob, Level world) {
	        this(mob, world, 0);
	    }

	    public DirectPathNavigator(Mob mob, Level world, float yMobOffset) {
	        super(mob, world);
	        this.mob = mob;
	        this.yMobOffset = yMobOffset;
	    }

	    public void tick() {
	        ++this.tick;
	    }

	    public boolean moveTo(double x, double y, double z, double speedIn) {
	        mob.getMoveControl().setWantedPosition(x, y, z, speedIn);
	        return true;
	    }

	    public boolean moveTo(Entity entityIn, double speedIn) {
	        mob.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY() + yMobOffset, entityIn.getZ(), speedIn);
	        return true;
	    }

	}

}
