package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.FiddlerCrab;
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
public class FiddlerCrabModel extends EntityModel<FiddlerCrab> {
	private final ModelPart belly;
	private final ModelPart shell;
	private final ModelPart eye;
	private final ModelPart eye_1;
	private final ModelPart mouth_r;
	private final ModelPart mouth_l;
	private final ModelPart arm_small;
	private final ModelPart claw_small;
	private final ModelPart claw_small_opening;
	private final ModelPart arm_big;
	private final ModelPart claw_big;
	private final ModelPart claw_big_opening;
	private final ModelPart legg_l_1;
	private final ModelPart legg_l_2;
	private final ModelPart legg_l_3;
	private final ModelPart legg_l_4;
	private final ModelPart legg_r_1;
	private final ModelPart legg_r_2;
	private final ModelPart legg_r_3;
	private final ModelPart legg_r_4;

	public FiddlerCrabModel(ModelPart root) {
		this.belly = root.getChild("belly");
		this.shell = this.belly.getChild("shell");
		this.eye = this.shell.getChild("eye");
		this.eye_1 = this.shell.getChild("eye_1");
		this.mouth_r = this.shell.getChild("mouth_r");
		this.mouth_l = this.shell.getChild("mouth_l");
		this.arm_small = this.shell.getChild("arm_small");
		this.claw_small = this.arm_small.getChild("claw_small");
		this.claw_small_opening = this.claw_small.getChild("claw_small_opening");
		this.arm_big = this.shell.getChild("arm_big");
		this.claw_big = this.arm_big.getChild("claw_big");
		this.claw_big_opening = this.claw_big.getChild("claw_big_opening");
		this.legg_l_1 = this.belly.getChild("legg_l_1");
		this.legg_l_2 = this.belly.getChild("legg_l_2");
		this.legg_l_3 = this.belly.getChild("legg_l_3");
		this.legg_l_4 = this.belly.getChild("legg_l_4");
		this.legg_r_1 = this.belly.getChild("legg_r_1");
		this.legg_r_2 = this.belly.getChild("legg_r_2");
		this.legg_r_3 = this.belly.getChild("legg_r_3");
		this.legg_r_4 = this.belly.getChild("legg_r_4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition belly = partdefinition.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(3, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -1.0F));

		PartDefinition shell = belly.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 4).addBox(-3.5F, -2.0F, 0.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, -0.2618F, 0.0F, 0.0F));

		PartDefinition eye = shell.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition eye_1 = shell.addOrReplaceChild("eye_1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition mouth_r = shell.addOrReplaceChild("mouth_r", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -1.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.2618F, -0.0873F, 0.0F));

		PartDefinition mouth_l = shell.addOrReplaceChild("mouth_l", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(0.0F, -1.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.2618F, 0.0873F, 0.0F));

		PartDefinition arm_small = shell.addOrReplaceChild("arm_small", CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 1.0F, 0.0782F, -0.1173F, 0.5084F));

		PartDefinition claw_small = arm_small.addOrReplaceChild("claw_small", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.6474F, 0.1014F, -0.0448F));

		PartDefinition claw_small_opening = claw_small.addOrReplaceChild("claw_small_opening", CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offset(-0.5F, 1.0F, -0.5F));

		PartDefinition arm_big = shell.addOrReplaceChild("arm_big", CubeListBuilder.create().texOffs(6, 12).mirror().addBox(-0.5F, 0.0F, -2.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 1.0F, -0.1564F, 0.43F, 0.2737F));

		PartDefinition claw_big = arm_big.addOrReplaceChild("claw_big", CubeListBuilder.create().texOffs(15, 14).mirror().addBox(-0.5F, -1.0F, -1.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -2.5F, 0.0F, -0.4691F, 0.0391F));

		PartDefinition claw_big_opening = claw_big.addOrReplaceChild("claw_big_opening", CubeListBuilder.create().texOffs(15, 18).mirror().addBox(-1.0F, 0.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(2.5F, -1.0F, -0.5F));

		PartDefinition legg_l_1 = belly.addOrReplaceChild("legg_l_1", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 0.0F, 1.5F, 0.0F, -0.2618F, 0.0F));

		PartDefinition legg_l_2 = belly.addOrReplaceChild("legg_l_2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 0.0F, 2.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition legg_l_3 = belly.addOrReplaceChild("legg_l_3", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 0.0F, 2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition legg_l_4 = belly.addOrReplaceChild("legg_l_4", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 0.0F, 3.0F, 0.0F, 1.1345F, 0.0F));

		PartDefinition legg_r_1 = belly.addOrReplaceChild("legg_r_1", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 1.5F, 0.0F, 0.2618F, 0.0F));

		PartDefinition legg_r_2 = belly.addOrReplaceChild("legg_r_2", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 2.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition legg_r_3 = belly.addOrReplaceChild("legg_r_3", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition legg_r_4 = belly.addOrReplaceChild("legg_r_4", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, 0.0F, -1.1345F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(FiddlerCrab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.belly.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.eye.xRot = headPitch * ((float)Math.PI / 180F) + 0.2618F;
		this.eye_1.xRot = headPitch * ((float)Math.PI / 180F) + 0.2618F;
		this.shell.zRot = -(Mth.cos(limbSwing * 0.6662F * 2.0F) * 0.2F) * limbSwingAmount;
		this.shell.xRot = -0.2618F;
		this.legg_r_4.zRot = 0;
		this.legg_l_4.zRot = 0;
		this.legg_r_3.zRot = 0;
		this.legg_l_3.zRot = 0;
		this.legg_r_2.zRot = 0;
		this.legg_l_2.zRot = 0;
		this.legg_r_1.zRot = 0;
		this.legg_l_1.zRot = 0;
		this.legg_r_4.yRot = -1.1345F;
		this.legg_l_4.yRot = 1.1345F;
		this.legg_r_3.yRot = -0.7854F;
		this.legg_l_3.yRot = 0.7854F;
		this.legg_r_2.yRot = -0.2618F;
		this.legg_l_2.yRot = 0.2618F;
		this.legg_r_1.yRot = 0.2618F;
		this.legg_l_1.yRot = -0.2618F;
		float f3 = -(Mth.cos(limbSwing * 0.8662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
		float f4 = -(Mth.cos(limbSwing * 0.8662F * 2.0F + (float)Math.PI) * 0.4F) * limbSwingAmount;
		float f5 = -(Mth.cos(limbSwing * 0.8662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
		float f6 = -(Mth.cos(limbSwing * 0.8662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
		float f7 = Math.abs(Mth.sin(limbSwing * 0.8662F + 0.0F) * 2.4F) * limbSwingAmount;
		float f8 = Math.abs(Mth.sin(limbSwing * 0.8662F + (float)Math.PI) * 2.4F) * limbSwingAmount;
		float f9 = Math.abs(Mth.sin(limbSwing * 0.8662F + ((float)Math.PI / 2F)) * 2.4F) * limbSwingAmount;
		float f10 = Math.abs(Mth.sin(limbSwing * 0.8662F + ((float)Math.PI * 1.5F)) * 2.4F) * limbSwingAmount;
		this.legg_r_4.yRot += f3;
		this.legg_l_4.yRot += -f3;
		this.legg_r_3.yRot += f4;
		this.legg_l_3.yRot += -f4;
		this.legg_r_2.yRot += f5;
		this.legg_l_2.yRot += -f5;
		this.legg_r_1.yRot += f6;
		this.legg_l_1.yRot += -f6;
		this.legg_r_4.zRot += -f7;
		this.legg_l_4.zRot += f7;
		this.legg_r_3.zRot += -f8;
		this.legg_l_3.zRot += f8;
		this.legg_r_2.zRot += -f9;
		this.legg_l_2.zRot += f9;
		this.legg_r_1.zRot += -f10;
		this.legg_l_1.zRot += f10;
		if (entity.isWaving()) {
			this.arm_big.zRot = -Math.abs(Mth.sin(0.2F * ageInTicks) * 1.5F) + 0.2737F;
			this.claw_big.yRot = Math.abs(Mth.sin(0.2F * ageInTicks)) - 0.4691F;
			this.claw_big_opening.zRot = -Math.abs(Mth.sin(0.2F * ageInTicks) * 0.8F);
		} else {
			this.arm_big.zRot = 0.2737F;
			this.claw_big.yRot = -0.4691F;
			this.claw_big_opening.zRot = 0;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		belly.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}