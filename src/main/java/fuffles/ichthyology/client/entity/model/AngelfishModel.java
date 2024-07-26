package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Angelfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class AngelfishModel extends EntityModel<Angelfish> {
	private final ModelPart Body;
	private final ModelPart Tail_f;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;

	public AngelfishModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail_f = Body.getChild("Tail_f");
		this.Pectoral_f_l = Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = Body.getChild("Pectoral_f_r");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 20.0F, -4.0F));

		PartDefinition Mouth = Body.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition Tail_f = Body.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 6.0F));

		PartDefinition Anal_f = Body.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(8, 6).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, 0.089F, 0.0F, 0.0F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(8, -5).addBox(0.0F, -6.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -0.1222F, 0.0F, 0.0F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(4, 8).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition pelvic_f_r = Body.addOrReplaceChild("pelvic_f_r", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 1.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(9, 16).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0F, 1.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 3.0F, 1.5F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Angelfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * ((float)Math.PI / 180F);
		this.Body.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.Tail_f.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.Pectoral_f_l.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks)) + 0.7854F;
		this.Pectoral_f_r.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks)) - 0.7854F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}