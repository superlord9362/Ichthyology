package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.CatfishBabyModel;
import fuffles.ichthyology.client.entity.model.CatfishModel;
import fuffles.ichthyology.common.entity.Catfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CatfishRenderer extends AbstractAgeableFishRenderer<Catfish, EntityModel<Catfish>>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Ichthyology.ID, "textures/entity/catfish/catfish.png");
	private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(Ichthyology.ID, "textures/entity/catfish/catfish_baby.png");

	public CatfishRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new CatfishModel<>(renderManager.bakeLayer(ClientEvents.CATFISH)), 0.375F, new CatfishBabyModel<>(renderManager.bakeLayer(ClientEvents.CATFISH_BABY)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Catfish entity)
	{
		return entity.isBaby() ? BABY_TEXTURE : TEXTURE;
	}

}
