package exter.foundry.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;


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
