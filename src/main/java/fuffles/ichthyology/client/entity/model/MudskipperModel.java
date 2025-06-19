package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Mudskipper;
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
public class MudskipperModel extends EntityModel<Mudskipper> {
	private final ModelPart Body;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart Dorsal_f_tail;
	private final ModelPart part6;
	private final ModelPart Head;
	private final ModelPart eyes;
	private final ModelPart Dorsal_f;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pelvic_f_l;
	private final ModelPart Pelvic_f_r;

	public MudskipperModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail = this.Body.getChild("Tail");
		this.Tail_f = this.Tail.getChild("Tail_f");
		this.Dorsal_f_tail = this.Tail.getChild("Dorsal_f_tail");
		this.part6 = this.Tail.getChild("part6");
		this.Head = this.Body.getChild("Head");
		this.eyes = this.Head.getChild("eyes");
		this.Dorsal_f = this.Body.getChild("Dorsal_f");
		this.Pectoral_f_l = this.Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = this.Body.getChild("Pectoral_f_r");
		this.Pelvic_f_l = this.Body.getChild("Pelvic_f_l");
		this.Pelvic_f_r = this.Body.getChild("Pelvic_f_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.7F, -3.0F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(15, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 4.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(25, 7).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 6.0F));

		PartDefinition Dorsal_f_tail = Tail.addOrReplaceChild("Dorsal_f_tail", CubeListBuilder.create().texOffs(14, 9).addBox(0.0F, -1.6F, 0.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition part6 = Tail.addOrReplaceChild("part6", CubeListBuilder.create().texOffs(17, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 1.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -1.0F, -4.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -1.0F, -0.8F));

		PartDefinition eyes = Head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(10, 2).addBox(-0.5F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, -2.0F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(14, 5).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -0.5F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(8, 17).addBox(-3.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 1.0F, -0.5F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, -0.5F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(24, 3).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition Pelvic_f_r = Body.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.3491F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Mudskipper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.Body.y = 22.7F;
		this.Body.zRot = 0;
		this.eyes.y = -1;
		if (entity.isFighting()) {
			this.Body.y = -Mth.abs(4 * Mth.cos(0.2F * ageInTicks)) + 22.7F;
		} else {
			this.Body.y = -Mth.abs(Mth.cos(1F + limbSwing * 0.3F) * 3.6F * limbSwingAmount) + 22.7F;
		}
		if (entity.isInWater()) {
			this.Body.xRot = headPitch * ((float)Math.PI / 180F);
			this.Body.yRot = netHeadYaw * ((float)Math.PI / 180F);
			this.Tail.yRot = 0.25F * Mth.sin(0.6F * ageInTicks);
			this.Tail_f.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
			this.Pectoral_f_l.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
			this.Pectoral_f_r.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
			this.Body.zRot = 0;
		} else {
			this.Head.xRot = headPitch * ((float)Math.PI / 180F);
			this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
			this.Pectoral_f_l.yRot = Mth.cos(1.0F + limbSwing * 0.6F) * 1.6F * limbSwingAmount;
			this.Pectoral_f_r.yRot = -Mth.cos(1.0F + limbSwing * 0.6F) * 1.6F * limbSwingAmount;
			this.Body.xRot = 0;
			this.Body.yRot = 0;
			this.Tail.yRot = 0;
			this.Tail_f.yRot = 0;
			if (entity.isBlinking()) {
				this.eyes.y = Mth.abs(-0.99F * Mth.sin(0.05F * ageInTicks)) - 1;
			}
		}
		if (entity.isRolling()) {
			this.Body.zRot = (4 * Mth.cos(0.05F * ageInTicks));
		} else {
			this.Body.zRot = 0;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}