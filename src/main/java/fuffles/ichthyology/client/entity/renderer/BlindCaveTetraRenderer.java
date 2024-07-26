package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.BlindCaveTetraModel;
import fuffles.ichthyology.common.entity.BlindCaveTetra;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BlindCaveTetraRenderer extends MobRenderer<BlindCaveTetra, EntityModel<BlindCaveTetra>> {

	private static final ResourceLocation BLIND_CAVE_TETRA = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/blind_cave_tetra.png");

	public BlindCaveTetraRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new BlindCaveTetraModel(renderManager.bakeLayer(ClientEvents.BLIND_CAVE_TETRA)), 0.25F);
	}

	public ResourceLocation getTextureLocation(BlindCaveTetra entity) {
		return BLIND_CAVE_TETRA;
	}


}
