package tterrag.treesimulator;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerTGS implements IPacketHandler{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (player instanceof EntityPlayerMP) {
			// server side, "request growth" packet
			if (TreeSimulator.yellingWorks) {
				ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.data);
				DataInputStream stream = new DataInputStream(byteStream);
				double rms;
				try {
					rms = stream.readDouble();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				if (rms > TreeSimulator.loudnessThreshold) {
					TreeGrower.instance.requestTreeGrowth((EntityPlayerMP)player);
				}
			}
		}
		else {
			// client side, particle effects packet
			ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.data);
			DataInputStream stream = new DataInputStream(byteStream);
			int x, y, z;
			try {
				x = stream.readInt();
				y = stream.readInt();
				z = stream.readInt();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	
			EntityPlayer entity = (EntityPlayer) player;
			Block block = Block.blocksList[entity.worldObj.getBlockId(x, y, z)];
			if (block instanceof BlockSapling)
			{
				entity.worldObj.playAuxSFX(2005, x, y, z, 0);				
			}
		}
	}
}
