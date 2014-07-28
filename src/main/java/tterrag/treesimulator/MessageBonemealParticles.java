package tterrag.treesimulator;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        Block block = entity.worldObj.getBlock(message.x, message.y, message.z);
        if (block instanceof BlockSapling)
        {
            entity.worldObj.playAuxSFX(2005, message.x, message.y, message.z, 0);               
        }
        return null;
    }
}
