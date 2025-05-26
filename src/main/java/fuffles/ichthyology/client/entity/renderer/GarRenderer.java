package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.GarModel;
import fuffles.ichthyology.common.entity.Gar;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GarRenderer extends MobRenderer<Gar, EntityModel<Gar>> {

	private static final ResourceLocation GAR = new ResourceLocation(Ichthyology.ID, "textures/entity/gar/gar.png");

	public GarRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new GarModel(renderManager.bakeLayer(ClientEvents.GAR)), 0.3125F);
	}

	public ResourceLocation getTextureLocation(Gar entity) {
		return GAR;
	}

}
