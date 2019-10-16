/*package tterrag.treesimulator;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEngine extends ModelBase
{
	ModelRenderer ring2back;
	ModelRenderer ring1right;
	ModelRenderer ring1back;
	ModelRenderer ring1front;
	ModelRenderer ring2front;
	ModelRenderer ring2right;
	ModelRenderer wallright;
	ModelRenderer wallleft;
	ModelRenderer base;
	ModelRenderer wallback;
	ModelRenderer column;
	ModelRenderer wallfront;
	ModelRenderer ring3right;
	ModelRenderer ring3front;
	ModelRenderer ring3back;
	ModelRenderer brace1;
	ModelRenderer brace2;
	ModelRenderer brace3;
	ModelRenderer brace4;
	ModelRenderer ring1left;
	ModelRenderer ring2left;
	ModelRenderer ring3left;
	ModelRenderer twerkbarright;
	ModelRenderer twerkbarfront;
	ModelRenderer twerkbarleft;
	ModelRenderer twerkbarback;

	public ModelEngine()
	{
		textureWidth = 64;
		textureHeight = 64;

		ring2back = new ModelRenderer(this, 0, 35);
		ring2back.addBox(0F, 0F, 0F, 4, 1, 1);
		ring2back.setRotationPoint(6F, -7F, 10F);
		ring2back.setTextureSize(64, 64);
		ring2back.mirror = true;
		setRotation(ring2back, 0F, 0F, 0F);
		ring1right = new ModelRenderer(this, 0, 35);
		ring1right.addBox(0F, 0F, 0F, 1, 1, 6);
		ring1right.setRotationPoint(10F, -3F, 5F);
		ring1right.setTextureSize(64, 64);
		ring1right.mirror = true;
		setRotation(ring1right, 0F, 0F, 0F);
		ring1back = new ModelRenderer(this, 0, 35);
		ring1back.addBox(0F, 0F, 0F, 4, 1, 1);
		ring1back.setRotationPoint(6F, -3F, 10F);
		ring1back.setTextureSize(64, 64);
		ring1back.mirror = true;
		setRotation(ring1back, 0F, 0F, 0F);
		ring1front = new ModelRenderer(this, 0, 35);
		ring1front.addBox(0F, 0F, 0F, 4, 1, 1);
		ring1front.setRotationPoint(6F, -3F, 5F);
		ring1front.setTextureSize(64, 64);
		ring1front.mirror = true;
		setRotation(ring1front, 0F, 0F, 0F);
		ring2front = new ModelRenderer(this, 0, 35);
		ring2front.addBox(0F, 0F, 0F, 4, 1, 1);
		ring2front.setRotationPoint(6F, -7F, 5F);
		ring2front.setTextureSize(64, 64);
		ring2front.mirror = true;
		setRotation(ring2front, 0F, 0F, 0F);
		ring3front = new ModelRenderer(this, 0, 35);
		ring3front.addBox(0F, 0F, 0F, 4, 1, 1);
		ring3front.setRotationPoint(6F, -11F, 5F);
		ring3front.setTextureSize(64, 64);
		ring3front.mirror = true;
		setRotation(ring3front, 0F, 0F, 0F);
		ring2right = new ModelRenderer(this, 0, 35);
		ring2right.addBox(0F, 0F, 0F, 1, 1, 6);
		ring2right.setRotationPoint(10F, -7F, 5F);
		ring2right.setTextureSize(64, 64);
		ring2right.mirror = true;
		setRotation(ring2right, 0F, 0F, 0F);
		wallright = new ModelRenderer(this, 17, 0);
		wallright.addBox(0F, 0F, 0F, 1, 3, 14);
		wallright.setRotationPoint(14F, -4F, 1F);
		wallright.setTextureSize(64, 64);
		wallright.mirror = true;
		setRotation(wallright, 0F, 0F, 0F);
		wallleft = new ModelRenderer(this, 17, 0);
		wallleft.addBox(0F, 0F, 0F, 1, 3, 14);
		wallleft.setRotationPoint(1F, -4F, 1F);
		wallleft.setTextureSize(64, 64);
		wallleft.mirror = true;
		setRotation(wallleft, 0F, 0F, 0F);
		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 2, 16);
		base.setRotationPoint(0F, -1F, 0F);
		base.setTextureSize(64, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		wallback = new ModelRenderer(this, 19, 0);
		wallback.addBox(0F, 0F, 0F, 12, 3, 1);
		wallback.setRotationPoint(2F, -4F, 14F);
		wallback.setTextureSize(64, 64);
		wallback.mirror = true;
		setRotation(wallback, 0F, 0F, 0F);
		column = new ModelRenderer(this, 0, 17);
		column.addBox(0F, 0F, 0F, 4, 14, 4);
		column.setRotationPoint(6F, -15F, 6F);
		column.setTextureSize(64, 64);
		column.mirror = true;
		setRotation(column, 0F, 0F, 0F);
		wallfront = new ModelRenderer(this, 19, 0);
		wallfront.addBox(0F, 0F, 0F, 12, 3, 1);
		wallfront.setRotationPoint(2F, -4F, 1F);
		wallfront.setTextureSize(64, 64);
		wallfront.mirror = true;
		setRotation(wallfront, 0F, 0F, 0F);
		ring3right = new ModelRenderer(this, 0, 35);
		ring3right.addBox(0F, 0F, 0F, 1, 1, 6);
		ring3right.setRotationPoint(10F, -11F, 5F);
		ring3right.setTextureSize(64, 64);
		ring3right.mirror = true;
		setRotation(ring3right, 0F, 0F, 0F);
		ring3back = new ModelRenderer(this, 0, 35);
		ring3back.addBox(0F, 0F, 0F, 4, 1, 1);
		ring3back.setRotationPoint(6F, -11F, 10F);
		ring3back.setTextureSize(64, 64);
		ring3back.mirror = true;
		setRotation(ring3back, 0F, 0F, 0F);
		ring3back = new ModelRenderer(this, 0, 35);
		ring3back.addBox(0F, 0F, 0F, 4, 1, 1);
		ring3back.setRotationPoint(6F, -11F, 5F);
		ring3back.setTextureSize(64, 64);
		ring3back.mirror = true;
		setRotation(ring3back, 0F, 0F, 0F);
		brace1 = new ModelRenderer(this, 16, 18);
		brace1.addBox(0F, 0F, 0F, 1, 12, 1);
		brace1.setRotationPoint(2F, -13F, 2F);
		brace1.setTextureSize(64, 64);
		brace1.mirror = true;
		setRotation(brace1, 0F, 0F, 0F);
		brace2 = new ModelRenderer(this, 16, 18);
		brace2.addBox(0F, 0F, 0F, 1, 12, 1);
		brace2.setRotationPoint(2F, -13F, 13F);
		brace2.setTextureSize(64, 64);
		brace2.mirror = true;
		setRotation(brace2, 0F, 0F, 0F);
		brace3 = new ModelRenderer(this, 16, 18);
		brace3.addBox(0F, 0F, 0F, 1, 12, 1);
		brace3.setRotationPoint(13F, -13F, 2F);
		brace3.setTextureSize(64, 64);
		brace3.mirror = true;
		setRotation(brace3, 0F, 0F, 0F);
		brace4 = new ModelRenderer(this, 16, 18);
		brace4.addBox(0F, 0F, 0F, 1, 12, 1);
		brace4.setRotationPoint(13F, -13F, 13F);
		brace4.setTextureSize(64, 64);
		brace4.mirror = true;
		setRotation(brace4, 0F, 0F, 0F);
		ring1left = new ModelRenderer(this, 0, 35);
		ring1left.addBox(0F, 0F, 0F, 1, 1, 6);
		ring1left.setRotationPoint(5F, -3F, 5F);
		ring1left.setTextureSize(64, 64);
		ring1left.mirror = true;
		setRotation(ring1left, 0F, 0F, 0F);
		ring2left = new ModelRenderer(this, 0, 35);
		ring2left.addBox(0F, 0F, 0F, 1, 1, 6);
		ring2left.setRotationPoint(5F, -7F, 5F);
		ring2left.setTextureSize(64, 64);
		ring2left.mirror = true;
		setRotation(ring2left, 0F, 0F, 0F);
		ring3left = new ModelRenderer(this, 0, 35);
		ring3left.addBox(0F, 0F, 0F, 1, 1, 6);
		ring3left.setRotationPoint(5F, -11F, 5F);
		ring3left.setTextureSize(64, 64);
		ring3left.mirror = true;
		setRotation(ring3left, 0F, 0F, 0F);
		twerkbarright = new ModelRenderer(this, 20, 18);
		twerkbarright.addBox(0F, 0F, 0F, 3, 2, 12);
		twerkbarright.setRotationPoint(11F, -12F, 2F);
		twerkbarright.setTextureSize(64, 64);
		twerkbarright.mirror = true;
		setRotation(twerkbarright, 0F, 0F, 0F);
		twerkbarfront = new ModelRenderer(this, 20, 18);
		twerkbarfront.addBox(0F, 0F, 0F, 6, 2, 3);
		twerkbarfront.setRotationPoint(5F, -12F, 2F);
		twerkbarfront.setTextureSize(64, 64);
		twerkbarfront.mirror = true;
		setRotation(twerkbarfront, 0F, 0F, 0F);
		twerkbarleft = new ModelRenderer(this, 20, 18);
		twerkbarleft.addBox(0F, 0F, 0F, 3, 2, 12);
		twerkbarleft.setRotationPoint(2F, -12F, 2F);
		twerkbarleft.setTextureSize(64, 64);
		twerkbarleft.mirror = true;
		setRotation(twerkbarleft, 0F, 0F, 0F);
		twerkbarback = new ModelRenderer(this, 20, 18);
		twerkbarback.addBox(0F, 0F, 0F, 6, 2, 3);
		twerkbarback.setRotationPoint(5F, -12F, 11F);
		twerkbarback.setTextureSize(64, 64);
		twerkbarback.mirror = true;
		setRotation(twerkbarback, 0F, 0F, 0F);
	}

	public void renderAll(float size)
	{

		wallright.render(size);
		wallleft.render(size);
		base.render(size);
		wallback.render(size);
		column.render(size);
		wallfront.render(size);
		brace1.render(size);
		brace2.render(size);
		brace3.render(size);
		brace4.render(size);
		twerkbarright.render(size);
		twerkbarfront.render(size);
		twerkbarleft.render(size);
		twerkbarback.render(size);
		
		ring1left.render(size);
		ring2left.render(size);
		ring3left.render(size);
		ring3right.render(size);
		ring3back.render(size);
		ring3back.render(size);
		ring2back.render(size);
		ring1right.render(size);
		ring1back.render(size);
		ring1front.render(size);
		ring2front.render(size);
		ring2right.render(size);
	}

	public void renderAllExceptAnimated(float size)
	{
		wallright.render(size);
		wallleft.render(size);
		base.render(size);
		wallback.render(size);
		column.render(size);
		wallfront.render(size);
		brace1.render(size);
		brace2.render(size);
		brace3.render(size);
		brace4.render(size);
		twerkbarright.render(size);
		twerkbarfront.render(size);
		twerkbarleft.render(size);
		twerkbarback.render(size);
	}
	
	public void renderAnimated(float size)
	{
		ring1left.render(size);
		ring2left.render(size);
		ring3left.render(size);
		ring1right.render(size);
		ring2right.render(size);
		ring3right.render(size);
		ring1back.render(size);
		ring2back.render(size);
		ring3back.render(size);
		ring1front.render(size);
		ring2front.render(size);
		ring3front.render(size);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
*/