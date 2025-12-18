package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.PeacockBassBaby;
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
public class PeacockBassBabyModel extends EntityModel<PeacockBassBaby> {
	private final ModelPart Body;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart pelvic_F;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pectoral_f_l;

	public PeacockBassBabyModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail = this.Body.getChild("Tail");
		this.Tail_f = this.Tail.getChild("Tail_f");
		this.pelvic_F = this.Body.getChild("pelvic_F");
		this.Pectoral_f_r = this.Body.getChild("Pectoral_f_r");
		this.Pectoral_f_l = this.Body.getChild("Pectoral_f_l");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(19, 1).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, -2).addBox(0.0F, 1.0F, 5.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 3).addBox(0.0F, -3.0F, 1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, -3.0F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(12, 1).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(30, 9).addBox(0.0F, -1.1F, 0.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.9F, 6.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 3.0F));

		PartDefinition pelvic_F = Body.addOrReplaceChild("pelvic_F", CubeListBuilder.create().texOffs(28, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 1.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(26, 10).addBox(0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.5F, 1.0F, 0.0F, -1.0472F, 0.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(26, 11).addBox(-2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, 1.0F, 0.0F, 1.0472F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(PeacockBassBaby entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F + (-0.05F * Mth.sin(0.1F * ageInTicks));
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.05F * Mth.sin(0.2F * ageInTicks));
		this.Pectoral_f_r.yRot = -0.45F * Mth.sin(0.25F * ageInTicks) - 0.9346F;
		this.Pectoral_f_l.yRot = 0.45F * Mth.sin(-0.25F * ageInTicks) + 0.9346F;
		this.Tail.yRot = 0.225F * Mth.sin(0.3F * ageInTicks);
		this.Tail_f.yRot = 0.275F * Mth.sin(0.3F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
