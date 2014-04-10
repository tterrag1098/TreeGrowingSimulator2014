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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerTGS implements IPacketHandler{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p) {
		if (p instanceof EntityPlayerMP) {
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
					EntityPlayer player = (EntityPlayer) p;
					Player basePlayer = (Player) p;
					if (TreeSimulator.tickHandler.ticksSinceLastCheck >= 5)
					{
						int[] pos = TreeSimulator.tickHandler.getNearestSapling(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);

						if (pos.length == 0)
							return;
						
						TreeSimulator.tickHandler.movementCounter++;
						if (TreeSimulator.tickHandler.movementCounter > TreeSimulator.waitTime)
						{
							if (pos.length == 0) 
							{
								TreeSimulator.tickHandler.movementCounter--;
								return;
							}

							BonemealEvent event = new BonemealEvent(player, player.worldObj, player.worldObj.getBlockId(pos[0], pos[1], pos[2]), pos[0], pos[1], pos[2]);
							MinecraftForge.EVENT_BUS.post(event);

			        		if ((double)player.worldObj.rand.nextFloat() < 0.45D)
			                	((BlockSapling)Block.blocksList[player.worldObj.getBlockId(pos[0], pos[1], pos[2])]).markOrGrowMarked(player.worldObj, pos[0], pos[1], pos[2], player.worldObj.rand);
							
							if (TreeSimulator.showParticles)
								TreeSimulator.tickHandler.sendPacket(pos[0], pos[1], pos[2], player.worldObj, basePlayer);
							
							TreeSimulator.tickHandler.movementCounter = 0;
						}
					}
					else
					{
						TreeSimulator.tickHandler.ticksSinceLastCheck++;
					}
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

				EntityPlayer entity = (EntityPlayer) p;
				Block block = Block.blocksList[entity.worldObj.getBlockId(x, y, z)];
				if (block instanceof BlockSapling)
				{
					entity.worldObj.playAuxSFX(2005, x, y, z, 0);				
				}
			}
		}
	}
}
