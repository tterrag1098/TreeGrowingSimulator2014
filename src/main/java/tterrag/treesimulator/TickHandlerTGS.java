package tterrag.treesimulator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Throwables;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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
	
	private Constructor<BonemealEvent> eventctor;

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
				List<BlockPos> coords = getNearestBlocks(player.getEntityWorld(), new BlockPos(player));

				if (coords.size() == 0)
					return;

				if (player.isSprinting())
				{
					movementCounter++;
	                sendPlayerPacket(player, coords);
					doEngines(coords, player.getEntityWorld());
				}
				if (PlayerState.getState(player.isSneaking()) != getState(player))
				{
					movementCounter++;
                    sendPlayerPacket(player, coords);
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

					for (BlockPos pos : coords)
					{
						Block block = player.getEntityWorld().getBlockState(pos).getBlock();

						if (block instanceof BlockSapling)
						{
							BonemealEvent bonemeal;
							try {
								bonemeal = new BonemealEvent(player, player.getEntityWorld(), pos, player.getEntityWorld().getBlockState(pos));
							} catch (Throwable t) {
							    try {
							        if (eventctor == null) {
							            eventctor = BonemealEvent.class.getConstructor(EntityPlayer.class, World.class, BlockPos.class, IBlockState.class, EnumHand.class, ItemStack.class);
							        }
							        bonemeal = eventctor.newInstance(player, player.getEntityWorld(), pos, player.getEntityWorld().getBlockState(pos), EnumHand.MAIN_HAND, new ItemStack(Blocks.AIR, 0));
							    } catch (Exception e) {
							        throw Throwables.propagate(e);
							    }
							}
							MinecraftForge.EVENT_BUS.post(bonemeal);

							BlockSapling sapling = (BlockSapling) block;
							if ((double) player.getEntityWorld().rand.nextFloat() < 0.45D)
							{
								sapling.generateTree(player.getEntityWorld(), pos, player.getEntityWorld().getBlockState(pos), player.getEntityWorld().rand);
							}

							if (TreeSimulator.showParticles && sapling == Blocks.SAPLING)
								sendBonemealPacket(pos);

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
	private void doEngines(List<BlockPos> coords, World world)
	{
		for (BlockPos pos : coords)
		{
			Block block = world.getBlockState(pos).getBlock();
		}
	}

	private void sendBonemealPacket(BlockPos pos)
	{
		PacketHandlerTGS.INSTANCE.sendToAll(new MessageBonemealParticles(pos));
	}
		   
    private void sendPlayerPacket(EntityPlayer player, List<BlockPos> coords) {
        for (BlockPos pos : coords) {
            PacketHandlerTGS.INSTANCE.sendToDimension(new MessagePlayerParticle(player, pos), player.worldObj.provider.getDimension());
        }
    }

	private List<BlockPos> getNearestBlocks(World world, BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (BlockPos p : BlockPos.getAllInBox(pos.add(-5, -2, -5), pos.add(5, 2, 5))) {
            Block block = world.getBlockState(p).getBlock();
            if (block instanceof BlockSapling) {
                list.add(p);
            }
        }
        return list;
    }
}
