package tterrag.treesimulator;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.effect.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageBonemealParticles implements IMessage, IMessageHandler<MessageBonemealParticles, IMessage>
{
    public MessageBonemealParticles() {}
    
    private int x, y, z;
    
    public MessageBonemealParticles(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageBonemealParticles message, MessageContext ctx)
    {
        EntityPlayer entity = Minecraft.getMinecraft().thePlayer;
        Block block = entity.getEntityWorld().getBlockState(new BlockPos(message.x, message.y, message.z)).getBlock();
        if (block instanceof BlockSapling)
        {
        	World entWorld = entity.getEntityWorld();
        	for (int c = 0; c < 20; c++)
            {
        		entWorld.spawnParticle(net.minecraft.util.EnumParticleTypes.VILLAGER_HAPPY, message.x + entWorld.rand.nextDouble(), message.y + entWorld.rand.nextDouble(), message.z + entWorld.rand.nextDouble(), 0f, 0f, 0f, 0); //message.x, message.y, message.z        
            }
        }
        return null;
    }
}
