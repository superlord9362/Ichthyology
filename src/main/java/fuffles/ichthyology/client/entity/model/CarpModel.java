package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Carp;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class CarpModel extends EntityModel<Carp> {
	private final ModelPart Body;
	private final ModelPart Body_back;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;

	public CarpModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Body_back = Body.getChild("Body_back");
		this.Tail = Body_back.getChild("Tail");
		this.Tail_f = Tail.getChild("Tail_f");
		this.Pectoral_f_l = Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = Body.getChild("Pectoral_f_r");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -6.0F));

		PartDefinition Body_back = Body.addOrReplaceChild("Body_back", CubeListBuilder.create().texOffs(5, 14).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition Tail = Body_back.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(8, 24).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 5.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(22, 16).addBox(0.0F, -3.5F, 0.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 3.0F));

		PartDefinition Dorsal_f_back = Body_back.addOrReplaceChild("Dorsal_f_back", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition part8 = Body_back.addOrReplaceChild("part8", CubeListBuilder.create().texOffs(22, 15).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 2.0F));

		PartDefinition Mouth = Body.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(17, 2).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, -0.0391F));

		PartDefinition whisker = Mouth.addOrReplaceChild("whisker", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, -1.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition whisker_1 = Mouth.addOrReplaceChild("whisker_1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.0F, -1.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(26, 2).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 3.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(23, 1).mirror().addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 2.5F, 1.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(23, 4).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, 2.5F, 1.0F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(33, 1).mirror().addBox(-2.6464F, 0.3536F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 3.0F, 6.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition Pelvic_f_r = Body.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(33, 4).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Carp entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * ((float)Math.PI / 180F);
		this.Body.yRot = (netHeadYaw * ((float)Math.PI / 180F)) + (-0.15F * Mth.sin(0.2F * ageInTicks));
		this.Body_back.yRot = 0.35F * Mth.sin(0.2F * ageInTicks);
		this.Tail.yRot = 0.15F * Mth.sin(0.2F * ageInTicks);
		this.Tail_f.yRot = 0.25F * Mth.sin(0.2F * ageInTicks);
		this.Pectoral_f_l.zRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f_r.zRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
