package fuffles.ichthyology.common.entity.ai;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;

import fuffles.ichthyology.common.entity.AbstractBreedableFish;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

public class BreedFishGoal extends Goal {
   private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
   protected final AbstractBreedableFish fish;
   private final Class<? extends AbstractBreedableFish> partnerClass;
   protected final Level level;
   @Nullable
   protected AbstractBreedableFish partner;
   private int loveTime;
   private final double speedModifier;

   public BreedFishGoal(AbstractBreedableFish pfish, double pSpeedModifier) {
      this(pfish, pSpeedModifier, pfish.getClass());
   }

   public BreedFishGoal(AbstractBreedableFish pfish, double pSpeedModifier, Class<? extends AbstractBreedableFish> pPartnerClass) {
      this.fish = pfish;
      this.level = pfish.level();
      this.partnerClass = pPartnerClass;
      this.speedModifier = pSpeedModifier;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   public boolean canUse() {
      if (!this.fish.isInLove()) {
         return false;
      } else {
         this.partner = this.getFreePartner();
         return this.partner != null;
      }
   }

   public boolean canContinueToUse() {
      return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60;
   }

   public void stop() {
      this.partner = null;
      this.loveTime = 0;
   }

   public void tick() {
      this.fish.getLookControl().setLookAt(this.partner, 10.0F, (float)this.fish.getMaxHeadXRot());
      this.fish.getNavigation().moveTo(this.partner, this.speedModifier);
      ++this.loveTime;
      if (this.loveTime >= this.adjustedTickDelay(60) && this.fish.distanceToSqr(this.partner) < 9.0D) {
      }

   }
   
   @Nullable
   private AbstractBreedableFish getFreePartner() {
      List<? extends AbstractBreedableFish> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.fish, this.fish.getBoundingBox().inflate(8.0D));
      double d0 = Double.MAX_VALUE;
      AbstractBreedableFish fish = null;

      for(AbstractBreedableFish fish1 : list) {
         if (this.fish.canMate(fish1) && this.fish.distanceToSqr(fish1) < d0) {
            fish = fish1;
            d0 = this.fish.distanceToSqr(fish1);
         }
      }

      return fish;
   }
}	