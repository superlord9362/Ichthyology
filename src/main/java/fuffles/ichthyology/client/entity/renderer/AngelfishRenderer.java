package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.AngelfishModel;
import fuffles.ichthyology.common.entity.Angelfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AngelfishRenderer extends MobRenderer<Angelfish, EntityModel<Angelfish>> {

	private static final ResourceLocation ANGELFISH = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/angelfish.png");

	public AngelfishRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new AngelfishModel(renderManager.bakeLayer(ClientEvents.ANGELFISH)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Angelfish entity) {
		return ANGELFISH;
	}


}
