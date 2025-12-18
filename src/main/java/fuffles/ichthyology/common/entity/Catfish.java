package fuffles.ichthyology.common.entity;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.common.entity.ai.BreedFishGoal;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityTypes;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Catfish extends AbstractBreedableFish {
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HUNTING = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	int layEggsCounter;
	int huntTimer;

	public static final Predicate<LivingEntity> SMALL_ENTITY = (entity) -> {
		return entity.getBbWidth() <= 0.25F && entity.getType() != ModEntityTypes.CATFISH && entity.getType() != ModEntityTypes.CATFISH_BABY || entity.getType() == ModEntityTypes.CRAYFISH;
	};

	public Catfish(EntityType<? extends Catfish> entityType, Level level) {
		super(entityType, level);
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

	public boolean isHunting() {
		return this.entityData.get(HUNTING);
	}

	void setHunting(boolean isHunting) {
		this.entityData.set(HUNTING, isHunting);
	}

	public boolean isFood(ItemStack pStack) {
		return pStack.is(ModItems.CRAYFISH);
	}
	
	public int huntTimer() {
		return huntTimer;
	}
	
	public void setHuntTimer(int timer) {
		huntTimer = timer;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAS_EGGS, false);
		this.entityData.define(LAYING_EGGS, false);
		this.entityData.define(HUNTING, false);
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("HasEggs", this.hasEggs());
		pCompound.putInt("HuntingTicks", this.huntTimer());
	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setHasEggs(pCompound.getBoolean("HasEggs"));
		this.setHuntTimer(pCompound.getInt("HuntingTicks"));
	}

	public boolean canFallInLove() {
		return super.canFallInLove() && !this.hasEggs();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FOLLOW_RANGE, 10);
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

	public void aiStep() {
		if (huntTimer() < 2400 && !isHunting()) {
			setHuntTimer(huntTimer() + 1);
		} else {
			if (!isHunting()) setHunting(true);
			setHuntTimer(0);
		}
		
		super.aiStep();
	}

	public class CatfishSuckUpEnemyGoal extends MeleeAttackGoal {

		Catfish catfish;

		public CatfishSuckUpEnemyGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
			super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
			pMob = catfish;
		}

		protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
			if (Catfish.this.getTarget() != null && Catfish.this.getTarget().isEffectiveAi()) {
				if (Catfish.this.getTarget().distanceTo(Catfish.this) < 0.35F) {
					Catfish.this.getTarget().kill();
					stop();
				}
				else if (Catfish.this.getTarget().distanceTo(Catfish.this) < 2.25F) {
					Catfish.this.getTarget().setDeltaMovement(Catfish.this.getTarget().position().vectorTo(Catfish.this.position()).normalize().scale(0.35D));
				} else {
					Catfish.this.lookControl.setLookAt(Catfish.this.getTarget().getX(), Catfish.this.getTarget().getY(), Catfish.this.getTarget().getZ());
					Catfish.this.setDeltaMovement(Catfish.this.position().vectorTo(Catfish.this.getTarget().position()).normalize().scale(0.15D));
				}
			}
		}

		protected double getAttackReachSqr(LivingEntity pAttackTarget) {
			return (double)(6.0F + pAttackTarget.getBbWidth());
		}
		
		public boolean canUse() {
			return super.canUse() && Catfish.this.isHunting() && catfish != null;
 		}
		
		public boolean canContinueToUse() {
			return super.canContinueToUse() && Catfish.this.isHunting();
		}
		
		public void stop() {
			super.stop();
			Catfish.this.setHunting(false);
		}

	}

	static class CatfishBreedGoal extends BreedFishGoal {
		private final Catfish catfish;

		CatfishBreedGoal(Catfish catfish, double speedModifier) {
			super(catfish, speedModifier);
			this.catfish = catfish;
		}

		public boolean canUse() {
			return super.canUse() && !catfish.hasEggs();
		}

		protected void breed() {
			ServerPlayer serverPlayer = this.fish.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null) {
				serverPlayer = this.partner.getLoveCause();
			}
			if (serverPlayer != null) {
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
			}
			this.catfish.setHasEggs(true);
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
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		setHuntTimer(0);
		if (reason == MobSpawnType.BUCKET) return spawnData;
		else return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
	}
	
	@Override
	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (itemstack.getItem() == Items.BUCKET) {
			return InteractionResult.PASS;
		} else return super.mobInteract(pPlayer, pHand);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return null;
	}

}
