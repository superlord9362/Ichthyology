package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Catfish;
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
public class CatfishModel extends EntityModel<Catfish> {
	private final ModelPart Belly;
	private final ModelPart Tail;
	private final ModelPart Tail2;
	private final ModelPart Tail_tip;
	private final ModelPart Tail3;
	private final ModelPart Tail2_f;
	private final ModelPart Tail_f;
	private final ModelPart Head;
	private final ModelPart Jaw;
	private final ModelPart Jawwhisker;
	private final ModelPart Whisker;
	private final ModelPart Whisker_1;
	private final ModelPart mouthinside;
	private final ModelPart Pelvic_f;
	private final ModelPart Pelvic_f_1;
	private final ModelPart Dorsal_f;
	private final ModelPart Pectoral_f;
	private final ModelPart Pectoral_f_1;

	public CatfishModel(ModelPart root) {
		this.Belly = root.getChild("Belly");
		this.Tail = this.Belly.getChild("Tail");
		this.Tail2 = this.Tail.getChild("Tail2");
		this.Tail_tip = this.Tail2.getChild("Tail_tip");
		this.Tail3 = this.Tail_tip.getChild("Tail3");
		this.Tail2_f = this.Tail2.getChild("Tail2_f");
		this.Tail_f = this.Tail.getChild("Tail_f");
		this.Head = this.Belly.getChild("Head");
		this.Jaw = this.Head.getChild("Jaw");
		this.Jawwhisker = this.Jaw.getChild("Jawwhisker");
		this.Whisker = this.Head.getChild("Whisker");
		this.Whisker_1 = this.Head.getChild("Whisker_1");
		this.mouthinside = this.Head.getChild("mouthinside");
		this.Pelvic_f = this.Belly.getChild("Pelvic_f");
		this.Pelvic_f_1 = this.Belly.getChild("Pelvic_f_1");
		this.Dorsal_f = this.Belly.getChild("Dorsal_f");
		this.Pectoral_f = this.Belly.getChild("Pectoral_f");
		this.Pectoral_f_1 = this.Belly.getChild("Pectoral_f_1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Belly = partdefinition.addOrReplaceChild("Belly", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 6.0F, 15.0F, new CubeDeformation(0.002F)), PartPose.offset(0.0F, 20.0F, -6.0F));

		PartDefinition Tail = Belly.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 5.0F, 13.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 0.0F, 13.0F));

		PartDefinition Tail2 = Tail.addOrReplaceChild("Tail2", CubeListBuilder.create().texOffs(0, 39).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 12.0F));

		PartDefinition Tail_tip = Tail2.addOrReplaceChild("Tail_tip", CubeListBuilder.create().texOffs(0, 39).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 11.0F));

		PartDefinition Tail3 = Tail_tip.addOrReplaceChild("Tail3", CubeListBuilder.create().texOffs(16, 39).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Tail2_f = Tail2.addOrReplaceChild("Tail2_f", CubeListBuilder.create().texOffs(16, 30).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(16, 31).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 4.0F));

		PartDefinition Head = Belly.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(38, 4).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, 0.1173F, 0.0F, 0.0F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(36, 15).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 3.1F, 0.0F, -0.2269F, 0.0F, 0.0F));

		PartDefinition Jawwhisker = Jaw.addOrReplaceChild("Jawwhisker", CubeListBuilder.create().texOffs(1, 56).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -5.0F, 0.391F, 0.0F, 0.0F));

		PartDefinition Whisker = Head.addOrReplaceChild("Whisker", CubeListBuilder.create().texOffs(36, 1).mirror().addBox(-10.0F, 0.0F, -1.0F, 10.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.0F, -4.0F, 0.0F, 0.0F, -1.0556F));

		PartDefinition Whisker_1 = Head.addOrReplaceChild("Whisker_1", CubeListBuilder.create().texOffs(26, 1).mirror().addBox(-10.0F, 0.0F, -1.0F, 10.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 1.0F, -4.0F, 0.0F, 0.0F, -2.0721F));

		PartDefinition mouthinside = Head.addOrReplaceChild("mouthinside", CubeListBuilder.create().texOffs(40, 25).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 1.0F, -5.0F));

		PartDefinition Pelvic_f = Belly.addOrReplaceChild("Pelvic_f", CubeListBuilder.create().texOffs(5, 19).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 3.0F, 13.0F, -0.3519F, 0.0F, 0.391F));

		PartDefinition Pelvic_f_1 = Belly.addOrReplaceChild("Pelvic_f_1", CubeListBuilder.create().texOffs(5, 19).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 3.0F, 13.0F, -0.2346F, -0.2346F, -0.6646F));

		PartDefinition Dorsal_f = Belly.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(5, 25).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 7.0F));

		PartDefinition Pectoral_f = Belly.addOrReplaceChild("Pectoral_f", CubeListBuilder.create().texOffs(3, 21).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, -1.0F, 0.0F, -0.3519F, 0.0017F));

		PartDefinition Pectoral_f_1 = Belly.addOrReplaceChild("Pectoral_f_1", CubeListBuilder.create().texOffs(3, 21).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, -1.0F, 0.0F, 0.3519F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Catfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Belly.zRot = f;
		this.Belly.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Belly.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f.yRot = -0.65F * Mth.sin(0.15F * ageInTicks) - 0.9346F;
		this.Pectoral_f_1.yRot = 0.65F * Mth.sin(0.15F * ageInTicks) + 0.9346F;
		this.Tail.yRot = 0.225F * Mth.sin(0.2F * ageInTicks);
		this.Tail2.yRot = 0.1F * Mth.sin(0.2F * ageInTicks);
		this.Tail_tip.yRot = 0.15F * Mth.sin(0.2F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Belly.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
