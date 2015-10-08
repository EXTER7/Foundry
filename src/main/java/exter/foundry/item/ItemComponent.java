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
  static public final int COMPONENT_GEAR = 0;
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
  static public final int COMPONENT_DUST_ZINC = 18;
  static public final int COMPONENT_DUST_BRASS = 19;
  static public final int COMPONENT_DUST_CUPRONICKEL = 20;
  static public final int COMPONENT_SHARD_ENERGY_TC = 21;
  static public final int COMPONENT_SHARD_LIFE_TC = 22;
  static public final int COMPONENT_SHARD_VOID_TC = 23;
  static public final int COMPONENT_AMMO_BULLET_STEEL = 24;
  static public final int COMPONENT_AMMO_PELLET_STEEL = 25;


  static private final String[] ICON_PATHS = 
  {
    "foundry:gear",
    "foundry:heatingcoil",
    "foundry:foundry_clay",
    "foundry:foundry_brick",
    "foundry:claymold_blank",
    "foundry:gun_barrel",
    "foundry:revolver_drum",
    "foundry:revolver_frame",
    "foundry:ammo_casing",
    "foundry:ammo_bullet",
    "foundry:ammo_bulletHollow",
    "foundry:ammo_bulletJacketed",
    "foundry:gunpowderSmall",
    "foundry:blazePowderSmall",
    "foundry:ammo_pellet",
    "foundry:ammo_casingShell",
    "foundry:shotgun_pump",
    "foundry:shotgun_frame",
    "foundry:dust_zinc",
    "foundry:dust_brass",
    "foundry:dust_cupronickel",
    "foundry:shard_energy_tc",
    "foundry:shard_life_tc",
    "foundry:shard_void_tc",
    "foundry:ammo_bullet_steel",
    "foundry:ammo_pellet_steel"
  };
  
  static public final String[] REGISTRY_NAMES = 
  {
    "gearStone",
    "heatingCoil",
    "refractoryClay",
    "refractoryBrick",
    "moldSoftBlank",
    "gunBarrel",
    "revolverDrum",
    "revolverFrame",
    "ammoCasing",
    "ammoBullet",
    "ammoBulletHollow",
    "AmmoBulletJacketed",
    "dustSmallGunpowder",
    "dustSmallBlaze",
    "ammoPellet",
    "ammoShellCasing",
    "shotgunPump",
    "shotgunFrame",
    "dustZinc",
    "dustBrass",
    "dustCupronickel",
    "shardEnergy",
    "shardLife",
    "shardVoid",
    "ammoBulletSteel",
    "ammoPelletSteel"
  };


  public ItemComponent()
  {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("component");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack) {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
  {
    int i;
    for (i = 0; i < ICON_PATHS.length; i++)
    {
      ItemStack itemstack = new ItemStack(this, 1, i);
      list.add(itemstack);
    }
  }
}
