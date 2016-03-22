package exter.foundry.block;


import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;


public class BlockRefractoryCasing extends Block
{

  public BlockRefractoryCasing()
  {
    super(Material.rock);
    setHardness(1.0F);
    setResistance(8.0F);
    setSoundType(SoundType.STONE);
    setUnlocalizedName("refractoryCasing");
    setCreativeTab(FoundryTabMaterials.tab);
  }

}
