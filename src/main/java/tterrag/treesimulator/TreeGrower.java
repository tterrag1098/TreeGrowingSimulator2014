package tterrag.treesimulator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class TreeGrower {
	private static int movementCounter = 0;
	
	private static int[] getNearestSapling(World world, int xpos, int ypos, int zpos) {
		for (int x = -5; x <= 5; x++)
			for (int y = -2; y <= 2; y++)
				for (int z = -5; z <= 5; z++) {
					int id = world.getBlockId(x + xpos, y + ypos, z + zpos);
					if (Block.blocksList[id] instanceof BlockSapling)
						return new int[] { x + xpos, y + ypos, z + zpos };
				}
		return new int[] {};
	}

	private static void sendPacket(int x, int y, int z, World worldObj, Player player) {

		Packet250CustomPayload packet = new Packet250CustomPayload();

		packet.channel = TreeSimulator.CHANNEL;

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(12);
		DataOutputStream stream = new DataOutputStream(byteStream);
		try {
			stream.writeInt(x);
			stream.writeInt(y);
			stream.writeInt(z);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		packet.data = byteStream.toByteArray();
		packet.length = byteStream.size();
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}

	public static void requestTreeGrowth(EntityPlayerMP player) {
		int[] pos = getNearestSapling(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);

		if (pos.length == 0)
			return;
		
		movementCounter++;
		if (movementCounter > TreeSimulator.waitTime)
		{
			if (pos.length == 0) 
			{
				movementCounter--;
				//updatePlayerPos(player);
				return;
			}

			BonemealEvent event = new BonemealEvent(player, player.worldObj, player.worldObj.getBlockId(pos[0], pos[1], pos[2]), pos[0], pos[1], pos[2]);
			MinecraftForge.EVENT_BUS.post(event);

    		if ((double)player.worldObj.rand.nextFloat() < 0.45D)
            	((BlockSapling)Block.blocksList[player.worldObj.getBlockId(pos[0], pos[1], pos[2])]).markOrGrowMarked(player.worldObj, pos[0], pos[1], pos[2], player.worldObj.rand);
			
			if (TreeSimulator.showParticles)
				sendPacket(pos[0], pos[1], pos[2], player.worldObj, (Player) player);
			
			movementCounter = 0;
		}
	}
}
