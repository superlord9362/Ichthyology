package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import fuffles.ichthyology.common.entity.perch.Perch;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class PerchModel<T extends Perch> extends RootedModel<T> implements IHoldItemModel<T>
{
	private final ModelPart body;
	private final ModelPart rightFin;
	private final ModelPart leftFin;
	private final ModelPart tail;
	private final ModelPart tailFin;

	public PerchModel(ModelPart root)
	{
		super(root);
		this.body = root.getChild("body");
		this.rightFin = this.body.getChild("right_fin");
		this.leftFin = this.body.getChild("left_fin");
		this.tail = this.body.getChild("tail");
		this.tailFin = this.tail.getChild("tail_fin");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 4).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 3.0F, 1.0F, CubeDeformation.NONE)
				.texOffs(0, 0).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 4.0F, 8.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
		body.addOrReplaceChild("upper_fin", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, -6.0F, -2.0F, 0.0F, 2.0F, 5.0F, CubeDeformation.NONE), PartPose.ZERO);
		body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(0, -2).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(-1.0F, -2.0F, -2.0F));//, 0.0F, -0.7854F, 0.0F));
		body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(0, -2).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(1.0F, -2.0F, -2.0F));//, 0.0F, 0.7854F, 0.0F));
		body.addOrReplaceChild("lower_fins", CubeListBuilder.create().texOffs(6, 12).addBox(-1.0F, 2.5F, -5.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -2.5F, 3.0F));
		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -1.499F, -1.0F, 1.0F, 3.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -2.5F, 3.0F));
		tail.addOrReplaceChild("upper_tail_fin", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.5F, 1.0F, 0.0F, 1.0F, 2.0F, CubeDeformation.NONE), PartPose.ZERO);
		tail.addOrReplaceChild("lower_tail_fin", CubeListBuilder.create().texOffs(4, -2).addBox(0.0F, 1.5F, -1.0F, 0.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.ZERO);
		tail.addOrReplaceChild("tail_fin", CubeListBuilder.create().texOffs(20, -4).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 0.0F, 3.0F));

		return LayerDefinition.create(mesh, 32, 16);
	}

	@Override
	public void translateHeldItem(PoseStack poseStack, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch)
	{
		//-F: translate to body
		poseStack.translate(this.body.x / 16F, this.body.y / 16F, this.body.z / 16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));
		poseStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
		//-F: apply custom offset
		poseStack.translate(0F, -1F / 16F, -0.425F);
		poseStack.mulPose(Axis.YP.rotationDegrees(22.5F));
		poseStack.mulPose(Axis.XP.rotationDegrees(90F));
	}

	@Override
	public void setupAnim(Perch entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.tail.yRot = f * 0.45F * Mth.sin(0.6F * ageInTicks) * 0.5F;
		this.tailFin.yRot = f * 0.45F * Mth.sin(0.6F * ageInTicks);
		this.rightFin.yRot = -Mth.HALF_PI / 2F; // Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
		this.leftFin.yRot = Mth.HALF_PI / 2F; // -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
	}
}