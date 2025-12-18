package fuffles.ichthyology.common.entity;

import java.util.function.Predicate;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Gar extends AbstractBreedableFish {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(ModItems.PLECO, ModItems.CARP, ModItems.PERCH);
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Gar.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Gar.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(Gar.class, EntityDataSerializers.INT);
	int layEggsCounter;
	private float meleeProgress = 0.0F;
	private float prevMeleeProgress = 0.0F;

	public static final Predicate<LivingEntity> SMALL_ENTITY = (entity) -> {
		return entity.getBbWidth() <= 0.25F && entity.getType() != ModEntityTypes.GAR && entity.getType() != ModEntityTypes.GAR_BABY;
	};

	public Gar(EntityType<? extends Gar> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, SMALL_ENTITY));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(0, new GarBreedGoal(this, 1));
		this.goalSelector.addGoal(1, new GarLayEggsGoal(this, 1));
		this.goalSelector.addGoal(0, new OcelotAttackGoal(this));
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
		return FOOD_ITEMS.test(pStack);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAS_EGGS, false);
		this.entityData.define(LAYING_EGGS, false);
		this.entityData.define(ATTACK_TICK, 0);
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

	public void tick() {
		super.tick();
		prevMeleeProgress = meleeProgress;
		if (this.entityData.get(ATTACK_TICK) > 0) {
			LivingEntity target = this.getTarget();
			if (this.entityData.get(ATTACK_TICK) == 1 && target != null && this.hasLineOfSight(target) && this.distanceTo(target) < 1.5F + this.getBbWidth() + target.getBbWidth()) {
				this.onAttackAnimationFinish(target);
			}
			this.entityData.set(ATTACK_TICK, this.entityData.get(ATTACK_TICK) - 1);
			if (meleeProgress < 1.0F) {
				meleeProgress = Math.min(meleeProgress + 0.2F, 1.0F);
			}
		} else {
			if (meleeProgress > 0F) {
				meleeProgress = Math.max(meleeProgress - 0.2F, 0.0F);
			}
		}
	}

	public boolean doHurtTarget(Entity entityIn) {
		this.entityData.set(ATTACK_TICK, 7);
		return true;
	}

	public float getMeleeProgress(float partialTick) {
		return prevMeleeProgress + (meleeProgress - prevMeleeProgress) * partialTick;
	}

	public boolean onAttackAnimationFinish(Entity target) {
		return target.hurt(this.damageSources().mobAttack(this), (float) ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FOLLOW_RANGE, 10).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.ARMOR, 2);
	}

	static class GarBreedGoal extends BreedFishGoal {
		private final Gar gar;

		GarBreedGoal(Gar gar, double speedModifier) {
			super(gar, speedModifier);
			this.gar = gar;
		}

		public boolean canUse() {
			return super.canUse() && !gar.hasEggs();
		}

		protected void breed() {
			ServerPlayer serverPlayer = this.fish.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null) {
				serverPlayer = this.partner.getLoveCause();
			}
			if (serverPlayer != null) {
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
			}
			this.gar.setHasEggs(true);
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

	static class GarLayEggsGoal extends MoveToBlockGoal {
		private final Gar gar;

		GarLayEggsGoal(Gar gar, double speedModifier) {
			super(gar, speedModifier, 16);
			this.gar = gar;
		}


		public double acceptedDistance() {
			return 1D;
		}

		public boolean canUse() {
			return gar.hasEggs() ? super.canUse() : false;
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && gar.hasEggs();
		}

		public void tick() {
			super.tick();
			BlockPos blockpos = gar.blockPosition();
			if (this.isReachedTarget()) {
				if (gar.layEggsCounter < 1) {
					gar.setLayingEggs(true);
				} else if (gar.layEggsCounter > this.adjustedTickDelay(200)) {
					Level level = gar.level();
					level.playSound((Player)null, blockpos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
					BlockState blockstate = ModBlocks.GAR_ROE.defaultBlockState();
					level.setBlock(blockPos, blockstate, 3);
					gar.setHasEggs(false);
					gar.setLayingEggs(false);
					gar.setInLoveTime(600);
				}
				if (gar.isLayingEggs()) {
					++gar.layEggsCounter;
				}
			}
		}

		@SuppressWarnings("deprecation")
		protected boolean isValidTarget(LevelReader level, BlockPos pos) {
			Block block = level.getBlockState(pos).getBlock();
			return block == Blocks.WATER && level.getBlockState(pos.below()).isSolid();
		}
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
