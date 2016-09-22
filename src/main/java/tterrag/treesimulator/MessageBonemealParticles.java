package tterrag.treesimulator;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBonemealParticles implements IMessage
{
    @SuppressWarnings("null")
    public MessageBonemealParticles() {}

    @Nonnull
    protected BlockPos pos;

    public MessageBonemealParticles(@Nonnull BlockPos pos)
    {
        this.pos = pos;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
    }

    public static final class Handler implements IMessageHandler<MessageBonemealParticles, IMessage> {

        @Override
        public IMessage onMessage(final MessageBonemealParticles message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {

                @Override
                public void run() {
                    EntityPlayer entity = Minecraft.getMinecraft().thePlayer;
                    Block block = entity.getEntityWorld().getBlockState(message.pos).getBlock();
                    if (block instanceof BlockSapling) {
                        World entWorld = entity.getEntityWorld();
                        for (int c = 0; c < 20; c++) {
                            entWorld.spawnParticle(net.minecraft.util.EnumParticleTypes.VILLAGER_HAPPY, message.pos.getX() + entWorld.rand.nextDouble(),
                                    message.pos.getY() + entWorld.rand.nextDouble(), message.pos.getZ() + entWorld.rand.nextDouble(), 0f, 0f, 0f, 0); // message.x, message.y, message.z
                        }
                    }
                }
            });
            return null;
        }
    }
}
