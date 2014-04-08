package tterrag.treesimulator;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "treeGrowingSimulator", version = "0.0.3", name = "Tree Growing Simulator 2014")
@NetworkMod(serverSideRequired=true, clientSideRequired=false, channels = {TreeSimulator.CHANNEL}, packetHandler = PacketHandlerTGS.class)
public class TreeSimulator {

	public static int waitTime;
	public static boolean showParticles;
	public static final String CHANNEL = "TGS2014";
	
	@Instance
	public static TreeSimulator instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		initConfig(event.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
<<<<<<< HEAD
		TickRegistry.registerTickHandler(new TickHandlerTGS(), Side.SERVER);
=======
		tickHandler = new TickHandlerTGS();
		TickRegistry.registerTickHandler(tickHandler, Side.SERVER);
		if (event.getSide() == Side.CLIENT && yellingWorks) {
			micListener = new MicListener();
		}
>>>>>>> parent of 5194366... Merge pull request #2 from impiaaa/master
	}
	
	private void initConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		waitTime = config.get("Tweaks", "waitTime", 100, "The amount of ticks (times 5) you must be sprinting before bonemeal is applied").getInt();
		showParticles = config.get("Tweaks", "showParticles", true, "Show bonemeal particles when appropriate. Not sure why you would turn this off, but eh").getBoolean(true);
		
		config.save();
	}
	
	@EventHandler
	public void gameStarted(FMLServerStartedEvent e) {
		if (e.getSide() == Side.CLIENT && yellingWorks)
		{
			micListener.init();
			TickRegistry.registerTickHandler(micListener, Side.CLIENT);
		}
	}

	@EventHandler
	public void gameStopping(FMLServerStoppingEvent e) {
		if (e.getSide() == Side.CLIENT && yellingWorks)
		{
			micListener.shutdown();
		}
	}
}
