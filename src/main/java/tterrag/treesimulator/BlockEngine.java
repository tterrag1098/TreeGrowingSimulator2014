package tterrag.treesimulator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockEngine extends Block
{
	public BlockEngine()
	{
		super(Material.iron);
		setHardness(1.0f);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IIconRegister)
	{
		this.blockIcon = par1IIconRegister.registerIcon("treegrowingsimulator:clocktwerkEngine");
	}
		
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEngine();
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (!par1World.isRemote)
			par5EntityPlayer.addChatMessage(new ChatComponentText("Energy Stored: " + EnumChatFormatting.YELLOW + ((TileEngine)par1World.getTileEntity(par2, par3, par4)).getEnergyStored(ForgeDirection.UP) + " RF"));
		return true;
	}
	
//	@Override
//	public boolean isOpaqueCube()
//	{
//		return false;
//	}
//	
//	@Override
//	public boolean isBlockNormalCube(World world, int x, int y, int z)
//	{
//		return false;
//	}
//	
//	@Override
//	public int getRenderType()
//	{
//		return -1;
//	}
}
	