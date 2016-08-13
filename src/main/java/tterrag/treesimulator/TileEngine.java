package tterrag.treesimulator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEngine extends TileEntity implements IEnergyHandler, ITickable
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
		if (!worldObj.isRemote)
		{
			for (EnumFacing side : EnumFacing.VALUES)
			{
				TileEntity tile = this.worldObj.getTileEntity(new BlockPos(this.pos.getX() + side.getFrontOffsetX(), this.pos.getY() + side.getFrontOffsetY(), this.pos.getZ() + side.getFrontOffsetZ()));
				if ((tile instanceof IEnergyHandler))
				{
					this.storage.extractEnergy(((IEnergyHandler) tile).receiveEnergy(side.getOpposite(), this.storage.extractEnergy(perTick, true), false), false);
				}
			}
		}
		
		if (animationProgress > Math.PI * 2)
			animationProgress -= Math.PI * 2;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return 0;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{
		return storage.extractEnergy(maxExtract, simulate);
	}
    
	@Override
    public boolean canConnectEnergy(EnumFacing from)
    {
	    return true;
    }
    
	@Override
	public int getEnergyStored(EnumFacing from)
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return storage.getMaxEnergyStored();
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