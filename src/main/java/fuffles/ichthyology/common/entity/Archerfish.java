package fuffles.ichthyology.common.entity;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.init.ModItems;
import fuffles.ichthyology.init.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Archerfish extends AbstractModFish implements RangedAttackMob {
	public static final Predicate<LivingEntity> ARTHROPOD = (p_289448_) -> {
		return p_289448_.getMobType() == MobType.ARTHROPOD;
	};

	public Archerfish(EntityType<? extends AbstractModFish> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
		this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
		this.targetSelector.addGoal(2, new ArcherfishHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<LivingEntity>(this, LivingEntity.class, 16, true, false, ARTHROPOD));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D);
	}


	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.ARCHERFISH_BUCKET);
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ARCHERFISH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource)
	{
		return ModSoundEvents.ARCHERFISH_HURT;
	}

	@NotNull
	@Override
	protected SoundEvent getFlopSound() {
		return ModSoundEvents.ARCHERFISH_FLOP;
	}

	private void spit(LivingEntity pTarget) {
		ArcherfishSpit archerfishSpit = new ArcherfishSpit(this.level(), this);
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - archerfishSpit.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2) * (double)0.2F;
		archerfishSpit.shoot(d0, d1 + d3, d2, 1.5F, 10.0F);
		this.level().addFreshEntity(archerfishSpit);
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		this.spit(pTarget);
	}

	static class ArcherfishHurtByTargetGoal extends HurtByTargetGoal {
		public ArcherfishHurtByTargetGoal(Archerfish pArcherfish) {
			super(pArcherfish);
		}
	}

}
