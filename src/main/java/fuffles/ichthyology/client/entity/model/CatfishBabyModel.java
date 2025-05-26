package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.CatfishBaby;
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
public class CatfishBabyModel extends EntityModel<CatfishBaby> {
	private final ModelPart Body;
	private final ModelPart Tail;
	private final ModelPart Tail2;
	private final ModelPart Tail_f;
	private final ModelPart Pectoral_f_l;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pelvic_f_l;
	private final ModelPart Pelvic_f_r;
	private final ModelPart Head;
	private final ModelPart whisker_l;
	private final ModelPart whisker_r;

	public CatfishBabyModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Tail = this.Body.getChild("Tail");
		this.Tail2 = this.Tail.getChild("Tail2");
		this.Tail_f = this.Tail2.getChild("Tail_f");
		this.Pectoral_f_l = this.Body.getChild("Pectoral_f_l");
		this.Pectoral_f_r = this.Body.getChild("Pectoral_f_r");
		this.Pelvic_f_l = this.Body.getChild("Pelvic_f_l");
		this.Pelvic_f_r = this.Body.getChild("Pelvic_f_r");
		this.Head = this.Body.getChild("Head");
		this.whisker_l = this.Head.getChild("whisker_l");
		this.whisker_r = this.Head.getChild("whisker_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.002F))
		.texOffs(3, 4).addBox(-0.5F, -2.0F, 3.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 22.0F, -3.0F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(13, 7).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.001F))
		.texOffs(7, 6).addBox(0.0F, 2.0F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, 6.0F));

		PartDefinition Tail2 = Tail.addOrReplaceChild("Tail2", CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(10, 10).addBox(0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));

		PartDefinition Tail_f = Tail2.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(3, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(0, -2).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -1.0F, 0.0F, 0.3491F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(0, -1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.0F, -1.0F, 0.0F, -0.3491F, 0.0F));

		PartDefinition Pelvic_f_l = Body.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.0F, 6.0F, 0.0F, 0.3491F, 0.0F));

		PartDefinition Pelvic_f_r = Body.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 6.0F, 0.0F, -0.3491F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.0F, -1.0F));

		PartDefinition whisker_l = Head.addOrReplaceChild("whisker_l", CubeListBuilder.create(), PartPose.offset(1.5F, 1.0F, -4.0F));

		PartDefinition cube_r1 = whisker_l.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(6, 10).addBox(0.0F, 0.0F, 0.0F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition whisker_r = Head.addOrReplaceChild("whisker_r", CubeListBuilder.create(), PartPose.offset(-1.5F, 1.0F, -4.0F));

		PartDefinition cube_r2 = whisker_r.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(6, 11).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(CatfishBaby entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f_r.yRot = -0.65F * Mth.sin(0.15F * ageInTicks) - 0.9346F;
		this.Pectoral_f_l.yRot = 0.65F * Mth.sin(0.15F * ageInTicks) + 0.9346F;
		this.Tail.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks);
		this.Tail2.yRot = f * 0.1F * Mth.sin(0.2F * ageInTicks);
		this.Tail_f.yRot = f * 0.15F * Mth.sin(0.2F * ageInTicks);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
