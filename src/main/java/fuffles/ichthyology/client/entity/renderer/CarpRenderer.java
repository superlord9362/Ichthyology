package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.CarpModel;
import fuffles.ichthyology.common.entity.Carp;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CarpRenderer extends MobRenderer<Carp, EntityModel<Carp>> {

	private static final ResourceLocation WILD = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/wild.png");
	private static final ResourceLocation ASAGI = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/asagi.png");
	private static final ResourceLocation HI_UTSURI = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/hi_utsuri.png");
	private static final ResourceLocation KARASUGOI = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/karasugoi.png");
	private static final ResourceLocation KI_UTSURI = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/ki_utsuri.png");
	private static final ResourceLocation KOHAKU = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/kohaku.png");
	private static final ResourceLocation KUJAKU = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/kujaku.png");
	private static final ResourceLocation ORENJI_OGON = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/orenji_ogon.png");
	private static final ResourceLocation PLATINUM_OGON = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/platinum_ogon.png");
	private static final ResourceLocation SANKE = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/sanke.png");
	private static final ResourceLocation SHIRO_UTSURI = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/shiro_utsuri.png");
	private static final ResourceLocation TANCHO = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/tancho.png");
	private static final ResourceLocation YAMABUKI_OGON = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/yamabuki_ogon.png");
	private static final ResourceLocation DRAGON = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/dragon.png");
	private static final ResourceLocation ENDER_DRAGON = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/carp/ender_dragon.png");

	public CarpRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new CarpModel(renderManager.bakeLayer(ClientEvents.CARP)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Carp entity) {
		if (entity.getColor() == 1) return ASAGI;
		if (entity.getColor() == 2) return HI_UTSURI;
		if (entity.getColor() == 3) return KARASUGOI;
		if (entity.getColor() == 4) return KI_UTSURI;
		if (entity.getColor() == 5) return KOHAKU;
		if (entity.getColor() == 6) return KUJAKU;
		if (entity.getColor() == 7) return ORENJI_OGON;
		if (entity.getColor() == 8) return PLATINUM_OGON;
		if (entity.getColor() == 9) return SANKE;
		if (entity.getColor() == 10) return SHIRO_UTSURI;
		if (entity.getColor() == 11) return TANCHO;
		if (entity.getColor() == 12) return YAMABUKI_OGON;
		if (entity.getColor() == 13) return DRAGON;
		if (entity.getColor() == 14) return ENDER_DRAGON;
		return WILD;
	}


}
