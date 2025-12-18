package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.function.IntFunction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.ai.BreedFishGoal;
import fuffles.ichthyology.common.entity.navigator.DirectPathNavigator;
import fuffles.ichthyology.common.entity.navigator.FlightMoveController;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class Olm extends AbstractBreedableFish {
	private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.DIRECTION);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.BYTE);
	private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	private static final EntityDataAccessor<Olm.Variant> VARIANT_ID = SynchedEntityData.defineId(Olm.class, ModEntityDataSerializers.OLM_VARIANT);
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Olm.class, EntityDataSerializers.BOOLEAN);
	public float attachChangeProgress = 0F;
	public float prevAttachChangeProgress = 0F;
	private Direction prevAttachDir = Direction.DOWN;
	private boolean isUpsideDownNavigator;
	public static final String TAG_VARIANT = "Variant";
	int layEggsCounter;


	public Olm(EntityType<? extends WaterAnimal> type, Level world) {
		super(type, world);
		switchNavigator(true);
	}
	
	@Override
	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((double)0, (double)-0.4F, (double)0));
		}
		super.aiStep();
	}

	private void switchNavigator(boolean rightsideUp) {
		if (rightsideUp) {
			this.moveControl =  new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 0.1F, true);
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
		this.goalSelector.addGoal(0, new OlmBreedGoal(this, 1));
		this.goalSelector.addGoal(1, new OlmLayEggsGoal(this, 1));
		this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
	}

	public static int createTagToFishType(CompoundTag tag) {
		Olm.Variant variant = Olm.Variant.readVariant(tag);
		return variant != null ? variant.getId() : 0;
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {		
		return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
	}

	@SuppressWarnings("deprecation")
	public void tick() {
		super.tick();
		if (attachChangeProgress > 0F) {
			attachChangeProgress -= 0.25F;
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
	}

	@SuppressWarnings("unused")
	private boolean isClimeableFromSide(BlockPos offsetPos, Direction opposite) {
		return false;
	}

	protected void onInsideBlock(BlockState state) {

	}

	public boolean onClimbable() {
		return false;
	}

	public boolean isBesideClimbableBlock() {
		return false;
	}

	public void setBesideClimbableBlock(boolean climbing) {
		
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CLIMBING, (byte) 0);
		this.entityData.define(ATTACHED_FACE, Direction.DOWN);
		this.entityData.define(HAS_EGGS, false);
		this.entityData.define(LAYING_EGGS, false);
		this.entityData.define(VARIANT_ID, Olm.Variant.WHITE);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
		compound.putBoolean("HasEggs", this.hasEggs());
		this.getVariant().writeVariant(compound);
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);	
		compound.putByte("AttachFace", (byte) this.entityData.get(ATTACHED_FACE).get3DDataValue());
		this.setHasEggs(compound.getBoolean("HasEggs"));
		Olm.Variant variant = Olm.Variant.readVariant(compound);
		this.entityData.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
		if (variant != null) this.setVariant(variant);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.OLM_BUCKET);
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(ModItems.OLM_SPAWN_EGG);
	}

	public boolean hasEggs() {
		return this.entityData.get(HAS_EGGS);
	}

	void setHasEggs(boolean pHasEggs) {
		this.entityData.set(HAS_EGGS, pHasEggs);
	}

	public boolean isLayingEggs() {
		return this.entityData.get(LAYING_EGGS);
	}

	void setLayingEggs(boolean pIsLayingEggs) {
		this.layEggsCounter = pIsLayingEggs ? 1 : 0;
		this.entityData.set(LAYING_EGGS, pIsLayingEggs);
	}

	public boolean isFood(ItemStack pStack) {
		return pStack.is(ModItems.CRAYFISH) && pStack.getTag().get("Variant").getAsString() == "cave";
	}

	public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle) {
		if (stack.hasTag()) {
			Olm.Variant variant = Olm.Variant.readVariant(stack.getTag());
			if (variant != null) {
				tooltipComponents.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveToBucketTag(ItemStack pStack) {
		Bucketable.saveDefaultDataToBucketTag(this, pStack);
		this.getVariant().writeVariant(pStack.getOrCreateTag());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadFromBucketTag(CompoundTag pTag) {
		Bucketable.loadDefaultDataFromBucketTag(this, pTag);
		Olm.Variant variant = Olm.Variant.readVariant(pTag);
		if (variant != null) this.setVariant(variant);
		else this.setVariant(Olm.Variant.byId(this.level().getRandom().nextInt(Olm.Variant.VALUES.length)));
	}

	public Olm.Variant getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	public void setVariant(Olm.Variant variant) {
		this.entityData.set(VARIANT_ID, variant);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	static class OlmLayEggsGoal extends MoveToBlockGoal {
		private final Olm olm;

		OlmLayEggsGoal(Olm olm, double speedModifier) {
			super(olm, speedModifier, 16);
			this.olm = olm;
		}


		public double acceptedDistance() {
			return 1D;
		}

		public boolean canUse() {
			return olm.hasEggs() ? super.canUse() : false;
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && olm.hasEggs();
		}

		public void tick() {
			super.tick();
			BlockPos blockpos = olm.blockPosition();
			if (this.isReachedTarget()) {
				if (olm.layEggsCounter < 1) {
					olm.setLayingEggs(true);
				} else if (olm.layEggsCounter > this.adjustedTickDelay(200)) {
					Level level = olm.level();
					level.playSound((Player)null, blockpos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
					BlockState blockstate = ModBlocks.OLM_EGGS.defaultBlockState();
					level.setBlock(blockPos, blockstate, 3);
					olm.setHasEggs(false);
					olm.setLayingEggs(false);
					olm.setInLoveTime(600);
				}
				if (olm.isLayingEggs()) {
					++olm.layEggsCounter;
				}
			}
		}

		protected boolean isValidTarget(LevelReader level, BlockPos pos) {
			Block block = level.getBlockState(pos).getBlock();
			return block == Blocks.WATER && (level.getBlockState(pos.below()).is(Blocks.DRIPSTONE_BLOCK) || level.getBlockState(pos.below()).is(Blocks.CALCITE)) && level.getBrightness(LightLayer.BLOCK, pos) <= 8;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean checkOlmSpawnRules(EntityType<? extends LivingEntity> p_217018_, ServerLevelAccessor p_217019_, MobSpawnType p_217020_, BlockPos p_217021_, RandomSource p_217022_) {
		return p_217021_.getY() <= p_217019_.getSeaLevel() - 33 && p_217019_.getRawBrightness(p_217021_, 0) == 0 && p_217019_.getBlockState(p_217021_).is(Blocks.WATER);
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		if (reason == MobSpawnType.BUCKET) return spawnData;
		else {
			if (this.getRandom().nextInt(100) == 0) this.setVariant(Variant.BLACK);
			else this.setVariant(Variant.WHITE);
			return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
		}
	}

	public static enum Variant {
		WHITE("white", Ichthyology.id("textures/entity/olm/white.png")),
		BLACK("black", Ichthyology.id("textures/entity/olm/black.png"));

		public static final Olm.Variant[] VALUES = values();
		private static final IntFunction<Olm.Variant> BY_ID = ByIdMap.continuous(Olm.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

		private final String name;
		private final ResourceLocation texture;

		private Variant(String name, ResourceLocation texture) {
			this.name = name;
			this.texture = texture;
		}

		public int getId() {
			return this.ordinal();
		}

		public String getName() {
			return this.name;
		}

		@OnlyIn(Dist.CLIENT)
		public ResourceLocation getTexture() {
			return this.texture;
		}

		public String getVariantDescriptionId() {
			return "entity." + Ichthyology.ID + ".olm.variant." + this.getName();
		}

		public void writeVariant(CompoundTag tag) {
			tag.putString(TAG_VARIANT, this.getName());
		}

		public static Olm.Variant byId(int i) {
			return BY_ID.apply(i);
		}

		@Nullable
		public static Olm.Variant byData(String name) {
			for (Olm.Variant variant : VALUES) {
				if (variant.getName().equals(name)) {
					return variant;
				}
			}
			return null;
		}

		@Nullable
		public static Olm.Variant readVariant(CompoundTag tag) {
			return byData(tag.getString(TAG_VARIANT));
		}
	}

	static class OlmBreedGoal extends BreedFishGoal {
		private final Olm olm;

		OlmBreedGoal(Olm olm, double speedModifier) {
			super(olm, speedModifier);
			this.olm = olm;
		}

		public boolean canUse() {
			return super.canUse() && !olm.hasEggs();
		}

		protected void breed() {
			ServerPlayer serverPlayer = this.fish.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null) {
				serverPlayer = this.partner.getLoveCause();
			}
			if (serverPlayer != null) {
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
			}
			this.olm.setHasEggs(true);
			this.fish.setAge(6000);
			this.partner.setAge(6000);
			this.fish.resetLove();
			this.partner.resetLove();
			RandomSource randomSource = this.fish.getRandom();
			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				this.level.addFreshEntity(new ExperienceOrb(this.level, this.fish.getX(), this.fish.getY(), this.fish.getZ(), randomSource.nextInt(7) + 1));
			}
		}
	} 

}

