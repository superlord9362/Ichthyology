package fuffles.ichthyology.common.item;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.Carp;
import fuffles.ichthyology.common.entity.Goldfish;
import fuffles.ichthyology.common.entity.SaulosiCichlid;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class IBucketItem extends BucketItem {
	
	private final boolean hasTooltip;
	
	public IBucketItem(Supplier<? extends EntityType<?>> entityType, Supplier<? extends Fluid> fluid, Properties builder) {
		this(entityType, fluid, builder, true);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Ichthyology.CALLBACKS.add(() -> ItemProperties.register(this, new ResourceLocation(Ichthyology.MOD_ID, "color"), (stack, world, player, i) -> stack.hasTag() ? stack.getTag().getInt("Color") : 0)));
	}
	
	public IBucketItem(Supplier<? extends EntityType<?>> entityType, Supplier<? extends Fluid> fluid, Properties builder, boolean hasToolTip) {
		super(fluid, builder);
		this.hasTooltip = hasToolTip;
		this.entityTypeSupplier = entityType;
	}
	
	public void checkExtraContent(@Nullable Player player, Level world, ItemStack stack, BlockPos pos) {
		if (world instanceof ServerLevel) {
			this.spawn((ServerLevel) world, stack, pos);
			world.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> toolTip, TooltipFlag flag) {
		super.appendHoverText(stack, world, toolTip, flag);
		if (hasTooltip && stack.hasTag()) {
			toolTip.add(Component.translatable(getEntityType().getDescriptionId() + "." + stack.getTag().getInt("Color")).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}
	}
	
	private void spawn(ServerLevel world, ItemStack stack, BlockPos pos) {
		Entity entity = this.entityTypeSupplier.get().spawn(world, stack, (Player)null, pos, MobSpawnType.BUCKET, true, false);	
		if (entity instanceof Goldfish goldfish && stack.hasTag()) {
			goldfish.setColor(stack.getTag().getInt("Color"));
		}
		if (entity instanceof SaulosiCichlid cichlid && stack.hasTag()) {
			cichlid.setColor(stack.getTag().getInt("Color"));
		}
		if (entity instanceof Carp carp && stack.hasTag()) {
			carp.setColor(stack.getTag().getInt("Color"));
		}
	}
	
	private final Supplier<? extends EntityType<?>> entityTypeSupplier;
	protected EntityType<?> getEntityType() {
		return entityTypeSupplier.get();
	}

}
