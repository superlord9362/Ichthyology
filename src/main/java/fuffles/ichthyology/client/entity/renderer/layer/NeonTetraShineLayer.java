package fuffles.ichthyology.client.entity.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.entity.model.NeonTetraModel;
import fuffles.ichthyology.common.entity.NeonTetra;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NeonTetraShineLayer extends EyesLayer<NeonTetra, EntityModel<NeonTetra>> {
   private static final RenderType NEON_TETRA_SHINE = RenderType.eyes(new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/neon_tetra/neon_tetra_layer.png"));
	private final RenderLayerParent<NeonTetra, EntityModel<NeonTetra>> tetraRenderer;

   public NeonTetraShineLayer(RenderLayerParent<NeonTetra, EntityModel<NeonTetra>> p_117507_) {
      super(p_117507_);
      this.tetraRenderer = p_117507_;
   }

   public RenderType renderType() {
      return NEON_TETRA_SHINE;
   }
   
   @Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, NeonTetra tetra, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
	   if (!(tetraRenderer.getModel() instanceof NeonTetraModel)) {
			return;
		}
	   long roundTime = tetra.level().getDayTime() % 24000;
		boolean night = roundTime >= 13000 && roundTime <= 22000;
		BlockPos tetraPos = tetra.blockPosition();
		int i = tetra.level().getBrightness(LightLayer.SKY, tetraPos);
		int j = tetra.level().getBrightness(LightLayer.BLOCK, tetraPos);
		int brightness;
		if (night) {
			brightness = j;
		} else {
			brightness = Math.max(i, j);
		}
		if (brightness > 7) {
			RenderType tex = null;
			tex = NEON_TETRA_SHINE;
	        if(tex != null){
	        	VertexConsumer ivertexbuilder = bufferIn.getBuffer(tex);
	            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	        }
		}
	}
}