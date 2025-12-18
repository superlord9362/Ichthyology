package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Tilapia;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class TilapiaModel extends EntityModel<Tilapia> {
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart Tail_F;
	private final ModelPart pectoral_F;
	private final ModelPart pectoral_F2;

	public TilapiaModel(ModelPart root) {
		this.body = root.getChild("body");
		this.tail = body.getChild("tail");
		this.Tail_F = tail.getChild("Tail_F");
		this.pectoral_F = body.getChild("pectoral_F");
		this.pectoral_F2 = body.getChild("pectoral_F2");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -3.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(21, 8).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition Tail_F = tail.addOrReplaceChild("Tail_F", CubeListBuilder.create().texOffs(27, 6).mirror().addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition DorsalAnal_F = body.addOrReplaceChild("DorsalAnal_F", CubeListBuilder.create().texOffs(21, -7).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pelvic = body.addOrReplaceChild("pelvic", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -1.0F));

		PartDefinition pectoral_F = body.addOrReplaceChild("pectoral_F", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition pectoral_F2 = body.addOrReplaceChild("pectoral_F2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Tilapia entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F + (-0.05F * Mth.sin(0.1F * ageInTicks));
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.body.y = 21;
		this.body.x = Mth.sin(0.15F * ageInTicks);
		this.body.zRot = f;
		this.body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.05F * Mth.sin(0.15F * ageInTicks));
		this.tail.yRot = 0.45F * Mth.sin(0.35F * ageInTicks) * 0.5F;
		this.Tail_F.yRot = 0.45F * Mth.sin(0.35F * ageInTicks);
		this.Tail_F.zRot = 0.05F * Mth.sin(0.35F * ageInTicks);
		this.pectoral_F.yRot = -0.45F * Mth.sin(0.35F * ageInTicks) - 0.9346F;
		this.pectoral_F2.yRot = 0.45F * Mth.sin(-0.35F * ageInTicks) + 0.9346F;
//		this.DorsalAnal_F.zRot = -0.1F * Mth.sin(0.15F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}