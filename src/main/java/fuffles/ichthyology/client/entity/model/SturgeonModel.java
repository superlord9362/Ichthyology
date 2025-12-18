package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Sturgeon;
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
public class SturgeonModel extends EntityModel<Sturgeon> {
	private final ModelPart Body_1;
	private final ModelPart Body_2;
	private final ModelPart Tail_1;
	private final ModelPart Tail_2;
	private final ModelPart Tail_f;
	private final ModelPart Anal_f;
	private final ModelPart Dorsal_f;
	private final ModelPart Pelvic_f_r;
	private final ModelPart Pelvic_f_l;
	private final ModelPart Gills;
	private final ModelPart Head;
	private final ModelPart Snoot;
	private final ModelPart Barbels;
	private final ModelPart Mouth;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pectoral_f_l;

	public SturgeonModel(ModelPart root) {
		this.Body_1 = root.getChild("Body_1");
		this.Body_2 = this.Body_1.getChild("Body_2");
		this.Tail_1 = this.Body_2.getChild("Tail_1");
		this.Tail_2 = this.Tail_1.getChild("Tail_2");
		this.Tail_f = this.Tail_2.getChild("Tail_f");
		this.Anal_f = this.Tail_2.getChild("Anal_f");
		this.Dorsal_f = this.Tail_1.getChild("Dorsal_f");
		this.Pelvic_f_r = this.Tail_1.getChild("Pelvic_f_r");
		this.Pelvic_f_l = this.Tail_1.getChild("Pelvic_f_l");
		this.Gills = this.Body_1.getChild("Gills");
		this.Head = this.Gills.getChild("Head");
		this.Snoot = this.Head.getChild("Snoot");
		this.Barbels = this.Snoot.getChild("Barbels");
		this.Mouth = this.Head.getChild("Mouth");
		this.Pectoral_f_r = this.Body_1.getChild("Pectoral_f_r");
		this.Pectoral_f_l = this.Body_1.getChild("Pectoral_f_l");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body_1 = partdefinition.addOrReplaceChild("Body_1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 6.0F, 9.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 18.0F, 2.9F));

		PartDefinition Body_2 = Body_1.addOrReplaceChild("Body_2", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

		PartDefinition Tail_1 = Body_2.addOrReplaceChild("Tail_1", CubeListBuilder.create().texOffs(46, 0).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 1.0F, 9.0F));

		PartDefinition Tail_2 = Tail_1.addOrReplaceChild("Tail_2", CubeListBuilder.create().texOffs(46, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition Tail_f = Tail_2.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(50, 10).addBox(0.0F, -5.0F, 0.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 6.0F));

		PartDefinition Anal_f = Tail_2.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(45, 21).addBox(0.0F, -0.0076F, -0.1086F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.1F));

		PartDefinition Dorsal_f = Tail_1.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(41, 12).addBox(0.0F, -3.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition Pelvic_f_r = Tail_1.addOrReplaceChild("Pelvic_f_r", CubeListBuilder.create().texOffs(37, 13).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Pelvic_f_l = Tail_1.addOrReplaceChild("Pelvic_f_l", CubeListBuilder.create().texOffs(30, 13).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition Gills = Body_1.addOrReplaceChild("Gills", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition Head = Gills.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -4.0F));

		PartDefinition Snoot = Head.addOrReplaceChild("Snoot", CubeListBuilder.create().texOffs(13, 24).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -2.0F));

		PartDefinition Barbels = Snoot.addOrReplaceChild("Barbels", CubeListBuilder.create().texOffs(15, 16).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, -1.0F, 0.43F, 0.0F, 0.0F));

		PartDefinition Mouth = Head.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(19, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -0.4691F, 0.0F, 0.0F));

		PartDefinition Pectoral_f_r = Body_1.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(27, 22).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 0.0F, 0.0F, 0.0F, -0.6646F));

		PartDefinition Pectoral_f_l = Body_1.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(27, 27).addBox(0.0F, 0.0F, 0.0F, 5.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 0.0F, 0.0F, 0.0F, 0.5864F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Sturgeon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F + (-0.05F * Mth.sin(0.1F * ageInTicks));
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body_1.x = Mth.sin(0.15F * ageInTicks);
		this.Body_1.zRot = f;
		this.Body_1.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body_1.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.05F * Mth.sin(0.1F * ageInTicks));
		this.Pectoral_f_r.zRot = 0.1F * Mth.sin(0.05F * ageInTicks) - 0.6646F;
		this.Pectoral_f_l.zRot = 0.1F * Mth.sin(0.05F * ageInTicks) + 0.5864F;
		this.Pelvic_f_r.zRot = 0.1F * Mth.sin(0.05F * ageInTicks) + 0.7854F;
		this.Pelvic_f_l.zRot = 0.1F * Mth.sin(0.05F * ageInTicks) - 0.7854F;
		this.Barbels.xRot = 0.1F * Mth.sin(0.05F * ageInTicks) + 0.43F;
		this.Body_2.yRot = 0.225F * Mth.sin(0.1F * ageInTicks);
		this.Tail_1.yRot = 0.255F * Mth.sin(0.1F * ageInTicks);
		this.Tail_2.yRot = 0.275F * Mth.sin(0.1F * ageInTicks);
		this.Tail_f.yRot = 0.35F * Mth.sin(0.1F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}