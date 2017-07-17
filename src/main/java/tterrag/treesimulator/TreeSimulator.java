package tterrag.treesimulator;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import tterrag.treesimulator.proxy.CommonProxy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "treegrowingsimulator", version = "0.0.4", name = "Tree Growing Simulator 2014", acceptedMinecraftVersions = "[1.9.4,1.13)")
public class TreeSimulator {

	public static int waitTime;
	public static boolean showParticles = true;
	public static boolean allTheParticles = false;
	public static int energyPerBump;
	
	public static final String CHANNEL = "TGS2014";
	
	@SidedProxy(clientSide="tterrag.treesimulator.proxy.ClientProxy", serverSide="tterrag.treesimulator.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance
	public static TreeSimulator instance;
	
	public static BlockEngine engine;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		initConfig(event.getSuggestedConfigurationFile());
		
		engine = (BlockEngine) new BlockEngine().setRegistryName("engine");
		GameRegistry.register(engine);
		GameRegistry.register(new ItemBlock(engine).setRegistryName(engine.getRegistryName()));

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
				
				's', Blocks.STONE,
				'i', Items.IRON_INGOT,
				'b', Blocks.IRON_BARS
		);
	}
	
	private void initConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		waitTime = config.get("Tweaks", "waitTime", 100, "The amount of ticks (times 5) you must be crouching or sprinting before bonemeal is applied").getInt();
		showParticles = config.get("Tweaks", "showParticles", true, "Show bonemeal particles when appropriate. Not sure why you would turn this off, but eh").getBoolean(true);
		allTheParticles = config.get("Tweaks", "allTheParticles", false, "Will spawn a LOT more particles for actions near saplings.").getBoolean(false);
		energyPerBump = config.get("Tweaks", "energyPerBump", 25, "Energy (in RF) that is gotten each time the engine is \"bumped,\" meaning every time you crouch or sprint").getInt();
		
		config.save();
	}
}
