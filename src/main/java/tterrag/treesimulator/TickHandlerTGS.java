package tterrag.treesimulator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
			if (ticksSinceLastCheck >= 5)
			{
				if ((Math.abs(player.posX - posX) > 0.25 || Math.abs(player.posZ - posZ) > 0.25) && TreeSimulator.sprintingWorks)
				{
					TreeGrower.requestTreeGrowth((EntityPlayerMP)tickData[0]);
				}
				if ((PlayerState.getState(player.isSneaking()) != state) && TreeSimulator.crouchingWorks)
				{					
					TreeGrower.requestTreeGrowth((EntityPlayerMP)tickData[0]);
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

	private void updatePlayerPos(EntityPlayer player) {
		posX = player.posX;
		posZ = player.posZ;
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
