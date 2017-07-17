package tterrag.treesimulator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEngine extends TileEntity implements ITickable
{
	private EnergyStorage storage;
	private int perTick = 10;
	double animationProgress = 0;

	public TileEngine()
	{
		storage = new EnergyStorage(20000);
	}

	@Override
	public void update()
	{
		if (!getWorld().isRemote)
		{
			for (EnumFacing side : EnumFacing.VALUES)
			{
				TileEntity tile = this.getWorld().getTileEntity(getPos().offset(side));
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite()))
				{
				    IEnergyStorage other = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					this.storage.extractEnergy(other.receiveEnergy(this.storage.extractEnergy(perTick, true), false), false);
				}
			}
		}
		
		if (animationProgress > Math.PI * 2)
			animationProgress -= Math.PI * 2;
	}

	public boolean bumpEnergy(int amnt)
	{
		animationProgress += 0.1;
		if (storage.receiveEnergy(amnt, false) == amnt)
		{
			return true;
		}
		else
			return false;
	}
}