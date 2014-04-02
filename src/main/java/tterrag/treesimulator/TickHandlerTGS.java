package tterrag.treesimulator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import static tterrag.treesimulator.TickHandlerTGS.PlayerState.*;

public class TickHandlerTGS implements ITickHandler {

	private double posX = 0, posZ = 0;
	private int movementCounter = 0;
	private int ticksSinceLastCheck = 0;

	public enum PlayerState {
		CROUCHED, STANDING;
		
		public static PlayerState getState(boolean bool){ return bool ? CROUCHED : STANDING; };
	};

	private PlayerState state = STANDING;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) 
	{
		if (type.contains(TickType.PLAYER))
		{
			EntityPlayer player = (EntityPlayer) tickData[0];
			Player basePlayer = (Player) tickData[0];
			if (ticksSinceLastCheck >= 5)
			{
				int[] pos = getNearestSapling(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);

				if (pos.length == 0)
					return;
				
				if ((Math.abs(player.posX - posX) > 0.25 || Math.abs(player.posZ - posZ) > 0.25) && TreeSimulator.sprintingWorks)
				{
					movementCounter++;
				}
				if ((PlayerState.getState(player.isSneaking()) != state) && TreeSimulator.crouchingWorks)
				{					
					movementCounter++;
				}
				if (movementCounter > TreeSimulator.waitTime)
				{
					if (pos.length == 0) 
					{
						movementCounter--;
						updatePlayerPos(player);
						return;
					}

					BonemealEvent event = new BonemealEvent(player, player.worldObj, player.worldObj.getBlockId(pos[0], pos[1], pos[2]), pos[0], pos[1], pos[2]);
					MinecraftForge.EVENT_BUS.post(event);

	        		if ((double)player.worldObj.rand.nextFloat() < 0.45D)
	                	((BlockSapling)Block.blocksList[player.worldObj.getBlockId(pos[0], pos[1], pos[2])]).markOrGrowMarked(player.worldObj, pos[0], pos[1], pos[2], player.worldObj.rand);
					
					if (TreeSimulator.showParticles)
						sendPacket(pos[0], pos[1], pos[2], player.worldObj, basePlayer);
					
					movementCounter = 0;
				}
			}
			else
			{
				ticksSinceLastCheck++;
			}
			
			state = PlayerState.getState(player.isSneaking());
			updatePlayerPos(player);
		}
	}

	private void sendPacket(int x, int y, int z, World worldObj, Player player) {

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

	private void updatePlayerPos(EntityPlayer player) {
		posX = player.posX;
		posZ = player.posZ;
	}

	private int[] getNearestSapling(World world, int xpos, int ypos, int zpos) {
		for (int x = -5; x <= 5; x++)
			for (int y = -2; y <= 2; y++)
				for (int z = -5; z <= 5; z++) {
					int id = world.getBlockId(x + xpos, y + ypos, z + zpos);
					if (Block.blocksList[id] instanceof BlockSapling)
						return new int[] { x + xpos, y + ypos, z + zpos };
				}
		return new int[] {};
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		return;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "treeSimulatorTickHandler";
	}
}
