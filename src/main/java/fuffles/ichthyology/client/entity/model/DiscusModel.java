package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Discus;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class DiscusModel extends EntityModel<Discus> {
	private final ModelPart Body;
	private final ModelPart Tail_f;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Dorsal_f;
	private final ModelPart Anal_f;

	public DiscusModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail_f = Body.getChild("Tail_f");
		this.Pectoral_f_l = Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = Body.getChild("Pectoral_f_r");
		this.Dorsal_f = Body.getChild("Dorsal_f");
		this.Anal_f = Body.getChild("Anal_f");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(13, 5).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(19, -6).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, -0.5F));

		PartDefinition Tail_f = Body.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(20, 9).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 4.0F));

		PartDefinition Pelvic_f = Body.addOrReplaceChild("Pelvic_f", CubeListBuilder.create().texOffs(14, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.5F, 0.0F, 0.6109F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(1, 3).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.5F, 0.0F, -0.6109F, 0.0F));

		PartDefinition Anal_f = Body.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(19, 2).addBox(0.0F, -1.0F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 1.5F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(Discus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F + (-0.1F * Mth.sin(0.1F * ageInTicks));
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.x = Mth.sin(0.15F * ageInTicks);
		this.Body.zRot = f;
		this.Body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.Tail_f.yRot = 0.25F * Mth.sin(0.25F * ageInTicks);
		this.Tail_f.zRot = 0.05F * Mth.sin(0.25F * ageInTicks);
		this.Anal_f.zRot = 0.05F * Mth.sin(0.25F * ageInTicks);
		this.Dorsal_f.zRot = -0.05F * Mth.sin(0.25F * ageInTicks);
		this.Pectoral_f_l.yRot = (0.525F * Mth.sin(0.15F * ageInTicks)) + 0.7854F;
		this.Pectoral_f_r.yRot = (0.525F * Mth.sin(0.15F * ageInTicks)) - 0.7854F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}