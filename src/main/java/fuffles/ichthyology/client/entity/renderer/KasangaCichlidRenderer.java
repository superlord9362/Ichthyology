package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.AfricanCichlidModel;
import fuffles.ichthyology.common.entity.KasangaCichlid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KasangaCichlidRenderer extends MobRenderer<KasangaCichlid, AfricanCichlidModel<KasangaCichlid>> {

	private static final ResourceLocation KASANGA_CICHLID = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/cichlid/kasanga.png");

	public KasangaCichlidRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new AfricanCichlidModel<>(renderManager.bakeLayer(ClientEvents.AFRICAN_CICHLID)), 0.25F);
	}

	public ResourceLocation getTextureLocation(KasangaCichlid entity) {
		return KASANGA_CICHLID;
	}


}
