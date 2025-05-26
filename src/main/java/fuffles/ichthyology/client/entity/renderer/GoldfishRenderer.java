package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.GoldfishModel;
import fuffles.ichthyology.common.entity.Goldfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoldfishRenderer extends AbstractFishRenderer<Goldfish, EntityModel<Goldfish>> {

	public GoldfishRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new GoldfishModel(renderManager.bakeLayer(ClientEvents.GOLDFISH)), 0.2F);
	}

	public static boolean hasBifurcatedTail(Goldfish.Variant variant)
	{
		return variant == Goldfish.Variant.PEARLSCALE || variant == Goldfish.Variant.RANCHU || variant == Goldfish.Variant.TELESCOPE;
	}

	public static boolean hasDoubleLowerBackFins(Goldfish.Variant variant)
	{
		return variant == Goldfish.Variant.PEARLSCALE;
	}

	public ResourceLocation getTextureLocation(Goldfish entity)
	{
		return entity.getVariant().getTexture();
	}
}
