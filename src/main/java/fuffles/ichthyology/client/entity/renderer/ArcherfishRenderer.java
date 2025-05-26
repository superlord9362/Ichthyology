package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.ArcherfishModel;
import fuffles.ichthyology.common.entity.Archerfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArcherfishRenderer extends MobRenderer<Archerfish, EntityModel<Archerfish>> {

	private static final ResourceLocation ARCHERFISH = new ResourceLocation(Ichthyology.ID, "textures/entity/archerfish.png");

	public ArcherfishRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ArcherfishModel(renderManager.bakeLayer(ClientEvents.ARCHERFISH)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Archerfish entity) {
		return ARCHERFISH;
	}


}