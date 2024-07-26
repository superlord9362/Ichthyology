package fuffles.ichthyology.init;

import com.mojang.serialization.Codec;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.world.IchthyologyBiomeModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IBiomeModifiers {
public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Ichthyology.MOD_ID);
	
	public static final RegistryObject<Codec<IchthyologyBiomeModifier>> ICHTHYOLOGY_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("ichthyology_biome_modifier", () -> Codec.unit(IchthyologyBiomeModifier.INSTANCE));

}
