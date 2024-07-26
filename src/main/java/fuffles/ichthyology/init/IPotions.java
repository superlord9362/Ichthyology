package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IPotions {
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Ichthyology.MOD_ID);

	public static final RegistryObject<Potion> BLINDNESS = POTIONS.register("blindness", () -> new Potion(new MobEffectInstance(MobEffects.BLINDNESS, 600)));
	public static final RegistryObject<Potion> BLINDNESS_LONG = POTIONS.register("blindness_long", () -> new Potion(new MobEffectInstance(MobEffects.BLINDNESS, 900)));
	
	public static void brewingRecipes() {
		PotionBrewing.addMix(Potions.AWKWARD, IItems.BLIND_CAVE_TETRA.get(), BLINDNESS.get());
		PotionBrewing.addMix(BLINDNESS.get(), Items.REDSTONE, BLINDNESS_LONG.get());
	}
	
}
