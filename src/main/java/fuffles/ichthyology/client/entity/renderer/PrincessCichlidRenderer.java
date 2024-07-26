package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.AfricanCichlidModel;
import fuffles.ichthyology.common.entity.PrincessCichlid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PrincessCichlidRenderer extends MobRenderer<PrincessCichlid, AfricanCichlidModel<PrincessCichlid>> {

	private static final ResourceLocation PRINCESS_CICHLID = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/cichlid/burundi.png");

	public PrincessCichlidRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new AfricanCichlidModel<>(renderManager.bakeLayer(ClientEvents.AFRICAN_CICHLID)), 0.25F);
	}

	public ResourceLocation getTextureLocation(PrincessCichlid entity) {
		return PRINCESS_CICHLID;
	}


}
