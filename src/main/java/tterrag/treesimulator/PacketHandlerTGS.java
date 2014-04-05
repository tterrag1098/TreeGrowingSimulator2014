package tterrag.treesimulator;

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
			if (packet.data.length == 12)
			{
				byte[] bytes = packet.data;
			
				int x = ((bytes[0] & 255) | ((bytes[1] & 255) << 8) | ((bytes[2] & 255) << 16) | (bytes[3] & 255) << 24);
				int y = ((bytes[4] & 255) | ((bytes[5] & 255) << 8) | ((bytes[6] & 255) << 16) | (bytes[7] & 255) << 24);
				int z = ((bytes[8] & 255) | ((bytes[9] & 255) << 8) | ((bytes[10] & 255) << 16) | (bytes[11] & 255) << 24);

				EntityPlayer entity = (EntityPlayer) player;
				Block block = Block.blocksList[entity.worldObj.getBlockId(x, y, z)];
				if (block instanceof BlockSapling)
				{
					entity.worldObj.playAuxSFX(2005, x, y, z, 0);				
				}
			}
		}
	}
}
