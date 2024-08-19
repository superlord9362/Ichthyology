package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Carp;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class CarpModel extends RootedModel<Carp>
{
	private final ModelPart body;
	private final ModelPart rightWhisker;
	private final ModelPart leftWhisker;
	private final ModelPart frontRightFin;
	private final ModelPart frontLeftFin;
	private final ModelPart backRightFin;
	private final ModelPart backLeftFin;
	private final ModelPart bodyTail;
	private final ModelPart tail;
	private final ModelPart tailFin;

	public CarpModel(ModelPart root)
	{
		super(root);
		this.body = root.getChild("body");
		this.rightWhisker = this.body.getChild("right_whisker");
		this.leftWhisker = this.body.getChild("left_whisker");
		this.frontRightFin = this.body.getChild("front_right_fin");
		this.frontLeftFin = this.body.getChild("front_left_fin");
		this.backRightFin = this.body.getChild("back_right_fin");
		this.backLeftFin = this.body.getChild("back_left_fin");
		this.bodyTail = this.body.getChild("body_tail");
		this.tail = this.bodyTail.getChild("tail");
		this.tailFin = this.tail.getChild("tail_fin");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.5F, -4.0F, -9.0F, 3.0F, 4.0F, 9.0F, CubeDeformation.NONE)
				.texOffs(10, 6).addBox(0.0F, -7.0F, -5.0F, 0.0F, 3.0F, 7.0F, CubeDeformation.NONE)
				.texOffs(0, 0).addBox(-1.0F, -3.0F, -11.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
		body.addOrReplaceChild("right_whisker", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, 0.0F, -1.0F, 0.0F, 4.0F, 2.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, -1.0F, -10.0F, 0.0F, 0.0F, Mth.HALF_PI / 6F));
		body.addOrReplaceChild("left_whisker", CubeListBuilder.create().texOffs(2, 3).addBox(0.0F, 0.0F, -1.0F, 0.0F, 4.0F, 2.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, -1.0F, -10.0F, 0.0F, 0.0F, -Mth.HALF_PI / 6F));
		body.addOrReplaceChild("front_right_fin", CubeListBuilder.create().texOffs(12, 3).addBox(-4.0F, 0.0F, -1.5F, 4.0F, 0.0F, 3.0F, CubeDeformation.NONE), PartPose.offset(-1.5F, -0.5F, -5.5F));
		body.addOrReplaceChild("front_left_fin", CubeListBuilder.create().texOffs(12, 0).addBox(0.0F, 0.0F, -1.5F, 4.0F, 0.0F, 3.0F, CubeDeformation.NONE), PartPose.offset(1.5F, -0.5F, -5.5F));
		body.addOrReplaceChild("back_right_fin", CubeListBuilder.create().texOffs(20, 3).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, 0.0F, -0.5F, 0.0F, 0.0F, -Mth.HALF_PI / 2F));
		body.addOrReplaceChild("back_left_fin", CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, 0.0F, -0.5F, 0.0F, 0.0F, Mth.HALF_PI / 2F));
		PartDefinition tail_body = body.addOrReplaceChild("body_tail", CubeListBuilder.create()
				.texOffs(0, 13).addBox(-1.0F, -1.999F, -1.0F, 2.0F, 3.998F, 6.0F, CubeDeformation.NONE)
				.texOffs(10, 9).addBox(0.0F, -4.0F, 0.0F, 0.0F, 2.0F, 7.0F, CubeDeformation.NONE)
				.texOffs(0, 10).addBox(0.0F, 2.0F, 2.0F, 0.0F, 3.0F, 3.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition tail = tail_body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 23).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 0.0F, 5.0F));
		tail.addOrReplaceChild("tail_fin", CubeListBuilder.create().texOffs(10, 16).addBox(0.0F, -3.5F, 0.0F, 0.0F, 7.0F, 7.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 0.0F, 3.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Carp entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.frontRightFin.zRot = f * -0.85F * Mth.abs(Mth.sin(0.15F * ageInTicks));
		this.frontLeftFin.zRot = f * 0.85F * Mth.abs(Mth.sin(0.15F * ageInTicks));
		this.bodyTail.yRot = f * 0.225F * Mth.sin(0.2F * ageInTicks);
		this.tail.yRot = f * 0.1F * Mth.sin(0.2F * ageInTicks);
		this.tailFin.yRot = f * 0.15F * Mth.sin(0.2F * ageInTicks);
	}
}
