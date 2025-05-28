package fuffles.ichthyology.common.item;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import fuffles.ichthyology.common.entity.FiddlerCrab;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;

public class CrabItem extends Item {

	private static final UUID REACH_UUID = UUID.fromString("fd9f3033-f10e-45ef-8960-5275c1fb5c74");

	public CrabItem(Properties pProperties) {
		super(pProperties);
	}

	@SuppressWarnings("deprecation")
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		Multimap<Attribute, AttributeModifier> defaultModifiers;
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(REACH_UUID, "Tool modifier", 2.5D, AttributeModifier.Operation.ADDITION));
	    defaultModifiers = builder.build();
		return (pEquipmentSlot == EquipmentSlot.OFFHAND || pEquipmentSlot == EquipmentSlot.MAINHAND) ? defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}
	
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		HitResult raytraceresult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
		if (raytraceresult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemstack);
		} else if (!(world instanceof ServerLevel)) {
			return InteractionResultHolder.success(itemstack);
		} else {
			BlockHitResult blockraytraceresult = (BlockHitResult)raytraceresult;
			BlockPos blockpos = blockraytraceresult.getBlockPos();
			if (world.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
				FiddlerCrab fiddlerCrab = new FiddlerCrab(ModEntityTypes.FIDDLER_CRAB, world);
				fiddlerCrab.setPos(raytraceresult.getLocation());
				fiddlerCrab.setFromBucket(true);
				if (itemstack.hasCustomHoverName()) fiddlerCrab.setCustomName(itemstack.getHoverName());
				world.addFreshEntity(fiddlerCrab);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.consume(itemstack);	
			} else return InteractionResultHolder.fail(itemstack);
		}
	}


}
