package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.FlowerhornModel;
import fuffles.ichthyology.common.entity.Flowerhorn;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FlowerhornRenderer extends MobRenderer<Flowerhorn, EntityModel<Flowerhorn>> {

	private static final ResourceLocation FLOWERHORN = new ResourceLocation(Ichthyology.ID, "textures/entity/flowerhorn.png");

	public FlowerhornRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new FlowerhornModel(renderManager.bakeLayer(ClientEvents.FLOWERHORN)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Flowerhorn entity) {
		return FLOWERHORN;
	}


}
