package tterrag.treesimulator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class BlockEngine extends Block
{
	public BlockEngine()
	{
		super(Material.IRON);
		setHardness(1.0f);
	}
		
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEngine();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
        if (!worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, side))
            {
                playerIn.sendMessage(new TextComponentString("Energy Stored: " + TextFormatting.YELLOW + te.getCapability(CapabilityEnergy.ENERGY, side).getEnergyStored() + " FE"));
            }
        }
        return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) 
	{
	    return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) 
	{
	    return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) 
	{
	    return EnumBlockRenderType.INVISIBLE;
	}
}
	