package tterrag.treesimulator;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "treeGrowingSimulator", version = "0.0.3", name = "Tree Growing Simulator 2014")
@NetworkMod(serverSideRequired=true, clientSideRequired=false, channels = {TreeSimulator.CHANNEL}, packetHandler = PacketHandlerTGS.class)
public class TreeSimulator {

	public static int waitTime;
	public static boolean showParticles;
	public static int energyPerBump;
	
	public static final String CHANNEL = "TGS2014";
	
	@Instance
	public static TreeSimulator instance;
	
	public static int engineID;
	public static Block engine;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		initConfig(event.getSuggestedConfigurationFile());
		
		engine = new BlockEngine(engineID).setUnlocalizedName("clocktwerkEngine");
		GameRegistry.registerBlock(engine, "clocktwerkEngine");
		GameRegistry.registerTileEntity(TileEngine.class, "tileClocktwerkEngine");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		TickRegistry.registerTickHandler(new TickHandlerTGS(), Side.SERVER);
		
		GameRegistry.addRecipe(new ItemStack(engine), 
				"sis",
				"ibi",
				"sis",
				
				's', Block.stone,
				'i', Item.ingotIron,
				'b', Block.fenceIron
		);
	}
	
	private void initConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		waitTime = config.get("Tweaks", "waitTime", 100, "The amount of ticks (times 5) you must be sprinting before bonemeal is applied").getInt();
		showParticles = config.get("Tweaks", "showParticles", true, "Show bonemeal particles when appropriate. Not sure why you would turn this off, but eh").getBoolean(true);
		energyPerBump = config.get("Tweaks", "energyPerBump", 25, "Energy (in RF) that is gotten each time the engine is \"bumped,\" meaning every time you crouch or sprint").getInt();
		
		engineID = config.getBlock("clocktwerkEngine", 1042, "ID for the Clocktwerk Engine").getInt() - 256;
		config.save();
	}
}
