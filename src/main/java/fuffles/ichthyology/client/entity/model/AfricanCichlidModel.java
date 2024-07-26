package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AfricanCichlidModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart Body;
	private final ModelPart pectoral_F_l;
	private final ModelPart pectoral_F_r;
	private final ModelPart tail;

	public AfricanCichlidModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.pectoral_F_l = Body.getChild("pectoral_F_l");
		this.pectoral_F_r = Body.getChild("pectoral_F_r");
		this.tail = Body.getChild("tail");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, -4.0F));

		PartDefinition Dorsal_F = Body.addOrReplaceChild("Dorsal_F", CubeListBuilder.create().texOffs(11, -2).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 2.0F));

		PartDefinition pelfic_F = Body.addOrReplaceChild("pelfic_F", CubeListBuilder.create().texOffs(6, 10).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 1.0F));

		PartDefinition anal_F = Body.addOrReplaceChild("anal_F", CubeListBuilder.create().texOffs(0, -3).addBox(0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 3.0F));

		PartDefinition tail = Body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 6.0F));

		PartDefinition pectoral_F_r = Body.addOrReplaceChild("pectoral_F_r", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 1.5F));

		PartDefinition Box_r1 = pectoral_F_r.addOrReplaceChild("Box_r1", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition pectoral_F_l = Body.addOrReplaceChild("pectoral_F_l", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, 1.5F));

		PartDefinition Box_r2 = pectoral_F_l.addOrReplaceChild("Box_r2", CubeListBuilder.create().texOffs(0, 1).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.zRot = f;
		this.Body.xRot = headPitch * ((float)Math.PI / 180F);
		this.Body.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.tail.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.pectoral_F_r.yRot = -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
		this.pectoral_F_l.yRot = Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}