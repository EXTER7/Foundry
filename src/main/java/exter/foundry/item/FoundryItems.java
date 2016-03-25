package exter.foundry.item;

import exter.foundry.item.ammo.ItemRoundNormal;
import exter.foundry.item.ammo.ItemRoundJacketed;
import exter.foundry.item.ammo.ItemRoundPoison;
import exter.foundry.item.ammo.ItemShellAP;
import exter.foundry.item.ammo.ItemShellNormal;
import exter.foundry.item.firearm.ItemRevolver;
import exter.foundry.item.firearm.ItemShotgun;
import exter.foundry.item.ammo.ItemRoundAP;
import exter.foundry.item.ammo.ItemRoundFire;
import exter.foundry.item.ammo.ItemRoundHollow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FoundryItems
{
  static public ItemComponent item_component;
  static public ItemMold item_mold;
  static public ItemRevolver item_revolver;
  static public ItemShotgun item_shotgun;
  static public ItemRoundNormal item_round;
  static public ItemRoundJacketed item_round_jacketed;
  static public ItemRoundHollow item_round_hollow;
  static public ItemRoundFire item_round_fire;
  static public ItemRoundPoison item_round_poison;
  static public ItemRoundAP item_round_ap;
  static public ItemShellNormal item_shell;
  static public ItemShellAP item_shell_ap;

  
  static public ItemRefractoryFluidContainer item_container;

  
  static public void registerItems(Configuration config)
  {
    item_component = new ItemComponent();
    item_mold = new ItemMold();
    item_revolver = new ItemRevolver();
    item_shotgun = new ItemShotgun();
    item_round = new ItemRoundNormal();
    item_round_jacketed = new ItemRoundJacketed();
    item_round_hollow = new ItemRoundHollow();
    item_round_fire = new ItemRoundFire();
    item_round_poison = new ItemRoundPoison();
    item_round_ap = new ItemRoundAP();
    item_shell = new ItemShellNormal();
    item_shell_ap = new ItemShellAP();
    
    GameRegistry.registerItem(item_component, "component");
    GameRegistry.registerItem(item_mold, "mold");
    GameRegistry.registerItem(item_revolver, "revolver");
    GameRegistry.registerItem(item_shotgun, "shotgun");
    GameRegistry.registerItem(item_round, "round");
    GameRegistry.registerItem(item_round_jacketed, "roundJacketed");
    GameRegistry.registerItem(item_round_hollow, "roundHollow");
    GameRegistry.registerItem(item_round_fire, "roundFire");
    GameRegistry.registerItem(item_round_poison, "roundPoison");
    GameRegistry.registerItem(item_shell, "shell");
    GameRegistry.registerItem(item_round_ap, "roundAP");
    GameRegistry.registerItem(item_shell_ap, "shellAP");
    

    item_container = new ItemRefractoryFluidContainer(FluidContainerRegistry.BUCKET_VOLUME);
    GameRegistry.registerItem(item_container, "fluidContainer");
  }

  static public ItemStack component(int dv)
  {
    return component(dv,1);
  }

  static public ItemStack component(int dv,int amount)
  {
    return new ItemStack(item_component,amount,dv);
  }

  static public ItemStack mold(int dv)
  {
    return mold(dv,1);
  }

  static public ItemStack mold(int dv,int amount)
  {
    return new ItemStack(item_mold,amount,dv);
  }
}
