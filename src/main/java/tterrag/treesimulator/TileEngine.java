package tterrag.treesimulator;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileEngine extends TileEntity implements IEnergyHandler
{
	private EnergyStorage storage;
	private int perTick = 10;
	double animationProgress = 0;

	public TileEngine()
	{
		storage = new EnergyStorage(20000);
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
			{
				TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ);
				if ((tile instanceof IEnergyHandler))
				{
					this.storage.extractEnergy(((IEnergyHandler) tile).receiveEnergy(side.getOpposite(), this.storage.extractEnergy(perTick, true), false), false);
				}
			}
		}
		
		System.out.println((worldObj.isRemote ? "client: " : "server: ") + "  " + animationProgress);
		if (animationProgress > Math.PI * 2)
			animationProgress -= Math.PI * 2;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		return storage.getMaxEnergyStored();
	}

	public boolean bumpEnergy(int amnt)
	{
		animationProgress += 0.1;
		if (storage.receiveEnergy(amnt, false) == amnt)
		{
			System.out.println("bumped! " + animationProgress);
			return true;
		}
		else
			return false;
	}
}