package fuffles.ichthyology.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import fuffles.ichthyology.common.entity.ArcherfishSpit;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ArcherfishSpitRenderer extends EntityRenderer<ArcherfishSpit> {
	   private static final ResourceLocation LLAMA_SPIT_LOCATION = new ResourceLocation("textures/entity/llama/spit.png");
	   private final LlamaSpitModel<ArcherfishSpit> model;

	   public ArcherfishSpitRenderer(EntityRendererProvider.Context pContext) {
	      super(pContext);
	      this.model = new LlamaSpitModel<>(pContext.bakeLayer(ModelLayers.LLAMA_SPIT));
	   }

	   public void render(ArcherfishSpit pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
	      pPoseStack.pushPose();
	      pPoseStack.translate(0.0F, 0.15F, 0.0F);
	      pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
	      pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
	      this.model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
	      VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(LLAMA_SPIT_LOCATION));
	      this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	      pPoseStack.popPose();
	      super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
	   }

	   /**
	    * Returns the location of an entity's texture.
	    */
	   public ResourceLocation getTextureLocation(ArcherfishSpit pEntity) {
	      return LLAMA_SPIT_LOCATION;
	   }
	}