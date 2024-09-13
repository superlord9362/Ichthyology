package fuffles.ichthyology.common.entity;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModItems;
import fuffles.ichthyology.init.ModEntityDataSerializers;
import fuffles.ichthyology.init.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.IntFunction;

public class AfricanCichlid extends AbstractAdvancedSchoolingFish
{
    private static final EntityDataAccessor<AfricanCichlid.Variant> VARIANT_ID = SynchedEntityData.defineId(AfricanCichlid.class, ModEntityDataSerializers.AFRICAN_CICHLID_VARIANT);
    public static final String TAG_VARIANT = "Variant";
    public static final String TAG_FEMALE = "IsFemale";

    protected int femaleFollowers = 0;

    public AfricanCichlid(EntityType<? extends AbstractAdvancedSchoolingFish> entityType, Level level)
    {
        super(entityType, level);
    }

    public AfricanCichlid.Variant getVariant()
    {
        return this.entityData.get(VARIANT_ID);
    }

    public void setVariant(AfricanCichlid.Variant variant)
    {
        this.entityData.set(VARIANT_ID, variant);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, AfricanCichlid.Variant.BURUNDI);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound)
    {
        super.addAdditionalSaveData(compound);
        this.getVariant().writeVariant(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound)
    {
        super.readAdditionalSaveData(compound);
        AfricanCichlid.Variant variant = AfricanCichlid.Variant.readVariant(compound);
        if (variant != null)
            this.setVariant(variant);
    }

    public void saveToBucketTag(@NotNull ItemStack stack)
    {
        super.saveToBucketTag(stack);
        this.getVariant().writeVariant(stack.getOrCreateTag());
    }

    public void loadFromBucketTag(@NotNull CompoundTag tag)
    {
        super.loadFromBucketTag(tag);
        AfricanCichlid.Variant variant = AfricanCichlid.Variant.readVariant(tag);
        if (variant != null)
            this.setVariant(variant);
        else
            this.setVariant(AfricanCichlid.Variant.byId(this.level().getRandom().nextInt(AfricanCichlid.Variant.VALUES.length)));
    }

    public static int createTagToFishType(CompoundTag tag)
    {
        AfricanCichlid.Variant variant = AfricanCichlid.Variant.readVariant(tag);
        return variant != null ? variant.getId() : 0;
    }

    public static boolean createBucketDescriptor(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced, @NotNull Style defaultStyle)
    {
        if (stack.hasTag())
        {
            AfricanCichlid.Variant variant = AfricanCichlid.Variant.readVariant(stack.getTag());
            if (variant != null)
            {
                tooltipComponents.add(Component.translatable(variant.getVariantDescriptionId()).withStyle(defaultStyle));
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaxSchoolSize()
    {
        int i = 1;
        for (AfricanCichlid.Variant variant : AfricanCichlid.Variant.VALUES)
        {
            if (this.getVariant().getName().equals(variant.getName()))
            {
                i += variant.getMaxAllowedInSchool();
            }
        }
        return i;
    }

    @Override
    public boolean canBeFollowedBy(AbstractAdvancedSchoolingFish other)
    {
        if (!super.canBeFollowedBy(other))
            return false;
        if (!(other instanceof AfricanCichlid otherCichlid) || !this.getVariant().getName().equals(otherCichlid.getVariant().getName()))
            return false;
        if (this.getVariant().hasDimorphism())
        {
            if (this.getVariant().isFemale())
                return false;
            if (otherCichlid.getVariant().isFemale())
                return this.femaleFollowers < otherCichlid.getVariant().getMaxAllowedInSchool();
            else
                return this.followers < this.getVariant().getMaxAllowedInSchool();
        }
        return true;
    }

    @Override
    public AbstractAdvancedSchoolingFish startFollowing(AbstractAdvancedSchoolingFish leader)
    {
        super.startFollowing(leader);
        if (this.leader instanceof AfricanCichlid cichlidLeader && cichlidLeader.getVariant().hasDimorphism() && this.getVariant().isFemale())
        {
            cichlidLeader.followers--;
            cichlidLeader.femaleFollowers++;
        }
        return this.leader;
    }

    @Override
    public void stopFollowing()
    {
        if (this.leader instanceof AfricanCichlid cichlidLeader && cichlidLeader.getVariant().hasDimorphism() && this.getVariant().isFemale())
        {
            cichlidLeader.followers++;
            cichlidLeader.femaleFollowers--;
        }
        super.stopFollowing();
    }

    @Override
    public int getFollowers()
    {
        return super.getFollowers() + Math.max(0, this.femaleFollowers);
    }

    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 10));
    }

    @Override
    public void finalizeSpawnWithLeader(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @NotNull LeaderSpawnGroupData<?> leaderSpawnData, @Nullable CompoundTag tag)
    {
        if (leaderSpawnData.leader() instanceof AfricanCichlid cichlidLeader && cichlidLeader.canBeFollowedBy(this))
        {
            super.finalizeSpawnWithLeader(level, difficulty, spawnReason, leaderSpawnData, tag);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag)
    {
        if (reason == MobSpawnType.BUCKET)
        {
            return spawnData;
        }
        else
        {
            this.setVariant(AfricanCichlid.Variant.byId(level.getRandom().nextInt(AfricanCichlid.Variant.VALUES.length)));
            /*this.setVariant(AfricanCichlidVariant.byId(level.getRandom().nextInt(AfricanCichlidVariant.VALUES.length)));
            if (this.getVariant().hasGenderDimorphism())
            {
                if (reason == MobSpawnType.NATURAL)
                    this.setFemale(level.getRandom().nextInt(7) > 1);
                else
                    this.setFemale(level.getRandom().nextBoolean());
            }*/
            return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
        }
    }

    @NotNull
    @Override
    public ItemStack getBucketItemStack()
    {
        return new ItemStack(ModItems.AFRICAN_CICHLID_BUCKET);
    }

    /*-F: Technically unused?
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.AFRICAN_CICHLID_AMBIENT;
    }
    */

    @Override
    protected SoundEvent getDeathSound()
    {
        return ModSoundEvents.AFRICAN_CICHLID_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource)
    {
        return ModSoundEvents.AFRICAN_CICHLID_HURT;
    }

    @NotNull
    @Override
    protected SoundEvent getFlopSound() {
        return ModSoundEvents.AFRICAN_CICHLID_FLOP;
    }

    public static enum Variant
    {
        BURUNDI("burundi", null, 9, Ichthyology.id("textures/entity/cichlid/burundi.png")),
        SAULOSI_FEMALE("saulosi", true, 5, Ichthyology.id("textures/entity/cichlid/saulosi_f.png")),
        SAULOSI_MALE("saulosi", false, 1, Ichthyology.id("textures/entity/cichlid/saulosi_m.png")),
        KASANGA("kasanga", null, 7, Ichthyology.id("textures/entity/cichlid/kasanga.png"));

        public static final AfricanCichlid.Variant[] VALUES = values();
        private static final IntFunction<AfricanCichlid.Variant> BY_ID = ByIdMap.continuous(AfricanCichlid.Variant::getId, VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);

        private final String name;
        private final Boolean female;
        private final int schoolAllowed;
        private final ResourceLocation texture;

        private Variant(String name, Boolean female, int schoolAllowed, ResourceLocation texture)
        {
            this.name = name;
            this.female = female;
            this.schoolAllowed = schoolAllowed;
            this.texture = texture;
        }

        public int getId() {
            return this.ordinal();
        }

        public String getName()
        {
            return this.name;
        }

        public boolean hasDimorphism()
        {
            return this.female != null;
        }

        public boolean isFemale()
        {
            return this.female;
        }

        public int getMaxAllowedInSchool()
        {
            return this.schoolAllowed;
        }

        @OnlyIn(Dist.CLIENT)
        public ResourceLocation getTexture()
        {
            return this.texture;
        }

        public String getVariantDescriptionId()
        {
            return "entity." + Ichthyology.ID + ".african_cichlid.variant." +  this.getName() + (this.hasDimorphism() ? (this.isFemale() ? ".female" : ".male") : "");
        }

        public void writeVariant(CompoundTag tag)
        {
            tag.putString(TAG_VARIANT, this.getName());
            if (this.hasDimorphism())
                tag.putBoolean(TAG_FEMALE, this.isFemale());
        }

        public static AfricanCichlid.Variant byId(int i)
        {
            return BY_ID.apply(i);
        }

        @Nullable
        public static AfricanCichlid.Variant byData(String name, boolean female)
        {
            for (AfricanCichlid.Variant variant : VALUES)
            {
                if (variant.getName().equals(name) && (!variant.hasDimorphism() || variant.isFemale() == female))
                    return variant;
            }
            return null;
        }

        @Nullable
        public static AfricanCichlid.Variant readVariant(CompoundTag tag)
        {
            return byData(tag.getString(TAG_VARIANT), tag.getBoolean(TAG_FEMALE));
        }
    }
}
