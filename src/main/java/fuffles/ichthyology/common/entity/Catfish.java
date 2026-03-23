package fuffles.ichthyology.common.entity;

import java.util.Collection;
import java.util.function.Predicate;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.ai.BreedFishGoalRedux;
import fuffles.ichthyology.init.ModSoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
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
import org.jetbrains.annotations.Nullable;

public class Catfish extends AbstractAgeableFish implements ManipulatesMobDropsFromKills
{
	public static final Predicate<LivingEntity> SMALL_ENTITY_PREDICATE = entity -> (entity.getBbWidth() < ModEntityTypes.CATFISH.getWidth() && entity.getType() != ModEntityTypes.CATFISH) || entity.getType() == ModEntityTypes.CRAYFISH;
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);
	//private static final EntityDataAccessor<Boolean> HUNTING = SynchedEntityData.defineId(Catfish.class, EntityDataSerializers.BOOLEAN);

	private int huntingCooldown;

	public Catfish(EntityType<Catfish> entityType, Level level)
	{
		super(entityType, level);
		this.huntingCooldown = 0;
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(HAS_EGGS, false);
		this.entityData.define(LAYING_EGGS, false);
		//this.entityData.define(HUNTING, false);
	}

	public boolean hasEggs()
	{
		return this.entityData.get(HAS_EGGS);
	}

	public void setHasEggs(boolean hasEggs)
	{
		this.entityData.set(HAS_EGGS, hasEggs);
	}

	public boolean isLayingEggs()
	{
		return this.entityData.get(LAYING_EGGS);
	}

	public void setLayingEggs(boolean isLayingEggs)
	{
		this.entityData.set(LAYING_EGGS, isLayingEggs);
	}

    /*public boolean isHunting()
    {
        return this.entityData.get(HUNTING);
    }

    public void setHunting(boolean isHunting)
    {
        this.entityData.set(HUNTING, isHunting);
    }*/

	@Override
	public boolean isFood(ItemStack stack)
	{
		return stack.is(ModItems.CRAYFISH);
	}

	protected void registerGoals()
	{
		super.registerGoals();
		//Tempt goal needed
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new Catfish.CatfishRestInVegetationGoal(this,1, 10, 5));
		this.goalSelector.addGoal(2, new Catfish.CatfishSuckUpEnemyGoal(this, 1, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, SMALL_ENTITY_PREDICATE));
		this.goalSelector.addGoal(0, new Catfish.CatfishBreedGoal(this, 1));
		this.goalSelector.addGoal(1, new Catfish.CatfishLayEggsGoal(this, 1));
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FOLLOW_RANGE, 10);
	}

	@Override
	protected SoundEvent getFlopSound()
	{
		return ModSoundEvents.CATFISH_FLOP;
	}

	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob)
	{
		return ModEntityTypes.CATFISH.create(serverLevel);
	}

	@Override
	public boolean isBucketable(Player player, InteractionHand hand)
	{
		return this.isBaby();
	}

	@Override
	public ItemStack getBucketItemStack()
	{
		return ModItems.CATFISH_BABY_BUCKET.getDefaultInstance();
	}

	@Override
	protected void commonSaveToTag(CompoundTag tag, boolean bucketTag)
	{
		super.commonSaveToTag(tag, bucketTag);
		if (!bucketTag) // Only babies are bucketable
		{
			tag.putBoolean("HasEggs", this.hasEggs());
			tag.putInt("HuntingCooldown", this.huntingCooldown);
		}
	}

	@Override
	protected void commonLoadFromTag(CompoundTag tag, boolean bucketTag)
	{
		super.commonLoadFromTag(tag, bucketTag);
		if (!bucketTag)
		{
			this.setHasEggs(tag.getBoolean("HasEggs"));
			this.huntingCooldown = tag.getInt("HuntingCooldown");
		}
		if (bucketTag && !tag.contains("Age", CompoundTag.TAG_INT))
			this.setBaby(true);
	}

	@Override
	public boolean canFallInLove()
	{
		return super.canFallInLove() && !this.hasEggs();
	}

	public void aiStep()
	{
		if (this.isBaby())
			this.huntingCooldown = 2400;
		if (this.huntingCooldown > 0)
			this.huntingCooldown--;
		super.aiStep();
	}

	@Override
	public boolean hasCustomBabyDrops()
	{
		return true;
	}

	@Override
	public boolean manipulateDrops(LivingEntity victim, Collection<ItemEntity> drops, DamageSource source, int lootingLevel)
	{
		// Might be fun to store items from kills in this, but ehh that would be a lot of extra work
		return false;
	}

	static class CatfishRestInVegetationGoal extends MoveToBlockGoal
	{
		public CatfishRestInVegetationGoal(Catfish catfish, double speedModifier, int searchRange, int verticalSearchRange)
		{
			super(catfish, speedModifier, searchRange, verticalSearchRange);
		}

		@Override
		public double acceptedDistance()
		{
			return 0.1D;
		}

		@Override
		public boolean shouldRecalculatePath()
		{
			return this.tryTicks % 100 == 0;
		}

		@Override
		protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos)
		{
			BlockPos belowPos = blockPos.below();
			return levelReader.getBlockState(blockPos).is(ModBlocks.Tags.CATFISH_RESTING_SPOTS) && levelReader.getBlockState(belowPos).isFaceSturdy(levelReader, belowPos, Direction.UP);
		}

		@Override
		protected BlockPos getMoveToTarget()
		{
			return this.blockPos;
		}

		@Override
		public boolean canUse()
		{
			return super.canUse() && this.mob.level().isDay();
		}

		@Override
		public boolean canContinueToUse()
		{
			return this.isValidTarget(this.mob.level(), this.blockPos) && this.mob.level().isDay();
		}
	}

	static class CatfishSuckUpEnemyGoal extends MeleeAttackGoal
	{
		public CatfishSuckUpEnemyGoal(Catfish catfish, double speedModifier, boolean followingTargetEvenIfNotSeen)
		{
			super(catfish, speedModifier, followingTargetEvenIfNotSeen);
		}

		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
			if (enemy.isEffectiveAi())
			{
				if (enemy.getBoundingBox().intersects(this.mob.getBoundingBox()))
				{
					this.mob.level().playSound(null, this.mob, ModSoundEvents.CATFISH_EAT, SoundSource.NEUTRAL, 2.0F, 1.0F);
					if (enemy.isAlive())
					{
						enemy.die(this.mob.damageSources().mobAttack(this.mob));
						enemy.remove(RemovalReason.KILLED);
					}
					((Catfish)this.mob).huntingCooldown = 2400;
					this.stop();
				}
				else if (enemy.distanceTo(this.mob) < 2.25F)
				{
					enemy.setDeltaMovement(enemy.position().vectorTo(this.mob.position()).normalize().scale(0.25D));
				}
				else
				{
					this.mob.getLookControl().setLookAt(enemy);
					this.mob.setDeltaMovement(this.mob.position().vectorTo(enemy.position()).normalize().scale(0.15D));
				}
			}
		}

		protected double getAttackReachSqr(LivingEntity attackTarget)
		{
			return 6.0F + attackTarget.getBbWidth();
		}

		public boolean canUse()
		{
			return ((Catfish)this.mob).huntingCooldown == 0 && super.canUse();
		}

		public boolean canContinueToUse()
		{
			return ((Catfish)this.mob).huntingCooldown == 0 && super.canContinueToUse();
		}
	}

	static class CatfishBreedGoal extends BreedFishGoalRedux<Catfish>
	{
		public CatfishBreedGoal(Catfish catfish, double speedModifier)
		{
			super(catfish, speedModifier);
		}

		public boolean canUse()
		{
			return super.canUse() && !this.ageableFish.hasEggs();
		}

		protected void breed()
		{
			ServerPlayer serverPlayer = this.ageableFish.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null)
				serverPlayer = this.partner.getLoveCause();
			if (serverPlayer != null)
			{
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
				AbstractAgeableFish.triggerBredAnimalsCriteria(serverPlayer, this.ageableFish, this.partner, null);
			}

			this.ageableFish.setHasEggs(true);
			this.ageableFish.setAge(6000);
			this.ageableFish.resetLove();
			this.partner.setAge(6000);
			this.partner.resetLove();
			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
				this.level.addFreshEntity(new ExperienceOrb(this.level, this.ageableFish.getX(), this.ageableFish.getY(), this.ageableFish.getZ(), this.ageableFish.getRandom().nextInt(7) + 1));
		}
	}

	static class CatfishLayEggsGoal extends MoveToBlockGoal
	{
		protected int actionTicks = 0;

		public CatfishLayEggsGoal(Catfish catfish, double speedModifier)
		{
			super(catfish, speedModifier, 16);
		}

		public double acceptedDistance()
		{
			return 1D;
		}

		public boolean canUse()
		{
			return ((Catfish)this.mob).hasEggs() && super.canUse();
		}

		public boolean canContinueToUse()
		{
			return super.canContinueToUse() && ((Catfish)this.mob).hasEggs();
		}

		public void tick()
		{
			super.tick();
			if (this.isReachedTarget())
			{
				Catfish catfish = (Catfish)this.mob;
				if (this.actionTicks == 0)
					catfish.setLayingEggs(true);

				if (this.actionTicks > this.adjustedTickDelay(200))
				{
					this.mob.level().playSound(null, this.mob.blockPosition(), ModSoundEvents.CATFISH_LAY_EGGS, SoundSource.BLOCKS, 0.3F, 0.9F + this.mob.level().random.nextFloat() * 0.2F);
					this.mob.level().setBlock(this.blockPos, ModBlocks.CATFISH_ROE.defaultBlockState(), Block.UPDATE_ALL);
					catfish.setHasEggs(false);
					catfish.setLayingEggs(false);
					catfish.setInLoveTime(600);
				}

				if (catfish.isLayingEggs())
					this.actionTicks++;
			}
		}

		@Override
		protected boolean isValidTarget(LevelReader level, BlockPos pos)
		{
			return level.getBlockState(pos).is(Blocks.WATER) && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
		}

		@Override
		public void stop()
		{
			super.stop();
			this.actionTicks = 0;
		}
	}
}
