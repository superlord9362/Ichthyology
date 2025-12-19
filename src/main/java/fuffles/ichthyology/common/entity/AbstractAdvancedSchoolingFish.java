package fuffles.ichthyology.common.entity;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.mojang.datafixers.DataFixUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public abstract class AbstractAdvancedSchoolingFish extends AbstractModFish
{
    @Nullable
    protected AbstractAdvancedSchoolingFish leader;
    protected int followers = 0;

    public AbstractAdvancedSchoolingFish(EntityType<? extends AbstractAdvancedSchoolingFish> entityType, Level level)
    {
        super(entityType, level);
    }

    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(5, new AbstractAdvancedSchoolingFish.AdvancedFollowFlockLeaderGoal(this));
    }

    public int getMaxSpawnClusterSize()
    {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize()
    {
        return super.getMaxSpawnClusterSize();
    }

    @Override
    protected boolean canRandomSwim()
    {
        return !this.isFollower();
    }

    public boolean isFollower()
    {
        return this.leader != null && this.leader.isAlive();
    }

    public AbstractAdvancedSchoolingFish startFollowing(AbstractAdvancedSchoolingFish leader)
    {
        this.leader = leader;
        this.leader.followers++;
        return leader;
    }

    public void stopFollowing()
    {
        if (this.leader != null)
        {
            this.leader.followers--;
            this.leader = null;
        }
    }

    public int getFollowers()
    {
        return Math.max(0, this.followers);
    }

    public boolean canBeFollowedBy(AbstractAdvancedSchoolingFish potentialFollower)
    {
        return potentialFollower != this && !this.isFollower() && this.getFollowers() < this.getMaxSchoolSize() - 1;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.hasFollowers() && this.level().getRandom().nextInt(200) == 1)
        {
            List<? extends AbstractFish> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (list.size() <= 0)
            {
                this.followers = 0;
            }
        }
    }

    public boolean hasFollowers()
    {
        return this.getFollowers() > 0;
    }

    public boolean inRangeOfLeader()
    {
        return this.distanceToSqr(this.leader) <= 121.0D;
    }

    public void pathToLeader()
    {
        if (this.isFollower())
        {
            this.getNavigation().moveTo(this.leader, 1.2D);
        }
    }

    public void addFollowers(Stream<? extends AbstractAdvancedSchoolingFish> followers)
    {
        followers.limit(this.getMaxSchoolSize() - 1 - this.getFollowers()).filter(entity -> entity != this).forEach(follower -> follower.startFollowing(this));
    }

    public void finalizeSpawnWithLeader(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @NotNull LeaderSpawnGroupData<?> leaderSpawnData, @Nullable CompoundTag tag)
    {
        if (leaderSpawnData.leader instanceof AbstractAdvancedSchoolingFish leader)
        {
            this.startFollowing(leader);
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag)
    {
        super.finalizeSpawn(level, difficulty, spawnReason, spawnData, tag);
        if (spawnData == null)
            spawnData = new AbstractAdvancedSchoolingFish.LeaderSpawnGroupData<>(this);
        else if (spawnData instanceof LeaderSpawnGroupData<?> leaderSpawnData)
            this.finalizeSpawnWithLeader(level, difficulty, spawnReason, leaderSpawnData, tag);

        return spawnData;
    }

    public record LeaderSpawnGroupData<T extends LivingEntity>(T leader) implements SpawnGroupData { }

    public static class AdvancedFollowFlockLeaderGoal extends Goal
    {
        private static final int INTERVAL_TICKS = 200;

        private final AbstractAdvancedSchoolingFish mob;
        private int timeToRecalcPath;
        private int nextStartTick;

        public AdvancedFollowFlockLeaderGoal(AbstractAdvancedSchoolingFish entity)
        {
            this.mob = entity;
            this.nextStartTick = this.nextStartTick(entity);
        }

        protected int nextStartTick(AbstractAdvancedSchoolingFish entity)
        {
            return reducedTickDelay(INTERVAL_TICKS + entity.getRandom().nextInt(INTERVAL_TICKS) % 20);
        }

        public boolean canUse()
        {
            if (this.mob.hasFollowers())
            {
                return false;
            }
            else if (this.mob.isFollower())
            {
                return true;
            }
            else if (this.nextStartTick > 0)
            {
                --this.nextStartTick;
                return false;
            }
            else
            {
                this.nextStartTick = this.nextStartTick(this.mob);
                List<? extends AbstractAdvancedSchoolingFish> candidates = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8D, 8D, 8D), entity -> entity != this.mob && !entity.isFollower());
                AbstractAdvancedSchoolingFish leader = DataFixUtils.orElse(candidates.stream().findAny(), this.mob);
                leader.addFollowers(candidates.stream().filter(leader::canBeFollowedBy));
                return this.mob.isFollower();
            }
        }

        public boolean canContinueToUse()
        {
            return this.mob.isFollower() && this.mob.inRangeOfLeader();
        }

        public void start()
        {
            this.timeToRecalcPath = 0;
        }

        public void stop()
        {
            this.mob.stopFollowing();
        }

        public void tick()
        {
            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.mob.pathToLeader();
            }
        }
    }
}
