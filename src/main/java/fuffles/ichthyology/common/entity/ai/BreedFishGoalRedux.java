package fuffles.ichthyology.common.entity.ai;

import fuffles.ichthyology.common.entity.AbstractAgeableFish;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

//Shallow BreedGoal copy
public class BreedFishGoalRedux<T extends AbstractAgeableFish> extends Goal
{
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0F).ignoreLineOfSight();
    protected final T ageableFish;
    private final Class<? extends AbstractAgeableFish> partnerClass;
    protected final Level level;
    @Nullable
    protected AbstractAgeableFish partner;
    private int loveTime;
    private final double speedModifier;

    public BreedFishGoalRedux(T ageableFish, double pSpeedModifier)
    {
        this(ageableFish, pSpeedModifier, ageableFish.getClass());
    }

    public BreedFishGoalRedux(T ageableFish, double pSpeedModifier, Class<? extends AbstractAgeableFish> partnerClass)
    {
        this.ageableFish = ageableFish;
        this.level = ageableFish.level();
        this.partnerClass = partnerClass;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse()
    {
        if (!this.ageableFish.isInLove())
            return false;
        this.partner = this.getFreePartner();
        return this.partner != null;
    }

    public boolean canContinueToUse()
    {
        return this.partner != null && this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60;
    }

    public void stop()
    {
        this.partner = null;
        this.loveTime = 0;
    }

    public void tick()
    {
        this.ageableFish.getLookControl().setLookAt(this.partner, 10.0F, (float)this.ageableFish.getMaxHeadXRot());
        this.ageableFish.getNavigation().moveTo(this.partner, this.speedModifier);
        if (++this.loveTime >= this.adjustedTickDelay(60) && this.ageableFish.distanceToSqr(this.partner) < 9.0F)
            this.breed();
    }

    @Nullable
    private AbstractAgeableFish getFreePartner()
    {
        double partnerDist = Double.MAX_VALUE;
        AbstractAgeableFish partner = null;
        for (AbstractAgeableFish ageableFish : this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.ageableFish, this.ageableFish.getBoundingBox().inflate(8F)))
        {
            double dist = this.ageableFish.distanceToSqr(ageableFish);
            if (this.ageableFish.canMate(ageableFish) && dist < partnerDist)
            {
                partner = ageableFish;
                partnerDist = dist;
            }
        }
        return partner;
    }

    protected void breed()
    {
        this.ageableFish.spawnChildFromBreeding((ServerLevel)this.level, this.partner);
    }
}
