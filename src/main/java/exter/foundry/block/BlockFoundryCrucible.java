package exter.foundry.block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;


public class BlockFoundryCrucible extends Block
{

  public BlockFoundryCrucible(int id)
  {
    super(id, Material.rock);
    setHardness(1.0F);
    setResistance(8.0F);
    setStepSound(Block.soundStoneFootstep);
    setUnlocalizedName("foundryCrucible");
    setCreativeTab(FoundryTabMaterials.tab);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister register)
  {
      blockIcon = register.registerIcon("foundry:casing");
  }
}
