package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Goldfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class GoldfishModel extends EntityModel<Goldfish> {
	private final ModelPart Body;
	private final ModelPart Tail_f_1;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Tail_f_split_l;
	private final ModelPart Tail_f_split_r;

	public GoldfishModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail_f_1 = Body.getChild("Tail_f_1");
		this.Pectoral_f_l = Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = Body.getChild("Pectoral_f_r");
		this.Tail_f_split_l = Body.getChild("Tail_f_split_l");
		this.Tail_f_split_r = Body.getChild("Tail_f_split_r");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, -5.0F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 2.0F));

		PartDefinition Pelvicl_f = Body.addOrReplaceChild("Pelvicl_f", CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 1.5F, 0.5236F, 0.0F, 0.0391F));

		PartDefinition Tail_f_1 = Body.addOrReplaceChild("Tail_f_1", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition Tail_f_split_l = Body.addOrReplaceChild("Tail_f_split_l", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition Tail_f_split_r = Body.addOrReplaceChild("Tail_f_split_r", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition Anal_f = Body.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(6, 9).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 4.0F));

		PartDefinition Anal_f_double = Body.addOrReplaceChild("Anal_f_double", CubeListBuilder.create().texOffs(10, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 4.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(1, 4).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 2.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(1, 2).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 2.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition Telescope_eyes = Body.addOrReplaceChild("Telescope_eyes", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -0.5F));

		return LayerDefinition.create(meshdefinition, 16, 32);
	}

	@Override
	public void setupAnim(Goldfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * ((float)Math.PI / 180F);
		this.Body.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.Tail_f_1.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.Tail_f_split_l.yRot = 0.45F * Mth.sin(0.6F * ageInTicks) - 0.2618F;
		this.Tail_f_split_r.yRot = 0.45F * Mth.sin(0.6F * ageInTicks) + 0.2618F;
		this.Pectoral_f_l.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks)) + 1.2F;
		this.Pectoral_f_r.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks)) - 1.2F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}