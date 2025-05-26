package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.MudskipperModel;
import fuffles.ichthyology.common.entity.Mudskipper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MudskipperRenderer extends MobRenderer<Mudskipper, EntityModel<Mudskipper>> {

	private static final ResourceLocation MUDSKIPPER = new ResourceLocation(Ichthyology.ID, "textures/entity/mudskipper/mudskipper.png");

	public MudskipperRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MudskipperModel(renderManager.bakeLayer(ClientEvents.MUDSKIPPER)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Mudskipper entity) {
		return MUDSKIPPER;
	}


}
