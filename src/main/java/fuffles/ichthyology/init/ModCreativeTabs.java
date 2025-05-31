package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.AfricanCichlid;
import fuffles.ichthyology.common.entity.Carp;
import fuffles.ichthyology.common.entity.Crayfish;
import fuffles.ichthyology.common.entity.Goldfish;
import fuffles.ichthyology.common.entity.Olm;
import fuffles.ichthyology.common.entity.perch.Perch;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ichthyology.ID);

	public static final RegistryObject<CreativeModeTab> ITEM_GROUP = REGISTER.register("ichthyology_item_group", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BLIND_CAVE_TETRA_BUCKET))
			.title(Component.translatable("itemGroup.ichthyology_item_group"))
			.displayItems((params, output) -> {
				for (Item item : ModItems.ITEM_REGISTRY.getEntries()) {
					if (item == ModItems.AFRICAN_CICHLID || item == ModItems.AFRICAN_CICHLID_BUCKET)
					{
						if (item == ModItems.AFRICAN_CICHLID_BUCKET)
							output.accept(item.getDefaultInstance());
						for (AfricanCichlid.Variant variant : AfricanCichlid.Variant.VALUES)
						{
							ItemStack stack = item.getDefaultInstance();
							variant.writeVariant(stack.getOrCreateTag());
							output.accept(stack);
						}
					}
					else if (item == ModItems.CARP_BUCKET)
					{
						output.accept(item.getDefaultInstance());
						for (Carp.Variant variant : Carp.Variant.NON_SPECIAL)
						{
							ItemStack stack = item.getDefaultInstance();
							variant.writeVariant(stack.getOrCreateTag());
							output.accept(stack);
						}
					}
					else if (item == ModItems.GOLDFISH_BUCKET)
					{
						output.accept(item.getDefaultInstance());
						for (Goldfish.Variant variant : Goldfish.Variant.VALUES)
						{
							ItemStack stack = item.getDefaultInstance();
							variant.writeVariant(stack.getOrCreateTag());
							output.accept(stack);
						}
					}
					else if (item == ModItems.CRAYFISH || item == ModItems.CRAYFISH_BUCKET) {
						if (item == ModItems.CRAYFISH_BUCKET) output.accept(item.getDefaultInstance());
						for (Crayfish.Variant variant : Crayfish.Variant.VALUES)
						{
							ItemStack stack = item.getDefaultInstance();
							variant.writeVariant(stack.getOrCreateTag());
							output.accept(stack);
						}
					}
					else if (item == ModItems.PERCH || item == ModItems.PERCH_BUCKET) {
						if (item == ModItems.PERCH_BUCKET) output.accept(item.getDefaultInstance());
						for (Perch.Variant variant : Perch.Variant.VALUES) {
							ItemStack stack = item.getDefaultInstance();
							variant.writeVariant(stack.getOrCreateTag());
							output.accept(stack);
						}
					}
					else if (item == ModItems.OLM_BUCKET)
					{
						output.accept(item.getDefaultInstance());
						for (Olm.Variant variant : Olm.Variant.VALUES)
						{
							ItemStack stack = item.getDefaultInstance();
							variant.writeVariant(stack.getOrCreateTag());
							output.accept(stack);
						}
					}
					else
					{
						output.accept(item);
					}
				}
			}).build());
	
	public static final RegistryObject<CreativeModeTab> SPAWN_EGG_GROUP = REGISTER.register("ichthyology_spawn_item_group", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BLIND_CAVE_TETRA_SPAWN_EGG))
			.title(Component.translatable("itemGroup.ichthyology_spawn_item_group"))
			.displayItems((pParameters, pOutput) -> {
				for (var block : ModItems.SPAWN_EGG_REGISTRY.getEntries()) {
					pOutput.accept(block);
				}
			}).build());
}