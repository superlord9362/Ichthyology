package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.PeacockBass;
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
public class PeacockBassModel extends EntityModel<PeacockBass> {
	private final ModelPart Body;
	private final ModelPart Tail;
	private final ModelPart Tail_tip;
	private final ModelPart Tail_f;
	private final ModelPart Dorsal_f_tail;
	private final ModelPart Anal_f;
	private final ModelPart Head;
	private final ModelPart Mouth;
	private final ModelPart Jaw;
	private final ModelPart Eyes;
	private final ModelPart Dorsal_f_body;
	private final ModelPart Pelvic_f_l;
	private final ModelPart Pelvic_f_r;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;

	public PeacockBassModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail = this.Body.getChild("Tail");
		this.Tail_tip = this.Tail.getChild("Tail_tip");
		this.Tail_f = this.Tail_tip.getChild("Tail_f");
		this.Dorsal_f_tail = this.Tail.getChild("Dorsal_f_tail");
		this.Anal_f = this.Tail.getChild("Anal_f");
		this.Head = this.Body.getChild("Head");
		this.Mouth = this.Head.getChild("Mouth");
		this.Jaw = this.Mouth.getChild("Jaw");
		this.Eyes = this.Head.getChild("Eyes");
		this.Dorsal_f_body = this.Body.getChild("Dorsal_f_body");
		this.Pelvic_f_l = this.Body.getChild("Pelvic_f_l");
		this.Pelvic_f_r = this.Body.getChild("Pelvic_f_r");
		this.Pectoral_f_l = this.Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = this.Body.getChild("Pectoral_f_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, -6.0F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 12.0F));

		PartDefinition Tail_tip = Tail.addOrReplaceChild("Tail_tip", CubeListBuilder.create().texOffs(20, 28).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 6.0F));

		PartDefinition Tail_f = Tail_tip.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(34, 22).addBox(0.0F, -4.5F, 0.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition Dorsal_f_tail = Tail.addOrReplaceChild("Dorsal_f_tail", CubeListBuilder.create().texOffs(37, 12).addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 1.0F));

		PartDefinition Anal_f = Tail.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(37, 19).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 3.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(22, 0).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -3.0F, -1.0F));

		PartDefinition Mouth = Head.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(40, 0).addBox(-2.5F, -1.0F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, -3.0F, 0.5864F, 0.0F, 0.0F));

		PartDefinition Jaw = Mouth.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -5.0F, -1.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -3.0F, 0.2346F, 0.0F, 0.0F));

		PartDefinition Eyes = Head.addOrReplaceChild("Eyes", CubeListBuilder.create().texOffs(0, 5).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -4.0F));

		PartDefinition Dorsal_f_body = Body.addOrReplaceChild("Dorsal_f_body", CubeListBuilder.create().texOffs(37, 3).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 1.0F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(47, 20).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 5.0F, -1.0F));

		PartDefinition Pelvic_f_r = Body.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(54, 20).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 5.0F, -1.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(47, 32).addBox(-6.0F, -1.0F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, -1.0F, 0.0F, 1.0472F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(47, 28).addBox(0.0F, -1.0F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, -1.0F, 0.0F, -1.0472F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(PeacockBass entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float partialTick = ageInTicks - entity.tickCount;
		float attackProgress = entity.getMeleeProgress(partialTick);
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f_r.yRot = -0.45F * Mth.sin(0.15F * ageInTicks) - 0.9346F;
		this.Pectoral_f_l.yRot = 0.45F * Mth.sin(0.15F * ageInTicks) + 0.9346F;
		this.Tail.yRot = 0.225F * Mth.sin(0.2F * ageInTicks);
		this.Tail_tip.yRot = 0.1F * Mth.sin(0.2F * ageInTicks);
		this.Tail_f.yRot = 0.15F * Mth.sin(0.2F * ageInTicks);
		this.Jaw.xRot = attackProgress * (float)Math.toRadians(50) + 0.2346F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
