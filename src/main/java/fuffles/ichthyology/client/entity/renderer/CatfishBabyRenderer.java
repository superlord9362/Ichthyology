package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.CatfishBabyModel;
import fuffles.ichthyology.common.entity.CatfishBaby;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CatfishBabyRenderer extends MobRenderer<CatfishBaby, EntityModel<CatfishBaby>> {

	private static final ResourceLocation CATFISH = new ResourceLocation(Ichthyology.ID, "textures/entity/catfish/catfish_baby.png");

	public CatfishBabyRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new CatfishBabyModel(renderManager.bakeLayer(ClientEvents.CATFISH_BABY)), 0.25F);
	}

	public ResourceLocation getTextureLocation(CatfishBaby entity) {
		return CATFISH;
	}

}
