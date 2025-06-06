package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ArcherfishSpit extends Projectile {
	
	public ArcherfishSpit(EntityType<? extends ArcherfishSpit> entityType, Level level) {
		super(entityType, level);
	}

	public ArcherfishSpit(Level pLevel, Archerfish pSpitter) {
		this(ModEntityTypes.ARCHERFISH_SPIT, pLevel);
		this.setOwner(pSpitter);
		this.setPos(pSpitter.getX() - (double)(pSpitter.getBbWidth() + 1.0F) * 0.5D * (double)Mth.sin(pSpitter.yBodyRot * ((float)Math.PI / 180F)), pSpitter.getEyeY() - (double)0.1F, pSpitter.getZ() + (double)(pSpitter.getBbWidth() + 1.0F) * 0.5D * (double)Mth.cos(pSpitter.yBodyRot * ((float)Math.PI / 180F)));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@SuppressWarnings("deprecation")
	public void tick() {
		super.tick();
		Vec3 vec3 = this.getDeltaMovement();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
			this.onHit(hitresult);
		double d0 = this.getX() + vec3.x;
		double d1 = this.getY() + vec3.y;
		double d2 = this.getZ() + vec3.z;
		this.updateRotation();
		if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir) && this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::liquid)) {
			this.discard();
		} else {
			this.setDeltaMovement(vec3.scale((double)0.99F));
			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-0.06F, 0.0D));
			}

			this.setPos(d0, d1, d2);
		}
//		if (this.tickCount % 5 == 0) {
			this.level().addParticle(ParticleTypes.FALLING_WATER, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
//		}
	}

	/**
	 * Called when the arrow hits an entity
	 */
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = this.getOwner();
		if (entity instanceof LivingEntity livingentity) {
			if (livingentity.getMobType() == MobType.ARTHROPOD) {
				pResult.getEntity().hurt(this.damageSources().mobProjectile(this, livingentity), 3.0F);
			} else pResult.getEntity().hurt(this.damageSources().mobProjectile(this, livingentity), 1.0F);
		}

	}

	protected void onHitBlock(BlockHitResult pResult) {
		if (this.level().getBlockState(pResult.getBlockPos()).getBlock() != Blocks.WATER) {
			super.onHitBlock(pResult);
		}

	}

	protected void defineSynchedData() {
	}

	public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
		super.recreateFromPacket(pPacket);
		double d0 = pPacket.getXa();
		double d1 = pPacket.getYa();
		double d2 = pPacket.getZa();

		for(int i = 0; i < 7; ++i) {
			double d3 = 0.4D + 0.1D * (double)i;
			this.level().addParticle(ParticleTypes.FALLING_WATER, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
		}

		this.setDeltaMovement(d0, d1, d2);
	}
}
