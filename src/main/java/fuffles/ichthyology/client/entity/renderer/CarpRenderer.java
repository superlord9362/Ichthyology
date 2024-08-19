package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.CarpModel;
import fuffles.ichthyology.common.entity.Carp;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CarpRenderer extends AbstractFishRenderer<Carp, EntityModel<Carp>>
{
	public CarpRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new CarpModel(renderManager.bakeLayer(ClientEvents.CARP)), 0.25F);
	}

	@Override
	protected float getYRotation(Carp entity, float ageInTicks)
	{
		float f = entity.isInWater() ? 1F : 1.5F;
		return f * -0.15F * Mth.sin(0.2F * ageInTicks) * Mth.RAD_TO_DEG;
	}

	public ResourceLocation getTextureLocation(Carp entity)
	{
		return entity.getVariant().getTexture();
	}
}
