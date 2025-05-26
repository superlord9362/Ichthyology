package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Gar;
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
public class GarModel extends EntityModel<Gar> {
	private final ModelPart Body_1;
	private final ModelPart Body2;
	private final ModelPart Tail;
	private final ModelPart Tail_tip;
	private final ModelPart Tail_f;
	private final ModelPart Anal_f;
	private final ModelPart Dorsal_f;
	private final ModelPart Pelvic_f_r;
	private final ModelPart Pelvic_f_l;
	private final ModelPart Head;
	private final ModelPart Jaw_bottom;
	private final ModelPart Teeth_bottom;
	private final ModelPart Jaw_top;
	private final ModelPart Teeth_top;
	private final ModelPart Teeth_front;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pectoral_f_l;

	public GarModel(ModelPart root) {
		this.Body_1 = root.getChild("Body_1");
		this.Body2 = this.Body_1.getChild("Body2");
		this.Tail = this.Body2.getChild("Tail");
		this.Tail_tip = this.Tail.getChild("Tail_tip");
		this.Tail_f = this.Tail_tip.getChild("Tail_f");
		this.Anal_f = this.Tail.getChild("Anal_f");
		this.Dorsal_f = this.Tail.getChild("Dorsal_f");
		this.Pelvic_f_r = this.Body2.getChild("Pelvic_f_r");
		this.Pelvic_f_l = this.Body2.getChild("Pelvic_f_l");
		this.Head = this.Body_1.getChild("Head");
		this.Jaw_bottom = this.Head.getChild("Jaw_bottom");
		this.Teeth_bottom = this.Jaw_bottom.getChild("Teeth_bottom");
		this.Jaw_top = this.Head.getChild("Jaw_top");
		this.Teeth_top = this.Jaw_top.getChild("Teeth_top");
		this.Teeth_front = this.Jaw_top.getChild("Teeth_front");
		this.Pectoral_f_r = this.Body_1.getChild("Pectoral_f_r");
		this.Pectoral_f_l = this.Body_1.getChild("Pectoral_f_l");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body_1 = partdefinition.addOrReplaceChild("Body_1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.002F)), PartPose.offset(0.0F, 19.0F, -8.0F));

		PartDefinition Body2 = Body_1.addOrReplaceChild("Body2", CubeListBuilder.create().texOffs(26, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition Tail = Body2.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(2, 13).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition Tail_tip = Tail.addOrReplaceChild("Tail_tip", CubeListBuilder.create().texOffs(52, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 7.0F));

		PartDefinition Tail_f = Tail_tip.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(53, 3).addBox(0.0F, -2.0F, 0.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 2.0F));

		PartDefinition Anal_f = Tail.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(45, -1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 3.0F));

		PartDefinition Dorsal_f = Tail.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(45, -5).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition Pelvic_f_r = Body2.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(-1, 1).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 5.0F, 0.0F, 0.0F, 0.5236F, -0.4363F));

		PartDefinition Pelvic_f_l = Body2.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(-1, 4).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 5.0F, 0.0F, 0.0F, -0.5236F, 0.4363F));

		PartDefinition Head = Body_1.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(25, 13).addBox(-2.5F, -1.0F, -4.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));

		PartDefinition Jaw_bottom = Head.addOrReplaceChild("Jaw_bottom", CubeListBuilder.create().texOffs(20, 21).addBox(-2.0F, 0.0F, -6.5F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.002F)), PartPose.offset(0.0F, 0.5F, -4.1F));

		PartDefinition Teeth_bottom = Jaw_bottom.addOrReplaceChild("Teeth_bottom", CubeListBuilder.create().texOffs(18, 37).addBox(-2.0F, -1.4F, -4.5F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Jaw_top = Head.addOrReplaceChild("Jaw_top", CubeListBuilder.create().texOffs(36, 13).addBox(-2.0F, 0.0F, -7.0F, 4.0F, 1.0F, 8.0F, new CubeDeformation(0.003F)), PartPose.offset(0.0F, -0.5F, -4.0F));

		PartDefinition Teeth_top = Jaw_top.addOrReplaceChild("Teeth_top", CubeListBuilder.create().texOffs(0, 37).addBox(-2.0F, -0.6F, -4.4F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Teeth_front = Jaw_top.addOrReplaceChild("Teeth_front", CubeListBuilder.create().texOffs(42, 28).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.7F, -6.9F, 0.0F, 0.0F, -0.7854F));

		PartDefinition Pectoral_f_r = Body_1.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(17, 4).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

		PartDefinition Pectoral_f_l = Body_1.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(17, 1).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

		return LayerDefinition.create(meshdefinition, 64, 48);
	}

	@Override
	public void setupAnim(Gar entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float partialTick = ageInTicks - entity.tickCount;
		float attackProgress = entity.getMeleeProgress(partialTick);
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body_1.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body_1.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks)) + (Mth.cos(1.0F + limbSwing * 0.1F) * 0.2F * limbSwingAmount);
		this.Body2.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks) + (Mth.cos(1.0F + limbSwing * 0.1F) * 0.2F * limbSwingAmount);
		this.Tail.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks) + (Mth.cos(1.0F + limbSwing * 0.1F) * 0.2F * limbSwingAmount);
		this.Tail_tip.yRot = f * 0.1F * Mth.sin(0.2F * ageInTicks) + (Mth.cos(1.0F + limbSwing * 0.1F) * 0.2F * limbSwingAmount);
		this.Tail_f.yRot = f * 0.15F * Mth.sin(0.2F * ageInTicks) + (Mth.cos(1.0F + limbSwing * 0.1F) * 0.2F * limbSwingAmount);
		this.Jaw_bottom.xRot = attackProgress * (float)Math.toRadians(50);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
