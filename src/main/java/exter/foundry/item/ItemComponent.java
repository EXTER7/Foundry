package exter.foundry.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class ItemComponent extends Item
{
  
  @Deprecated static public final int COMPONENT_GEAR = 0;// Moved to substratum
  static public final int COMPONENT_HEATINGCOIL = 1;
  static public final int COMPONENT_REFRACTORYCLAY = 2;
  static public final int COMPONENT_REFRACTORYBRICK = 3;
  static public final int COMPONENT_BLANKMOLD = 4;
  static public final int COMPONENT_GUN_BARREL = 5;
  static public final int COMPONENT_REVOLVER_DRUM = 6;
  static public final int COMPONENT_REVOLVER_FRAME = 7;
  static public final int COMPONENT_AMMO_CASING = 8;
  static public final int COMPONENT_AMMO_BULLET = 9;
  static public final int COMPONENT_AMMO_BULLET_HOLLOW = 10;
  static public final int COMPONENT_AMMO_BULLET_JACKETED = 11;
  static public final int COMPONENT_GUNPOWDER_SMALL = 12;
  static public final int COMPONENT_BLAZEPOWDER_SMALL = 13;
  static public final int COMPONENT_AMMO_PELLET = 14;
  static public final int COMPONENT_AMMO_CASING_SHELL = 15;
  static public final int COMPONENT_SHOTGUN_PUMP = 16;
  static public final int COMPONENT_SHOTGUN_FRAME = 17;
  @Deprecated static public final int COMPONENT_DUST_ZINC = 18;// Moved to substratum
  @Deprecated static public final int COMPONENT_DUST_BRASS = 19;// Moved to substratum
  @Deprecated static public final int COMPONENT_DUST_CUPRONICKEL = 20;// Moved to substratum
  static public final int COMPONENT_SHARD_ENERGY_TC = 21;
  static public final int COMPONENT_SHARD_LIFE_TC = 22;
  static public final int COMPONENT_SHARD_VOID_TC = 23;
  static public final int COMPONENT_AMMO_BULLET_STEEL = 24;
  static public final int COMPONENT_AMMO_PELLET_STEEL = 25;

  static public final String[] REGISTRY_NAMES = 
  {
    "componentGearStone",
    "componentHeatingCoil",
    "componentRefractoryClay",
    "componentRefractoryBrick",
    "componentBlankMold",
    "componentGunBarrel",
    "componentRevolverDrum",
    "componentRevolverFrame",
    "componentRoundCasing",
    "componentBullet",
    "componentBulletHollow",
    "componentBulletJacketed",
    "componentDustSmallGunpowder",
    "componentDustSmallBlaze",
    "componentPellet",
    "componentShellCasing",
    "componentShotgunPump",
    "componentShotgunFrame",
    "componentDustZinc",
    "componentDustBrass",
    "componentDustCupronickel",
    "componentShardEnergyTC",
    "componentShardLifeTC",
    "componentShardVoidTC",
    "componentBulletSteel",
    "componentPelletSteel"
  };


  public ItemComponent()
  {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("component");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack)
  {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list)
  {
    int i;
    for (i = 0; i < REGISTRY_NAMES.length; i++)
    {
      if(i == COMPONENT_GEAR || i == COMPONENT_DUST_ZINC || i == COMPONENT_DUST_BRASS || i == COMPONENT_DUST_CUPRONICKEL)
      {
        continue;
      }
      list.add(new ItemStack(this, 1, i));
    }
  }
}
