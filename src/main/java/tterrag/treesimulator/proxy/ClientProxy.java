package tterrag.treesimulator.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import tterrag.treesimulator.RenderEngine;
import tterrag.treesimulator.TileEngine;
import tterrag.treesimulator.TreeSimulator;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TreeSimulator.engine), 0, new ModelResourceLocation("treegrowingsimulator" + ":" + "engine", "inventory"));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());
	}
}
