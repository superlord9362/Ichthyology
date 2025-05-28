package fuffles.ichthyology.common.entity;

import java.util.function.Predicate;

import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityTypes;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Catfish extends Animal {
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	int layEggsCounter;

	public static final Predicate<LivingEntity> SMALL_ENTITY = (entity) -> {
		return entity.getBbWidth() <= 0.25F && entity.getType() != ModEntityTypes.CATFISH && entity.getType() != ModEntityTypes.CATFISH_BABY;
	};

	public Catfish(EntityType<? extends Catfish> entityType, Level level) {
		super(entityType, level);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new CatfishRestInVegetationGoal(1, 10, 5));
		this.goalSelector.addGoal(2, new CatfishSuckUpEnemyGoal(this, 1, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, SMALL_ENTITY));
		this.goalSelector.addGoal(0, new CatfishBreedGoal(this, 1));
		this.goalSelector.addGoal(1, new CatfishLayEggsGoal(this, 1));
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
		return pStack.is(ModItems.CRAYFISH);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAS_EGGS, false);
		this.entityData.define(LAYING_EGGS, false);
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("HasEggs", this.hasEggs());
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setHasEggs(pCompound.getBoolean("HasEggs"));
	}

	public boolean canFallInLove() {
		return super.canFallInLove() && !this.hasEggs();
	}

	public void baseTick() {
		int i = this.getAirSupply();
		super.baseTick();
		if (!this.isNoAi()) {
			this.handleAirSupply(i);
		}
	}

	protected void handleAirSupply(int pAirSupply) {
		if (this.isAlive() && !this.isInWaterRainOrBubble()) {
			this.setAirSupply(pAirSupply - 1);
			if (this.getAirSupply() == -20) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().dryOut(), 2.0F);
			}
		} else {
			this.setAirSupply(this.getMaxAirSupply());
		}
	}

	public MobType getMobType() {
		return MobType.WATER;
	}

	public void rehydrate() {
		int i = this.getAirSupply() + 1800;
		this.setAirSupply(Math.min(i, this.getMaxAirSupply()));
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public boolean isPushedByFluid() {
		return false;
	}

	protected PathNavigation createNavigation(Level pLevel) {
		return new WaterBoundPathNavigation(this, pLevel);
	}

	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence();
	}

	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return !this.hasCustomName();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FOLLOW_RANGE, 10);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		return null;
	}

	public class CatfishRestInVegetationGoal extends MoveToBlockGoal {
		protected int ticksWaited;

		public CatfishRestInVegetationGoal(double p_28675_, int p_28676_, int p_28677_) {
			super(Catfish.this, p_28675_, p_28676_, p_28677_);
		}

		public double acceptedDistance() {
			return 0.5D;
		}

		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		protected boolean isValidTarget(LevelReader p_28680_, BlockPos p_28681_) {
			BlockState blockstate = p_28680_.getBlockState(p_28681_);
			return (blockstate.is(Blocks.SEAGRASS) || blockstate.is(Blocks.TALL_SEAGRASS) || blockstate.is(Blocks.KELP) || blockstate.is(Blocks.KELP_PLANT));
		}

		public void tick() {
			if (this.isReachedTarget()) {
				if (this.ticksWaited >= 40) {
				} else {
					++this.ticksWaited;
				}
			}

			super.tick();
		}

		public boolean canUse() {
			return super.canUse() && Catfish.this.level().isDay();
		}

		public boolean canContinueToUse() {
			return Catfish.this.level().isDay();
		}

		public void start() {
			this.ticksWaited = 0;
			super.start();
		}
	}

	public void travel(Vec3 pTravelVector) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(this.getSpeed(), pTravelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(pTravelVector);
		}
	}
	
	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
			this.setOnGround(false);
			this.hasImpulse = true;
			this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
		}

		super.aiStep();
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.COD_FLOP;
	}

	public class CatfishSuckUpEnemyGoal extends MeleeAttackGoal {

		Catfish catfish;

		public CatfishSuckUpEnemyGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
			super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
			pMob = catfish;
		}

		protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
			if (Catfish.this.getTarget() != null) {
				if (Catfish.this.getTarget().distanceTo(Catfish.this) > 0.75F) {
					Catfish.this.getTarget().setDeltaMovement(Catfish.this.getTarget().position().vectorTo(Catfish.this.position()).normalize().scale(0.75D));
				} else {
					Catfish.this.getTarget().remove(Entity.RemovalReason.KILLED);
				}
			}
		}

		protected double getAttackReachSqr(LivingEntity pAttackTarget) {
			return (double)(6.0F + pAttackTarget.getBbWidth());
		}

	}

	static class CatfishBreedGoal extends BreedGoal {
		private final Catfish catfish;

		CatfishBreedGoal(Catfish catfish, double speedModifier) {
			super(catfish, speedModifier);
			this.catfish = catfish;
		}

		public boolean canUse() {
			return super.canUse() && !catfish.hasEggs();
		}

		protected void breed() {
			ServerPlayer serverPlayer = this.animal.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null) {
				serverPlayer = this.partner.getLoveCause();
			}
			if (serverPlayer != null) {
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
				CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this.animal, this.partner, (AgeableMob)null);
			}
			this.catfish.setHasEggs(true);
			this.animal.setAge(6000);
			this.partner.setAge(6000);
			this.animal.resetLove();
			this.partner.resetLove();
			RandomSource randomSource = this.animal.getRandom();
			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), randomSource.nextInt(7) + 1));
			}
		}
	} 

	static class CatfishLayEggsGoal extends MoveToBlockGoal {
		private final Catfish catfish;

		CatfishLayEggsGoal(Catfish catfish, double speedModifier) {
			super(catfish, speedModifier, 16);
			this.catfish = catfish;
		}


		public double acceptedDistance() {
			return 1D;
		}

		public boolean canUse() {
			return catfish.hasEggs() ? super.canUse() : false;
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && catfish.hasEggs();
		}

		public void tick() {
			super.tick();
			BlockPos blockpos = catfish.blockPosition();
			if (this.isReachedTarget()) {
				if (catfish.layEggsCounter < 1) {
					catfish.setLayingEggs(true);
				} else if (catfish.layEggsCounter > this.adjustedTickDelay(200)) {
					Level level = catfish.level();
					level.playSound((Player)null, blockpos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
					BlockState blockstate = ModBlocks.CATFISH_ROE.defaultBlockState();
					level.setBlock(blockPos, blockstate, 3);
					catfish.setHasEggs(false);
					catfish.setLayingEggs(false);
					catfish.setInLoveTime(600);
				}
				if (catfish.isLayingEggs()) {
					++catfish.layEggsCounter;
				}
			}
		}

		@SuppressWarnings("deprecation")
		protected boolean isValidTarget(LevelReader level, BlockPos pos) {
			Block block = level.getBlockState(pos).getBlock();
			return block == Blocks.WATER && level.getBlockState(pos.below()).isSolid();
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends Animal> pWaterAnimal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
		int i = pLevel.getSeaLevel();
		int j = i - 13;
		return pPos.getY() >= j && pPos.getY() <= i && pLevel.getFluidState(pPos.below()).is(FluidTags.WATER) && pLevel.getBlockState(pPos.above()).is(Blocks.WATER);
	}

}
