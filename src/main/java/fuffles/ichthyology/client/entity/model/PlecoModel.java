package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Pleco;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class PlecoModel extends EntityModel<Pleco> {
	private final ModelPart Body;
	private final ModelPart Hump_tail, Tail;
	private final ModelPart Tail_f;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Head;
	private final ModelPart Sucker;

	public PlecoModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Hump_tail = Body.getChild("Hump_tail");
		this.Tail = Hump_tail.getChild("Tail");
		this.Tail_f = Tail.getChild("Tail_f");
		this.Pectoral_f_l = Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = Body.getChild("Pectoral_f_r");
		this.Head = Body.getChild("Head");
		this.Sucker = Head.getChild("Sucker");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -5.1F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

		PartDefinition Sucker = Head.addOrReplaceChild("Sucker", CubeListBuilder.create().texOffs(23, 5).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, -0.5F));

		PartDefinition Hump_tail = Body.addOrReplaceChild("Hump_tail", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -2.0F));

		PartDefinition Tail = Hump_tail.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 24).addBox(-0.6F, -1.0F, 0.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 10.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(10, 23).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition Tail_dorsal_f = Tail.addOrReplaceChild("Tail_dorsal_f", CubeListBuilder.create().texOffs(18, 22).addBox(0.0F, -1.0F, -1.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 1.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(-3, 16).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 3.0F, 0.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(-3, 12).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, 0.0F));

		PartDefinition Pelvic_f_r = Body.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(13, 15).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 3.0F, 5.1F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(13, 12).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, 5.1F));

		PartDefinition Dorasl_f = Body.addOrReplaceChild("Dorasl_f", CubeListBuilder.create().texOffs(19, 7).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Pleco entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		if (entity.isPanicing()) {
		}
		this.Body.xRot = headPitch * ((float)Math.PI / 180F);
		this.Body.yRot = (netHeadYaw * ((float)Math.PI / 180F));
		this.Tail.yRot = 0.15F * Mth.sin(0.2F * ageInTicks);
		this.Tail_f.yRot = 0.25F * Mth.sin(0.2F * ageInTicks);
		this.Sucker.xRot = -Mth.abs(0.15F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f_l.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f_r.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}