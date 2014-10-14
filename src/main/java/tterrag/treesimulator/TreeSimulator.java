package tterrag.treesimulator;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import tterrag.treesimulator.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "treeGrowingSimulator", version = "0.0.3", name = "Tree Growing Simulator 2014")
public class TreeSimulator {

	public static int waitTime;
	public static boolean showParticles;
	public static int energyPerBump;
	
	public static final String CHANNEL = "TGS2014";
	
	@SidedProxy(clientSide="tterrag.treesimulator.proxy.ClientProxy", serverSide="tterrag.treesimulator.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance
	public static TreeSimulator instance;
	
	public static Block engine;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		initConfig(event.getSuggestedConfigurationFile());
		
		engine = new BlockEngine().setBlockName("clocktwerkEngine");
		GameRegistry.registerBlock(engine, "clocktwerkEngine");
		GameRegistry.registerTileEntity(TileEngine.class, "tileClocktwerkEngine");

		proxy.registerRenderers();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{		
	    PacketHandlerTGS.init();
	    
	    FMLCommonHandler.instance().bus().register(new TickHandlerTGS());
	    
		GameRegistry.addRecipe(new ItemStack(engine), 
				"sis",
				"ibi",
				"sis",
				
				's', Blocks.stone,
				'i', Items.iron_ingot,
				'b', Blocks.iron_bars
		);
	}
	
	private void initConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		waitTime = config.get("Tweaks", "waitTime", 100, "The amount of ticks (times 5) you must be crouching or sprinting before bonemeal is applied").getInt();
		showParticles = config.get("Tweaks", "showParticles", true, "Show bonemeal particles when appropriate. Not sure why you would turn this off, but eh").getBoolean(true);
		energyPerBump = config.get("Tweaks", "energyPerBump", 25, "Energy (in RF) that is gotten each time the engine is \"bumped,\" meaning every time you crouch or sprint").getInt();
		
		config.save();
	}
}
