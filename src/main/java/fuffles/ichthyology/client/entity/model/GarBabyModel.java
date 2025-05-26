package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.GarBaby;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

@SuppressWarnings("unused")
public class GarBabyModel extends EntityModel<GarBaby> {
	private final ModelPart Body_1;
	private final ModelPart Body_2;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;

	public GarBabyModel(ModelPart root) {
		this.Body_1 = root.getChild("Body_1");
		this.Body_2 = this.Body_1.getChild("Body_2");
		this.Tail = this.Body_2.getChild("Tail");
		this.Tail_f = this.Tail.getChild("Tail_f");
		this.Pectoral_f_l = this.Body_1.getChild("Pectoral_f_l");
		this.Pectoral_f_r = this.Body_1.getChild("Pectoral_f_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body_1 = partdefinition.addOrReplaceChild("Body_1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.002F))
		.texOffs(11, 0).addBox(-1.0F, -0.7F, -6.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -3.0F));

		PartDefinition Body_2 = Body_1.addOrReplaceChild("Body_2", CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.001F))
		.texOffs(28, -2).addBox(0.0F, -2.0F, 3.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 4.0F));

		PartDefinition cube_r1 = Body_2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(25, 1).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 0.0F, 0.0F, 0.4363F, -0.7854F));

		PartDefinition cube_r2 = Body_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(25, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.0F, 0.0F, 0.0F, -0.4363F, 0.7854F));

		PartDefinition Tail = Body_2.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(19, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(26, 4).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 1.0F, 1.0F));

		PartDefinition Pectoral_f_l = Body_1.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

		PartDefinition Pectoral_f_r = Body_1.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(24, 4).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.4363F, -0.3927F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(GarBaby entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body_1.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body_1.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.Body_2.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks);
		this.Tail.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks);
		this.Tail_f.yRot = f * 0.15F * Mth.sin(0.2F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
