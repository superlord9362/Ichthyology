package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.SturgeonBaby;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

@SuppressWarnings("unused")
public class SturgeonBabyModel extends EntityModel<SturgeonBaby> {
	private final ModelPart Body;
	private final ModelPart Body_2;
	private final ModelPart Tail2;
	private final ModelPart Tial_f;
	private final ModelPart part6;
	private final ModelPart part8;
	private final ModelPart part2;
	private final ModelPart part7;
	private final ModelPart part7_1;

	public SturgeonBabyModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Body_2 = this.Body.getChild("Body_2");
		this.Tail2 = this.Body_2.getChild("Tail2");
		this.Tial_f = this.Tail2.getChild("Tial_f");
		this.part6 = this.Body_2.getChild("part6");
		this.part8 = this.Body_2.getChild("part8");
		this.part2 = this.Body_2.getChild("part2");
		this.part7 = this.Body.getChild("part7");
		this.part7_1 = this.Body.getChild("part7_1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0001F))
		.texOffs(14, 0).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(15, 4).addBox(-1.0F, 1.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -5.0F));

		PartDefinition Body_2 = Body.addOrReplaceChild("Body_2", CubeListBuilder.create().texOffs(1, 11).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 5.0F));

		PartDefinition Tail2 = Body_2.addOrReplaceChild("Tail2", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(19, 9).addBox(0.0F, -2.0F, 0.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 4.0F));

		PartDefinition Tial_f = Tail2.addOrReplaceChild("Tial_f", CubeListBuilder.create().texOffs(23, -4).addBox(0.0F, -2.0F, -1.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition part6 = Body_2.addOrReplaceChild("part6", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 3.0F));

		PartDefinition part8 = Body_2.addOrReplaceChild("part8", CubeListBuilder.create().texOffs(21, 8).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, 3.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition part2 = Body_2.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(21, 5).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition part7 = Body.addOrReplaceChild("part7", CubeListBuilder.create().texOffs(-2, 3).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 1.0F, 1.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition part7_1 = Body.addOrReplaceChild("part7_1", CubeListBuilder.create().texOffs(-2, 0).addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.6981F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(SturgeonBaby entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.Tail2.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks);
		this.Tial_f.yRot = f * 0.15F * Mth.sin(0.2F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}