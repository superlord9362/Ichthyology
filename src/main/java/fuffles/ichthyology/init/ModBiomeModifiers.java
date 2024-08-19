package fuffles.ichthyology.init;

import com.mojang.serialization.Codec;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.world.IchthyologyBiomeModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBiomeModifiers
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Ichthyology.ID);
	
	public static final RegistryObject<Codec<IchthyologyBiomeModifier>> MAIN = REGISTRY.register("ichthyology_biome_modifier", () -> Codec.unit(IchthyologyBiomeModifier.INSTANCE));

	public static class Tags
	{
		public static final TagKey<Biome> SPAWNS_JAPANESE_VARIANTS = tag("spawns_japanese_variants");

		private static TagKey<Biome> tag(String path)
		{
			return TagKey.create(Registries.BIOME, Ichthyology.id(path));
		}
	}
}