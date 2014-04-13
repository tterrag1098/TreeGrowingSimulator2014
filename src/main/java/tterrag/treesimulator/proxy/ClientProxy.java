package tterrag.treesimulator.proxy;

import tterrag.treesimulator.RenderEngine;
import tterrag.treesimulator.TileEngine;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
	//	ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());
	}
}
