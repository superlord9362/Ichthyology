package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.TilapiaModel;
import fuffles.ichthyology.common.entity.Tilapia;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TilapiaRenderer extends MobRenderer<Tilapia, EntityModel<Tilapia>> {

	private static final ResourceLocation TILAPIA = new ResourceLocation(Ichthyology.ID, "textures/entity/tilapia.png");

	public TilapiaRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new TilapiaModel(renderManager.bakeLayer(ClientEvents.TILAPIA)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Tilapia entity) {
		return TILAPIA;
	}


}
