package tterrag.treesimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TickHandlerTGS
{
	private Map<String, Integer> counters = new HashMap<String, Integer>();
	private Map<String, PlayerState> states = new HashMap<String, PlayerState>();
	private int ticksSinceLastCheck = 0;

	public enum PlayerState
	{
		CROUCHED, STANDING;

		public static PlayerState getState(boolean bool)
		{
			return bool ? CROUCHED : STANDING;
		};
	};

	private class Coord
	{
		public int x, y, z;

		public Coord(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}


	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
	    Integer temp = counters.get(event.player.getCommandSenderEntity().getName());
	    int movementCounter = temp == null ? 0 : temp;
	    
		if (event.phase == Phase.END && !event.player.getEntityWorld().isRemote)
		{
			EntityPlayer player = (EntityPlayer) event.player;
			if (ticksSinceLastCheck >= 5)
			{
				List<Coord> coords = getNearestBlocks(player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);

				if (coords.size() == 0)
					return;

				if (player.isSprinting())
				{
					movementCounter++;
					doEngines(coords, player.getEntityWorld());
				}
				if (PlayerState.getState(player.isSneaking()) != getState(player))
				{
					movementCounter++;
					doEngines(coords, player.getEntityWorld());
				}
				if (movementCounter > TreeSimulator.waitTime)
				{
					if (coords.size() == 0)
					{
						movementCounter--;
						return;
					}
					
					Collections.shuffle(coords);

					for (Coord pos : coords)
					{
						Block block = player.getEntityWorld().getBlockState(new BlockPos(pos.x, pos.y, pos.z)).getBlock();

						if (block instanceof BlockSapling)
						{
							BonemealEvent bonemeal = new BonemealEvent(player, player.getEntityWorld(), new BlockPos(pos.x, pos.y, pos.z), player.getEntityWorld().getBlockState(new BlockPos(pos.x, pos.y, pos.z)));
							MinecraftForge.EVENT_BUS.post(bonemeal);

							BlockSapling sapling = (BlockSapling) block;
							if ((double) player.getEntityWorld().rand.nextFloat() < 0.45D)
								sapling.grow(player.getEntityWorld(), new BlockPos(pos.x, pos.y, pos.z), player.getEntityWorld().getBlockState(new BlockPos(pos.x, pos.y, pos.z)), player.getEntityWorld().rand);

							if (TreeSimulator.showParticles && sapling == Blocks.SAPLING)
								sendPacket(pos.x, pos.y, pos.z);
							
							break;
						}
					}
					movementCounter = 0;
				}
			}
			else
			{
				ticksSinceLastCheck++;
			}

			states.put(player.getCommandSenderEntity().getName(), PlayerState.getState(player.isSneaking()));
			counters.put(event.player.getCommandSenderEntity().getName(), movementCounter);
		}
	}
	
	private PlayerState getState(EntityPlayer player) 
	{
	    String user = player.getCommandSenderEntity().getName();
	    if (!states.containsKey(user)) {
	        states.put(user, PlayerState.getState(player.isSneaking()));
	    }
	    return states.get(user);
	}

	@Deprecated
	private void doEngines(List<Coord> coords, World world)
	{
		for (Coord pos : coords)
		{
			Block block = world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)).getBlock();
		}
	}

	private void sendPacket(int x, int y, int z)
	{
		PacketHandlerTGS.INSTANCE.sendToAll(new MessageBonemealParticles(x, y, z));
	}

	private List<Coord> getNearestBlocks(World world, int xpos, int ypos, int zpos)
	{
		List<Coord> list = new ArrayList<Coord>();
		for (int x = -5; x <= 5; x++)
			for (int y = -2; y <= 2; y++)
				for (int z = -5; z <= 5; z++)
				{
					Block block = world.getBlockState(new BlockPos(x + xpos, y + ypos, z + zpos)).getBlock();
					if (block instanceof BlockSapling)
						list.add(new Coord(x + xpos, y + ypos, z + zpos));
				}
		return list;
	}
}
