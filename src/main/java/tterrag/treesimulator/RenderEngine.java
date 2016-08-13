package tterrag.treesimulator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderEngine extends TileEntitySpecialRenderer
{
	private static final ModelEngine model = new ModelEngine();
	private static final ResourceLocation texture = new ResourceLocation("treegrowingsimulator", "textures/models/engine.png");
	
	double i = 0;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f, int destroyStage)
	{
		renderEngineAt((TileEngine) tileentity, x, y, z, false);
	}
	
	public void renderEngineAt(TileEngine te, double x, double y, double z, boolean isItem)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y + (1f/16f), (float) z + 1f);

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		GL11.glRotatef(180f, 1f, 0, 0);

		if (te == null) model.renderAll(0.0625f);
		else
		{
			if (i > Math.PI * 2)
				i -= Math.PI * 2;
			i += 0.1d;
			
			model.renderAllExceptAnimated(0.0625f);
			GL11.glTranslated(0, Math.sin(i) / 10, 0);
			if (te.getWorld().getWorldTime() % 20 == 0)
				System.out.println(i + " : " + Math.sin(i)); 
			model.renderAnimated(0.0625f);
		}
		
		GL11.glPopMatrix();
	}
}
