package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Flowerhorn;
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
public class FlowerhornModel extends EntityModel<Flowerhorn> {
	private final ModelPart Body;
	private final ModelPart Kok;
	private final ModelPart neck;
	private final ModelPart belly;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart Dorsal_f;
	private final ModelPart Anal_f;
	private final ModelPart Pelvic_f_l;
	private final ModelPart Pelvic_f_r;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;

	public FlowerhornModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Kok = this.Body.getChild("Kok");
		this.neck = this.Body.getChild("neck");
		this.belly = this.Body.getChild("belly");
		this.Tail = this.Body.getChild("Tail");
		this.Tail_f = this.Tail.getChild("Tail_f");
		this.Dorsal_f = this.Body.getChild("Dorsal_f");
		this.Anal_f = this.Body.getChild("Anal_f");
		this.Pelvic_f_l = this.Body.getChild("Pelvic_f_l");
		this.Pelvic_f_r = this.Body.getChild("Pelvic_f_r");
		this.Pectoral_f_l = this.Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = this.Body.getChild("Pectoral_f_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 1).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -5.0F));

		PartDefinition Kok = Body.addOrReplaceChild("Kok", CubeListBuilder.create().texOffs(15, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.1564F, 0.0F, 0.0F));

		PartDefinition neck = Body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.0F));

		PartDefinition belly = Body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(15, 16).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 4.0F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(23, 9).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 8.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(25, 13).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 1.0F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(19, 16).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 4.0F));

		PartDefinition Anal_f = Body.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(21, 21).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, 6.0F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(2, 20).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 6.0F, 3.0F, 0.2618F, 0.0F, 0.2618F));

		PartDefinition Pelvic_f_r = Body.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(8, 20).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 6.0F, 3.0F, 0.2618F, 0.0F, -0.2618F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(1, 4).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 2.0F, 3.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(1, 1).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 2.0F, 3.0F, 0.0F, -0.6109F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Flowerhorn entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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