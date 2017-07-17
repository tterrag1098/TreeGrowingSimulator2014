package tterrag.treesimulator;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlayerParticle extends MessageBonemealParticles {

    private int playerID;
    
    public MessagePlayerParticle() {}
    
    public MessagePlayerParticle(@Nonnull EntityPlayer player, @Nonnull BlockPos pos) {
        super(pos);
        this.playerID = player.getEntityId();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(playerID);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.playerID = buf.readInt();
    }

    public static final class Handler implements IMessageHandler<MessagePlayerParticle, IMessage> {
        
        @Override
        public IMessage onMessage(final MessagePlayerParticle message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                
                @Override
                public void run() {
                    EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(message.playerID);
                    if (player == null) {
                        return;
                    }
                    
                    World world = player.world;
                    
                    Vec3d spawnPos = new Vec3d(
                            player.posX + ((world.rand.nextGaussian() - 0.5) * 0.5), 
                            player.posY + ((world.rand.nextGaussian() - 0.5) * 0.8),
                            player.posZ + ((world.rand.nextGaussian() - 0.5) * 0.5)
                        );
                    Vec3d endPos = new Vec3d(message.pos).addVector(0.5, 0.5, 0.5);
                    
                    Vec3d vel = spawnPos.subtract(endPos).scale(-0.02);
                    
                    Particle p = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(net.minecraft.util.EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), spawnPos.xCoord, spawnPos.yCoord, spawnPos.zCoord, vel.xCoord, vel.yCoord, vel.zCoord);
                    p.motionX = vel.xCoord;
                    p.motionY = vel.yCoord;
                    p.motionZ = vel.zCoord;
                }
            });
            
            return null;
        }
    }
}
