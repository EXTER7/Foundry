package exter.foundry.block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;


public class BlockRefractoryCasing extends Block
{

  public BlockRefractoryCasing()
  {
    super(Material.rock);
    setHardness(1.0F);
    setResistance(8.0F);
    setStepSound(Block.soundTypeStone);
    setBlockName("refractoryCasing");
    setCreativeTab(FoundryTabMaterials.tab);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register)
  {
      blockIcon = register.registerIcon("foundry:casing");
  }
}
