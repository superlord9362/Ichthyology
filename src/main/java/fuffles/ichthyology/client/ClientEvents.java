package fuffles.ichthyology.client;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.entity.model.*;
import fuffles.ichthyology.client.entity.renderer.*;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Ichthyology.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
	
	public static final ModelLayerLocation BLIND_CAVE_TETRA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "blind_cave_tetra"), "blind_cave_tetra");
	public static final ModelLayerLocation GOLDFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "goldfish"), "goldfish");
	public static final ModelLayerLocation TILAPIA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "tilapia"), "tilapia");
	public static final ModelLayerLocation AFRICAN_CICHLID = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "african_cichlid"), "african_cichlid");
	public static final ModelLayerLocation CARP = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "carp"), "carp");
	public static final ModelLayerLocation PIRANHA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "piranha"), "piranha");
	public static final ModelLayerLocation PERCH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "perch"), "perch");
	public static final ModelLayerLocation DISCUS = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "discus"), "discus");
	public static final ModelLayerLocation ANGELFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "angelfish"), "angelfish");
	public static final ModelLayerLocation NEON_TETRA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "neon_tetra"), "neon_tetra");
	public static final ModelLayerLocation PLECO = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "pleco"), "pleco");
	
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.BLIND_CAVE_TETRA, BlindCaveTetraRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.GOLDFISH, GoldfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.TILAPIA, TilapiaRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.AFRICAN_CICHLID, AfricanCichlidRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CARP, CarpRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PIRANHA, PiranhaRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PERCH, PerchRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.DISCUS, DiscusRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.ANGELFISH, AngelfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.NEON_TETRA, NeonTetraRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PLECO, PlecoRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(BLIND_CAVE_TETRA, BlindCaveTetraModel::createBodyLayer);
		event.registerLayerDefinition(GOLDFISH, GoldfishModel::createBodyLayer);
		event.registerLayerDefinition(TILAPIA, TilapiaModel::createBodyLayer);
		event.registerLayerDefinition(AFRICAN_CICHLID, AfricanCichlidModel::createBodyLayer);
		event.registerLayerDefinition(CARP, CarpModel::createBodyLayer);
		event.registerLayerDefinition(PIRANHA, PiranhaModel::createBodyLayer);
		event.registerLayerDefinition(PERCH, PerchModel::createBodyLayer);
		event.registerLayerDefinition(DISCUS, DiscusModel::createBodyLayer);
		event.registerLayerDefinition(ANGELFISH, AngelfishModel::createBodyLayer);
		event.registerLayerDefinition(NEON_TETRA, NeonTetraModel::createBodyLayer);
		event.registerLayerDefinition(PLECO, PlecoModel::createBodyLayer);
	}

}
