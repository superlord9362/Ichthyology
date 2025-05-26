package fuffles.ichthyology.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AfricanCichlidModel<T extends Entity> extends RootedModel<T> {
	private final ModelPart body;
	private final ModelPart rightFin;
	private final ModelPart leftFin;
	private final ModelPart tailFin;

	public AfricanCichlidModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.rightFin = this.body.getChild("right_fin");
		this.leftFin = this.body.getChild("left_fin");
		this.tailFin = this.body.getChild("tail_fin");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -3.5F, 2.0F, 3.0F, 7.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
		body.addOrReplaceChild("upper_fin", CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, -4.0F, -1.5F, 0.0F, 1.0F, 6.0F, CubeDeformation.NONE), PartPose.ZERO);
		body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(0, -1).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(-1.0F, -1.5F, -1.0F));
		body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(0, -1).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(1.0F, -1.5F, -1.0F));
		body.addOrReplaceChild("lower_fins", CubeListBuilder.create().texOffs(6, 10).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.ZERO);
		body.addOrReplaceChild("lower_fin", CubeListBuilder.create().texOffs(0, -3).addBox(0.0F, 0.0F, 0.5F, 0.0F, 1.0F, 3.0F, CubeDeformation.NONE), PartPose.ZERO);
		body.addOrReplaceChild("tail_fin", CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.5F, 3.5F));

		return LayerDefinition.create(mesh, 32, 16);
	}


	@SuppressWarnings("unused")
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.tailFin.yRot = 0.45F * Mth.sin(0.6F * ageInTicks);
		this.rightFin.yRot = -Mth.HALF_PI / 2F; // -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
		this.leftFin.yRot = Mth.HALF_PI / 2F; // Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
	}
}