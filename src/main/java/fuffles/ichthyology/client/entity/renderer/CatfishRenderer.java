package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.CatfishModel;
import fuffles.ichthyology.common.entity.Catfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CatfishRenderer extends MobRenderer<Catfish, EntityModel<Catfish>> {

	private static final ResourceLocation CATFISH = new ResourceLocation(Ichthyology.ID, "textures/entity/catfish/catfish.png");

	public CatfishRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new CatfishModel(renderManager.bakeLayer(ClientEvents.CATFISH)), 0.375F);
	}

	public ResourceLocation getTextureLocation(Catfish entity) {
		return CATFISH;
	}

}
