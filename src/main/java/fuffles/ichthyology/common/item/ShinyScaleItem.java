package fuffles.ichthyology.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ShinyScaleItem extends Item {

	public ShinyScaleItem(Properties pProperties) {
		super(pProperties);
	}

	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		if (pEntity instanceof Player player) {
			if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == this || player.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == this) {
				player.addEffect(new MobEffectInstance(MobEffects.LUCK));
			}
		}
	}

}
