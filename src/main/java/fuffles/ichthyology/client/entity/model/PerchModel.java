package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Perch;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class PerchModel extends EntityModel<Perch> implements ArmedModel {
	private final ModelPart Body;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart pectoral_f_r_box_r1;
	private final ModelPart pectoral_f_l_box_r1;

	public PerchModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail = Body.getChild("Tail");
		this.Tail_f = Tail.getChild("Tail_f");
		this.pectoral_f_l_box_r1 = Body.getChild("pectoral_f_l_box_r1");
		this.pectoral_f_r_box_r1 = Body.getChild("pectoral_f_r_box_r1");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(13, 0).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 5).addBox(0.0F, 2.0F, 4.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(6, 13).addBox(-1.0F, 3.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(21, 5).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -4.0F));

		PartDefinition pectoral_f_r_box_r1 = Body.addOrReplaceChild("pectoral_f_r_box_r1", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition pectoral_f_l_box_r1 = Body.addOrReplaceChild("pectoral_f_l_box_r1", CubeListBuilder.create().texOffs(0, 1).mirror().addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 2.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(21, 6).addBox(0.0F, -2.0F, 1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(20, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));
		
		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(30, -4).addBox(0.0F, -1.0F, 3.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Perch entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * ((float)Math.PI / 180F);
		this.Body.yRot = (netHeadYaw * ((float)Math.PI / 180F));
		this.Tail.yRot = 0.15F * Mth.sin(0.2F * ageInTicks);
		this.Tail_f.yRot = 0.25F * Mth.sin(0.2F * ageInTicks);
		this.pectoral_f_l_box_r1.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
		this.pectoral_f_r_box_r1.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public void translateToHand(HumanoidArm sideIn, PoseStack matrixStackIn) {
		float f = sideIn == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelrenderer = this.getArmForSide(sideIn);
		modelrenderer.x += f;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.x -= f;
		matrixStackIn.translate(0, 0.7, 0);
	}

	protected ModelPart getArmForSide(HumanoidArm side) {
		return side == HumanoidArm.LEFT ? this.Body : this.Body;
	}
}