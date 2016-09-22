package tterrag.treesimulator;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandlerTGS
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(TreeSimulator.CHANNEL);
    
    public static void init()
    {
        INSTANCE.registerMessage(MessageBonemealParticles.Handler.class, MessageBonemealParticles.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MessagePlayerParticle.Handler.class, MessagePlayerParticle.class, 1, Side.CLIENT);
    }
}
