package tterrag.treesimulator;

import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerTGS implements ITickHandler{

	private double posX = 0, posZ = 0;
	private int movementCounter = 0;
	private int ticksSinceLastCheck = 0;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) 
	{
		if (type.contains(TickType.PLAYER))
		{
			EntityPlayer player = (EntityPlayer) tickData[0];
			if (ticksSinceLastCheck >= 5)
			{
				if (Math.abs(player.posX - posX) > 0.25 || Math.abs(player.posZ - posZ) > 0.25)
				{
					movementCounter++;
					if (movementCounter > TreeSimulator.instance.waitTime)
					{
						int[] pos = getNearestSapling(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);

						if (pos.length == 0) 
						{
							movementCounter--;
							updatePlayerPos(player);
							return;
						}

						BonemealEvent event = new BonemealEvent(player, player.worldObj, player.worldObj.getBlockId(pos[0], pos[1], pos[2]), pos[0], pos[1], pos[2]);
						MinecraftForge.EVENT_BUS.post(event);

						((BlockSapling)Block.blocksList[player.worldObj.getBlockId(pos[0], pos[1], pos[2])]).markOrGrowMarked(player.worldObj, pos[0], pos[1], pos[2], player.worldObj.rand);

						System.out.println("bonemeal event at: " + event.X + " " + event.Y + " " + event.Z + " status:" + event.getResult());
						movementCounter = 0;
					}
					System.out.println("moved!");
				}
			}
			else
			{
				ticksSinceLastCheck++;
			}
			
			updatePlayerPos(player);
		}
	}
	
	private void updatePlayerPos(EntityPlayer player)
	{
		posX = player.posX;
		posZ = player.posZ;
	}

	private int[] getNearestSapling(World world, int xpos, int ypos, int zpos)
	{
		for (int x = -5; x <= 5; x++)
			for (int y = -2; y <= 2; y++)
				for (int z = -5; z <= 5; z++)
				{
					int id = world.getBlockId(x + xpos, y + ypos, z + zpos);
					if (Block.blocksList[id] instanceof BlockSapling)
						return new int[]{x + xpos, y + ypos, z + zpos};
				}
		return new int[]{};
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		return;
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "treeSimulatorTickHandler";
	}
}
