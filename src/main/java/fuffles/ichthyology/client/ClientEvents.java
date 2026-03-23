package fuffles.ichthyology.client;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.entity.model.AfricanCichlidModel;
import fuffles.ichthyology.client.entity.model.AngelfishModel;
import fuffles.ichthyology.client.entity.model.ArcherfishModel;
import fuffles.ichthyology.client.entity.model.BlindCaveTetraModel;
import fuffles.ichthyology.client.entity.model.CarpModel;
import fuffles.ichthyology.client.entity.model.CatfishBabyModel;
import fuffles.ichthyology.client.entity.model.CatfishModel;
import fuffles.ichthyology.client.entity.model.CrayfishModel;
import fuffles.ichthyology.client.entity.model.DiscusModel;
import fuffles.ichthyology.client.entity.model.FiddlerCrabModel;
import fuffles.ichthyology.client.entity.model.FlowerhornModel;
import fuffles.ichthyology.client.entity.model.GarBabyModel;
import fuffles.ichthyology.client.entity.model.GarModel;
import fuffles.ichthyology.client.entity.model.GoldfishModel;
import fuffles.ichthyology.client.entity.model.MudskipperModel;
import fuffles.ichthyology.client.entity.model.NeonTetraModel;
import fuffles.ichthyology.client.entity.model.OlmModel;
import fuffles.ichthyology.client.entity.model.PeacockBassBabyModel;
import fuffles.ichthyology.client.entity.model.PeacockBassModel;
import fuffles.ichthyology.client.entity.model.PerchModel;
import fuffles.ichthyology.client.entity.model.PiranhaModel;
import fuffles.ichthyology.client.entity.model.PlecoModel;
import fuffles.ichthyology.client.entity.model.SturgeonBabyModel;
import fuffles.ichthyology.client.entity.model.SturgeonModel;
import fuffles.ichthyology.client.entity.model.TilapiaModel;
import fuffles.ichthyology.client.entity.renderer.AfricanCichlidRenderer;
import fuffles.ichthyology.client.entity.renderer.AngelfishRenderer;
import fuffles.ichthyology.client.entity.renderer.ArcherfishRenderer;
import fuffles.ichthyology.client.entity.renderer.ArcherfishSpitRenderer;
import fuffles.ichthyology.client.entity.renderer.BlindCaveTetraRenderer;
import fuffles.ichthyology.client.entity.renderer.CarpRenderer;
import fuffles.ichthyology.client.entity.renderer.CatfishRenderer;
import fuffles.ichthyology.client.entity.renderer.CrayfishRenderer;
import fuffles.ichthyology.client.entity.renderer.DiscusRenderer;
import fuffles.ichthyology.client.entity.renderer.FiddlerCrabRenderer;
import fuffles.ichthyology.client.entity.renderer.FlowerhornRenderer;
import fuffles.ichthyology.client.entity.renderer.GarBabyRenderer;
import fuffles.ichthyology.client.entity.renderer.GarRenderer;
import fuffles.ichthyology.client.entity.renderer.GoldfishRenderer;
import fuffles.ichthyology.client.entity.renderer.MudskipperRenderer;
import fuffles.ichthyology.client.entity.renderer.NeonTetraRenderer;
import fuffles.ichthyology.client.entity.renderer.OlmRenderer;
import fuffles.ichthyology.client.entity.renderer.PeacockBassBabyRenderer;
import fuffles.ichthyology.client.entity.renderer.PeacockBassRenderer;
import fuffles.ichthyology.client.entity.renderer.PerchRenderer;
import fuffles.ichthyology.client.entity.renderer.PiranhaRenderer;
import fuffles.ichthyology.client.entity.renderer.PlecoRenderer;
import fuffles.ichthyology.client.entity.renderer.SturgeonBabyRenderer;
import fuffles.ichthyology.client.entity.renderer.SturgeonRenderer;
import fuffles.ichthyology.client.entity.renderer.TilapiaRenderer;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Ichthyology.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event) {
		setupBlockRenders();
	}
	private static ModelLayerLocation modelLayer(String id, String layer)
	{
		return new ModelLayerLocation(Ichthyology.id(id), layer);
	}

	private static ModelLayerLocation modelLayer(String id)
	{
		return ClientEvents.modelLayer(id, "main");
	}

	public static final ModelLayerLocation BLIND_CAVE_TETRA = modelLayer("blind_cave_tetra");
	public static final ModelLayerLocation GOLDFISH = modelLayer("goldfish");
	public static final ModelLayerLocation TILAPIA = modelLayer("tilapia");
	public static final ModelLayerLocation AFRICAN_CICHLID = modelLayer("african_cichlid");
	public static final ModelLayerLocation CARP = modelLayer("carp");
	public static final ModelLayerLocation PIRANHA = modelLayer("piranha");
	public static final ModelLayerLocation PERCH = modelLayer("perch");
	public static final ModelLayerLocation DISCUS = modelLayer("discus");
	public static final ModelLayerLocation ANGELFISH = modelLayer("angelfish");
	public static final ModelLayerLocation NEON_TETRA = modelLayer("neon_tetra");
	public static final ModelLayerLocation PLECO = modelLayer("pleco");
	public static final ModelLayerLocation ARCHERFISH = modelLayer("archerfish");
	public static final ModelLayerLocation MUDSKIPPER = modelLayer("mudskipper");
	public static final ModelLayerLocation CRAYFISH = modelLayer("crayfish");
	public static final ModelLayerLocation CATFISH = modelLayer("catfish");
	public static final ModelLayerLocation CATFISH_BABY = modelLayer("catfish", "baby");
	public static final ModelLayerLocation PEACOCK_BASS = modelLayer("peacock_bass");
	public static final ModelLayerLocation PEACOCK_BASS_BABY = modelLayer("peacock_bass_baby");
	public static final ModelLayerLocation GAR = modelLayer("gar");
	public static final ModelLayerLocation GAR_BABY = modelLayer("gar_baby");
	public static final ModelLayerLocation STURGEON = modelLayer("sturgeon");
	public static final ModelLayerLocation STURGEON_BABY = modelLayer("sturgeon_baby");
	public static final ModelLayerLocation FIDDLER_CRAB = modelLayer("fiddler_crab");
	public static final ModelLayerLocation OLM = modelLayer("olm");
	public static final ModelLayerLocation FLOWERHORN = modelLayer("flowerhorn");
	
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
		event.registerEntityRenderer(ModEntityTypes.ARCHERFISH, ArcherfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.ARCHERFISH_SPIT, ArcherfishSpitRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.MUDSKIPPER, MudskipperRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CRAYFISH, CrayfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CATFISH, CatfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PEACOCK_BASS, PeacockBassRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PEACOCK_BASS_BABY, PeacockBassBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.GAR, GarRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.GAR_BABY, GarBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.STURGEON, SturgeonRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.STURGEON_BABY, SturgeonBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.FIDDLER_CRAB, FiddlerCrabRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.OLM, OlmRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.FLOWERHORN, FlowerhornRenderer::new);
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
		event.registerLayerDefinition(ARCHERFISH, ArcherfishModel::createBodyLayer);
		event.registerLayerDefinition(MUDSKIPPER, MudskipperModel::createBodyLayer);
		event.registerLayerDefinition(CRAYFISH, CrayfishModel::createBodyLayer);
		event.registerLayerDefinition(CATFISH, CatfishModel::createBodyLayer);
		event.registerLayerDefinition(CATFISH_BABY, CatfishBabyModel::createBodyLayer);
		event.registerLayerDefinition(PEACOCK_BASS, PeacockBassModel::createBodyLayer);
		event.registerLayerDefinition(PEACOCK_BASS_BABY, PeacockBassBabyModel::createBodyLayer);
		event.registerLayerDefinition(GAR, GarModel::createBodyLayer);
		event.registerLayerDefinition(GAR_BABY, GarBabyModel::createBodyLayer);
		event.registerLayerDefinition(STURGEON, SturgeonModel::createBodyLayer);
		event.registerLayerDefinition(STURGEON_BABY, SturgeonBabyModel::createBodyLayer);
		event.registerLayerDefinition(FIDDLER_CRAB, FiddlerCrabModel::createBodyLayer);
		event.registerLayerDefinition(OLM, OlmModel::createBodyLayer);
		event.registerLayerDefinition(FLOWERHORN, FlowerhornModel::createBodyLayer);
	}
	
	@SuppressWarnings("deprecation")
	public static void setupBlockRenders() {
		RenderType cutoutRenderType = RenderType.cutout();
		RenderType translucentRenderType = RenderType.translucent();
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.CATFISH_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.PEACOCK_BASS_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.GAR_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.STURGEON_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.OLM_EGGS, translucentRenderType);
		ItemBlockRenderTypes.setRenderLayer(Blocks.FROGSPAWN, translucentRenderType);
	}

}
