package fuffles.ichthyology.common.entity;

import java.util.EnumSet;
import java.util.List;

import fuffles.ichthyology.common.entity.ai.BreedFishGoal;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class Sturgeon extends AbstractBreedableFish {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(ModItems.GROUND_FISH, ModItems.CRAYFISH);
	private static final EntityDataAccessor<Boolean> HAS_EGGS = SynchedEntityData.defineId(Sturgeon.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAYING_EGGS = SynchedEntityData.defineId(Sturgeon.class, EntityDataSerializers.BOOLEAN);
	int layEggsCounter;

	public Sturgeon(EntityType<? extends Sturgeon> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(0, new SturgeonBreedGoal(this, 1));
		this.goalSelector.addGoal(1, new SturgeonLayEggsGoal(this, 1));
		this.goalSelector.addGoal(6, new DiggingGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Crayfish.class, true));
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

	public boolean doHurtTarget(Entity pEntity) {
		boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		if (flag) {
			this.doEnchantDamageEffects(this, pEntity);
		}
		return flag;
	}

	public boolean isFood(ItemStack pStack) {
		return FOOD_ITEMS.test(pStack);
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
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FOLLOW_RANGE, 10).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.ARMOR, 2);
	}

	static class SturgeonBreedGoal extends BreedFishGoal {
		private final Sturgeon sturgeon;

		SturgeonBreedGoal(Sturgeon sturgeon, double speedModifier) {
			super(sturgeon, speedModifier);
			this.sturgeon = sturgeon;
		}

		public boolean canUse() {
			return super.canUse() && !sturgeon.hasEggs();
		}

		protected void breed() {
			ServerPlayer serverPlayer = this.fish.getLoveCause();
			if (serverPlayer == null && this.partner.getLoveCause() != null) {
				serverPlayer = this.partner.getLoveCause();
			}
			if (serverPlayer != null) {
				serverPlayer.awardStat(Stats.ANIMALS_BRED);
			}
			this.sturgeon.setHasEggs(true);
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

	static class SturgeonLayEggsGoal extends MoveToBlockGoal {
		private final Sturgeon sturgeon;

		SturgeonLayEggsGoal(Sturgeon sturgeon, double speedModifier) {
			super(sturgeon, speedModifier, 16);
			this.sturgeon = sturgeon;
		}


		public double acceptedDistance() {
			return 1D;
		}

		public boolean canUse() {
			return sturgeon.hasEggs() ? super.canUse() : false;
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse() && sturgeon.hasEggs();
		}

		public void tick() {
			super.tick();
			BlockPos blockpos = sturgeon.blockPosition();
			if (this.isReachedTarget()) {
				if (sturgeon.layEggsCounter < 1) {
					sturgeon.setLayingEggs(true);
				} else if (sturgeon.layEggsCounter > this.adjustedTickDelay(200)) {
					Level level = sturgeon.level();
					level.playSound((Player)null, blockpos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
					BlockState blockstate = ModBlocks.STURGEON_ROE.defaultBlockState();
					level.setBlock(blockPos, blockstate, 3);
					sturgeon.setHasEggs(false);
					sturgeon.setLayingEggs(false);
					sturgeon.setInLoveTime(600);
				}
				if (sturgeon.isLayingEggs()) {
					++sturgeon.layEggsCounter;
				}
			}
		}

		@SuppressWarnings("deprecation")
		protected boolean isValidTarget(LevelReader level, BlockPos pos) {
			Block block = level.getBlockState(pos).getBlock();
			return block == Blocks.WATER && level.getBlockState(pos.below()).isSolid();
		}
	}

	static class DiggingGoal extends Goal {
		private static ResourceLocation DIGGING_LOOT;

		private final Sturgeon sturgeon;
		private int diggingTimer;
		private int digTimer2;

		public DiggingGoal(Sturgeon entity) {
			this.sturgeon = entity;
			setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
		}

		@Override
		public boolean canUse() {
			if (digTimer2 > 0) {
				--digTimer2;
				return false;
			}
			if (sturgeon.getRandom().nextInt(sturgeon.isBaby() ? 100 : 1000) != 0) {
				return false;
			} else {
				BlockPos blockpos = sturgeon.blockPosition();
				BlockState state = sturgeon.level().getBlockState(blockpos);
				if (state.is(BlockTags.DIRT) || state.is(BlockTags.SAND)) {
					return true;
				} else {
					return (sturgeon.level().getBlockState(blockpos.below()).is(BlockTags.DIRT) || sturgeon.level().getBlockState(blockpos.below()).is(BlockTags.SAND));
				}
			}
		}

		@Override
		public void start() {
			diggingTimer = 40;
			digTimer2 = 6000;
			sturgeon.level().broadcastEntityEvent(sturgeon, (byte) 10);
			sturgeon.getNavigation().stop();
		}

		@Override
		public void stop() {
			diggingTimer = 0;
		}

		@Override
		public boolean canContinueToUse() {
			return diggingTimer > 0;
		}

		@Override
		public void tick() {
			if (digTimer2 > 0) {
				--digTimer2;
			}
			if (diggingTimer > 0) {
				--diggingTimer;
			}
			if (diggingTimer == 25) {
				BlockPos blockpos = sturgeon.blockPosition();
				BlockPos blockpos1 = blockpos.below();
				if (sturgeon.level().getBlockState(blockpos1).is(BlockTags.DIRT) || sturgeon.level().getBlockState(blockpos1).is(BlockTags.SAND)) {
					BlockState state = sturgeon.level().getBlockState(blockpos1);
					sturgeon.level().levelEvent(2001, blockpos1, Block.getId(state));
					MinecraftServer server = sturgeon.level().getServer();
					if (server != null) {
						if (sturgeon.random.nextInt(10) == 9) DIGGING_LOOT = BuiltInLootTables.FISHING_TREASURE;
						else DIGGING_LOOT = BuiltInLootTables.FISHING_JUNK;
						List<ItemStack> items = server.getLootData().getLootTable(DIGGING_LOOT).getRandomItems(new LootParams.Builder((ServerLevel) sturgeon.level()).create(LootContextParamSets.EMPTY));
						Containers.dropContents(sturgeon.level(), blockpos, NonNullList.of(ItemStack.EMPTY, items.toArray(new ItemStack[0])));
					}
				}
			}
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
