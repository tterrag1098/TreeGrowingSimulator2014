package tterrag.treesimulator;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("treegrowingsimulator")
public class TreeSimulator {

    public static class ServerConfigs {
        public final IntValue waitTime;

        ServerConfigs(ForgeConfigSpec.Builder builder) {
            waitTime = builder.comment("The amount of ticks (times 5) you must be crouching or sprinting before bonemeal is applied.")
                    .defineInRange("waitTime", 20, 1, 1_000_000);
        }
    }

    public static class CommonConfigs {
        public final BooleanValue showParticles;
        public final BooleanValue allTheParticles;

        CommonConfigs(ForgeConfigSpec.Builder builder) {
            showParticles = builder.comment("Show bonemeal particles when appropriate. Not sure why you would turn this off, but eh.")
                    .define("showParticles", true);
            allTheParticles = builder.comment("Will spawn a LOT more particles for actions near saplings.")
                    .define("allTheParticles", true);
        }
    }

    static final ForgeConfigSpec serverSpec;
    public static final ServerConfigs SERVER_CONFIGS;
    static {
        final Pair<ServerConfigs, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfigs::new);
        serverSpec = specPair.getRight();
        SERVER_CONFIGS = specPair.getLeft();
    }

    static final ForgeConfigSpec commonSpec;
    public static final CommonConfigs COMMON_CONFIGS;
    static {
        final Pair<CommonConfigs, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfigs::new);
        commonSpec = specPair.getRight();
        COMMON_CONFIGS = specPair.getLeft();
    }

    public static final String CHANNEL = "treegrowingsimulator";

    public TreeSimulator() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::init);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
    }

    private void init(FMLCommonSetupEvent event) {		
        PacketHandlerTGS.init();
        MinecraftForge.EVENT_BUS.register(new TickHandlerTGS());
    }
}
