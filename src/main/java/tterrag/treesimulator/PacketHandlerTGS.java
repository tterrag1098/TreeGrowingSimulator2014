package tterrag.treesimulator;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandlerTGS
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(TreeSimulator.CHANNEL);
    
    public static void init()
    {
        INSTANCE.registerMessage(MessageBonemealParticles.class, MessageBonemealParticles.class, 0, Side.CLIENT);
    }
}
