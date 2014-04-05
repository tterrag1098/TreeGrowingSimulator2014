package tterrag.treesimulator;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "treeGrowingSimulator", version = "0.0.3", name = "Tree Growing Simulator 2014")
@NetworkMod(serverSideRequired=true, clientSideRequired=false, channels = {TreeSimulator.CHANNEL}, packetHandler = PacketHandlerTGS.class)
public class TreeSimulator {

	public static int waitTime;
	public static boolean showParticles;
	public static boolean yellingWorks;
	public static double loudnessThreshold;
	public static final String CHANNEL = "TGS2014";
	public static TickHandlerTGS tickHandler;
	
	@SideOnly(Side.CLIENT)
	public MicListener micListener;
	
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
		tickHandler = new TickHandlerTGS();
		TickRegistry.registerTickHandler(tickHandler, Side.SERVER);
		if (event.getSide() == Side.CLIENT && yellingWorks) {
			micListener = new MicListener();
			micListener.init();
			TickRegistry.registerTickHandler(micListener, Side.CLIENT);
		}
	}
	
	private void initConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		waitTime = config.get("Tweaks", "waitTime", 100, "The amount of ticks (times 5) you must be sprinting before bonemeal is applied").getInt();
		showParticles = config.get("Tweaks", "showParticles", true, "Show bonemeal particles when appropriate. Not sure why you would turn this off, but eh").getBoolean(true);
		yellingWorks = config.get("Tweaks", "yellingWorks", true, "Enable talking to speed growth").getBoolean(true);
		loudnessThreshold = config.get("Tweaks", "loudnessThreshold", 60.0, "What is considered to be the loudness of talking").getDouble(60.0);
		
		config.save();
	}
}
