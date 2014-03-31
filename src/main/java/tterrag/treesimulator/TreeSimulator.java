package tterrag.treesimulator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "treeGrowingSimulator", version = "0.0.1", name = "Tree Growing Simulator 2014")
public class TreeSimulator {

	@Instance
	public TreeSimulator instance;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		TickRegistry.registerTickHandler(new TickHandlerTGS(), Side.SERVER);
	}
}
