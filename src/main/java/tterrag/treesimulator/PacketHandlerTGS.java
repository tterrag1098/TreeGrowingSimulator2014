package tterrag.treesimulator;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandlerTGS
{
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("treegrowingsimulator", TreeSimulator.CHANNEL), 
            () -> PROTOCOL_VERSION, 
            PROTOCOL_VERSION::equals, 
            PROTOCOL_VERSION::equals);
    
    public static void init()
    {
        INSTANCE.registerMessage(0, MessageBonemealParticles.class, MessageBonemealParticles::encode, MessageBonemealParticles::new, MessageBonemealParticles::handle);
        INSTANCE.registerMessage(1, MessagePlayerParticle.class, MessagePlayerParticle::encode, MessagePlayerParticle::new, MessagePlayerParticle::handle);
    }
}
