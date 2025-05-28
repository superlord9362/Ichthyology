package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Olm;
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
public class OlmModel extends EntityModel<Olm> {
	private final ModelPart Body_1;
	private final ModelPart Body_2;
	private final ModelPart Body_3;
	private final ModelPart Tail;
	private final ModelPart Tail_flat;
	private final ModelPart Tail_tip;
	private final ModelPart Leg_b_r;
	private final ModelPart Leg_b_l;
	private final ModelPart Head;
	private final ModelPart Head_top;
	private final ModelPart Gills_r;
	private final ModelPart Gills_l;
	private final ModelPart Leg_f_r;
	private final ModelPart Leg_f_l;

	public OlmModel(ModelPart root) {
		this.Body_1 = root.getChild("Body_1");
		this.Body_2 = this.Body_1.getChild("Body_2");
		this.Body_3 = this.Body_2.getChild("Body_3");
		this.Tail = this.Body_3.getChild("Tail");
		this.Tail_flat = this.Tail.getChild("Tail_flat");
		this.Tail_tip = this.Tail.getChild("Tail_tip");
		this.Leg_b_r = this.Body_3.getChild("Leg_b_r");
		this.Leg_b_l = this.Body_3.getChild("Leg_b_l");
		this.Head = this.Body_1.getChild("Head");
		this.Head_top = this.Head.getChild("Head_top");
		this.Gills_r = this.Head.getChild("Gills_r");
		this.Gills_l = this.Head.getChild("Gills_l");
		this.Leg_f_r = this.Body_1.getChild("Leg_f_r");
		this.Leg_f_l = this.Body_1.getChild("Leg_f_l");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body_1 = partdefinition.addOrReplaceChild("Body_1", CubeListBuilder.create().texOffs(1, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -7.0F));

		PartDefinition Body_2 = Body_1.addOrReplaceChild("Body_2", CubeListBuilder.create().texOffs(0, 7).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

		PartDefinition Body_3 = Body_2.addOrReplaceChild("Body_3", CubeListBuilder.create().texOffs(1, 16).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));

		PartDefinition Tail = Body_3.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(2, 24).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition Tail_flat = Tail.addOrReplaceChild("Tail_flat", CubeListBuilder.create().texOffs(11, 19).addBox(0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Tail_tip = Tail.addOrReplaceChild("Tail_tip", CubeListBuilder.create().texOffs(11, 21).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition Leg_b_r = Body_3.addOrReplaceChild("Leg_b_r", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 1.0F, 3.0F));

		PartDefinition Leg_b_l = Body_3.addOrReplaceChild("Leg_b_l", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 1.0F, 3.0F));

		PartDefinition Head = Body_1.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

		PartDefinition Head_top = Head.addOrReplaceChild("Head_top", CubeListBuilder.create().texOffs(18, 7).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Gills_r = Head.addOrReplaceChild("Gills_r", CubeListBuilder.create().texOffs(16, 0).mirror().addBox(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition Gills_l = Head.addOrReplaceChild("Gills_l", CubeListBuilder.create().texOffs(12, 0).mirror().addBox(0.0F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, -1.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition Leg_f_r = Body_1.addOrReplaceChild("Leg_f_r", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 1.0F, 0.0F));

		PartDefinition Leg_f_l = Body_1.addOrReplaceChild("Leg_f_l", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 1.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	private float lerpTo(float pStart, float pEnd) {
		return this.lerpTo(0.05F, pStart, pEnd);
	}

	private float lerpTo(float pDelta, float pStart, float pEnd) {
		return Mth.rotLerp(pDelta, pStart, pEnd);
	}

	private void lerpPart(ModelPart pPart, float pXDelta, float pYDelta, float pZDelta) {
		pPart.setRotation(this.lerpTo(pPart.xRot, pXDelta), this.lerpTo(pPart.yRot, pYDelta), this.lerpTo(pPart.zRot, pZDelta));
	}

	@Override
	public void setupAnim(Olm entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = ageInTicks * 0.11F;
		float f1 = Mth.cos(f);
		float f2 = (f1 * f1 - 2.0F * f1) / 5.0F;
		float f3 = 0.7F * f1;
		this.Head.xRot = headPitch * ((float)Math.PI / 180F);
		this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
//		if (entity.isInWater()) {
//			this.Leg_b_l.yRot = Mth.cos(limbSwing * 2.6662F) * 6.4F * limbSwingAmount ;
//			this.Leg_b_l.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//			this.Leg_f_r.yRot = Mth.cos(limbSwing * 2.6662F + (float)Math.PI) * 6.4F * limbSwingAmount;
//			this.Leg_f_r.zRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
//			this.Leg_f_l.yRot = Mth.cos(limbSwing * 2.6662F + (float)Math.PI) * 6.4F * limbSwingAmount;
//			this.Leg_f_l.zRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
//			this.Leg_b_r.yRot = Mth.cos(limbSwing * 2.6662F) * 6.4F * limbSwingAmount;
//			this.Leg_b_r.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//			this.Body_2.yRot = Mth.cos(limbSwing * 0.6662F) * 5.4F * limbSwingAmount;
//			this.Body_3.yRot = -Mth.cos(limbSwing * 0.6662F) * 5.4F * limbSwingAmount;
//			this.Tail.yRot = Mth.cos(limbSwing * 0.6662F) * 5.4F * limbSwingAmount;
//		} else {
			this.Leg_b_l.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount ;
			this.Leg_b_l.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.Leg_f_r.yRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
			this.Leg_f_r.zRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
			this.Leg_f_l.yRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
			this.Leg_f_l.zRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
			this.Leg_b_r.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.Leg_b_r.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.Body_2.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.Body_3.yRot = -Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.Tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
