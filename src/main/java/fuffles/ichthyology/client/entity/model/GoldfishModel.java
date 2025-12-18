package fuffles.ichthyology.client.entity.model;

import fuffles.ichthyology.client.entity.renderer.GoldfishRenderer;
import fuffles.ichthyology.common.entity.Goldfish;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class GoldfishModel extends RootedModel<Goldfish> {
	private final ModelPart body;
	private final ModelPart backLowerFin;
	private final ModelPart backLowerFins;
	private final ModelPart tailRight;
	private final ModelPart tailLeft;
	private final ModelPart rightFin;
	private final ModelPart leftFin;

	public GoldfishModel(ModelPart root)
	{
		super(root);
		this.body = root.getChild("body");
		this.backLowerFin = this.body.getChild("back_lower_fin");
		this.backLowerFins = this.body.getChild("back_lower_fins");
		this.tailRight = this.body.getChild("tail_right");
		this.tailLeft = this.tailRight.getChild("tail_left");
		this.rightFin = this.body.getChild("right_fin");
		this.leftFin = this.body.getChild("left_fin");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -3.0F, -4.0F, 2.0F, 3.0F, 6.0F, CubeDeformation.NONE)
				.texOffs(10, 2).addBox(1.0F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
				.texOffs(10, 0).addBox(-2.0F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
				.texOffs(0, 5).addBox(0.0F, -5.0F, -1.0F, 0.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
		body.addOrReplaceChild("front_lower_fins", CubeListBuilder.create().texOffs(0, 3).addBox(-0.5F, 1.0F, 0.0F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, Mth.HALF_PI / 4F, 0.0F, 0.0F));
		body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, 0.0F, Mth.HALF_PI / 2F, 0.0F));
		body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, 0.0F, -1.0F, 0.0F, -Mth.HALF_PI / 2F, 0.0F));
		body.addOrReplaceChild("back_lower_fin", CubeListBuilder.create().texOffs(2, -2).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 0.0F, 1.0F));
		body.addOrReplaceChild("back_lower_fins", CubeListBuilder.create()
				.texOffs(2, -2).addBox(-0.5F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, CubeDeformation.NONE)
				.texOffs(2, 0).addBox(0.5F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 0.0F, 1.0F));
		PartDefinition tail_right = body.addOrReplaceChild("tail_right", CubeListBuilder.create().texOffs(6, 7).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 5.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.5F, 2.0F));
		tail_right.addOrReplaceChild("tail_left", CubeListBuilder.create().texOffs(8, 5).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, Mth.HALF_PI / 3F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	public void prepareMobModel(Goldfish entity, float limbSwing, float limbSwingAmount, float partialTick)
	{
		this.tailLeft.visible = GoldfishRenderer.hasBifurcatedTail(entity.getVariant());
		boolean doubleLowerBackFins = GoldfishRenderer.hasDoubleLowerBackFins(entity.getVariant());
		this.backLowerFins.visible = doubleLowerBackFins;
		this.backLowerFin.visible = !doubleLowerBackFins;
	}

	@Override
	public void setupAnim(Goldfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float f = 0F + (-0.1F * Mth.sin(0.2F * ageInTicks));
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.body.x = Mth.sin(0.2F * ageInTicks);
		this.body.zRot = f;
		this.body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.body.yRot = (netHeadYaw * Mth.DEG_TO_RAD) + (-0.15F * Mth.sin(0.15F * ageInTicks));
		this.tailRight.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.leftFin.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.leftFin.yRot = (-0.325F * Mth.sin(0.45F * ageInTicks)) - 0.7854F;
		this.rightFin.yRot = -(0.325F * Mth.sin(0.45F * ageInTicks)) + 0.7854F;
	}
}