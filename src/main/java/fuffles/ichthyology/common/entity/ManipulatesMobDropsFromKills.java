package fuffles.ichthyology.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Collection;

public interface ManipulatesMobDropsFromKills
{
    /**
     *
     * @return false prevents the drops from actually dropping
     */
    public abstract boolean manipulateDrops(LivingEntity victim, Collection<ItemEntity> drops, DamageSource source, int lootingLevel);
}
