package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Archerfish;
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
public class ArcherfishModel extends EntityModel<Archerfish> {
	private final ModelPart body;
	private final ModelPart snoot;
	private final ModelPart mouth;
	private final ModelPart tail_base;
	private final ModelPart tail_f;
	private final ModelPart dorsal_f;
	private final ModelPart pectoral_f_l;
	private final ModelPart pectoral_f_r;
	private final ModelPart Pelvic_f;
	private final ModelPart Pelvic_f2;

	public ArcherfishModel(ModelPart root) {
		this.body = root.getChild("body");
		this.snoot = this.body.getChild("snoot");
		this.mouth = this.snoot.getChild("mouth");
		this.tail_base = this.body.getChild("tail_base");
		this.tail_f = this.tail_base.getChild("tail_f");
		this.dorsal_f = this.body.getChild("dorsal_f");
		this.pectoral_f_l = this.body.getChild("pectoral_f_l");
		this.pectoral_f_r = this.body.getChild("pectoral_f_r");
		this.Pelvic_f = this.body.getChild("Pelvic_f");
		this.Pelvic_f2 = this.body.getChild("Pelvic_f2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0001F)), PartPose.offset(0.0F, 21.0F, -1.0F));

		PartDefinition snoot = body.addOrReplaceChild("snoot", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0001F)), PartPose.offset(0.0F, 1.0F, -3.0F));

		PartDefinition mouth = snoot.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.48F, 0.0F, 0.0F));

		PartDefinition cube_r1 = mouth.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(27, 1).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition tail_base = body.addOrReplaceChild("tail_base", CubeListBuilder.create().texOffs(19, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

		PartDefinition tail_f = tail_base.addOrReplaceChild("tail_f", CubeListBuilder.create().texOffs(19, 2).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition dorsal_f = body.addOrReplaceChild("dorsal_f", CubeListBuilder.create().texOffs(12, -3).addBox(0.0F, -2.0F, 0.0F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition pectoral_f_l = body.addOrReplaceChild("pectoral_f_l", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(-1.5F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 1.0F, 0.0F, 0.0F, 0.821F, 0.0F));

		PartDefinition pectoral_f_r = body.addOrReplaceChild("pectoral_f_r", CubeListBuilder.create().texOffs(0, 5).addBox(-0.5F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 0.0F, 0.0F, -1.0167F, 0.0F));

		PartDefinition Pelvic_f = body.addOrReplaceChild("Pelvic_f", CubeListBuilder.create().texOffs(1, 12).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -0.1F, 0.5236F, 0.0F, 0.3491F));

		PartDefinition Pelvic_f2 = body.addOrReplaceChild("Pelvic_f2", CubeListBuilder.create().texOffs(3, 12).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -0.1F, 0.5236F, 0.0F, -0.3491F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(Archerfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.body.zRot = f;
		this.body.xRot = headPitch * ((float)Math.PI / 180F);
		this.body.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.tail_f.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.pectoral_f_l.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks)) + 0.821F;
		this.pectoral_f_r.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks)) - 0.821F;

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
