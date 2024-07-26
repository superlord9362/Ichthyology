package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ITabs {
public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ichthyology.MOD_ID);
	
	public static final RegistryObject<CreativeModeTab> ITEM_GROUP = REGISTER.register("ichthyology_item_group", () -> CreativeModeTab.builder().icon(() -> new ItemStack(IItems.BLIND_CAVE_TETRA_BUCKET.get()))
			.title(Component.translatable("itemGroup.ichthyology_item_group"))
			.displayItems((pParameters, pOutput) -> {
				for (var block : IItems.REGISTER.getEntries()) {
					pOutput.accept(block.get());
				}
			}).build());
	
	public static final RegistryObject<CreativeModeTab> SPAWN_EGG_GROUP = REGISTER.register("ichthyology_spawn_item_group", () -> CreativeModeTab.builder().icon(() -> new ItemStack(IItems.BLIND_CAVE_TETRA_SPAWN_EGG.get()))
			.title(Component.translatable("itemGroup.ichthyology_spawn_item_group"))
			.displayItems((pParameters, pOutput) -> {
				for (var block : IItems.SPAWN_EGGS.getEntries()) {
					pOutput.accept(block.get());
				}
			}).build());
}
