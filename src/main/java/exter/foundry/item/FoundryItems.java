package exter.foundry.item;

import exter.foundry.item.ammo.ItemRoundNormal;
import exter.foundry.item.ammo.ItemRoundJacketed;
import exter.foundry.item.ammo.ItemRoundLumium;
import exter.foundry.item.ammo.ItemRoundPoison;
import exter.foundry.item.ammo.ItemRoundSnow;
import exter.foundry.item.ammo.ItemShellAP;
import exter.foundry.item.ammo.ItemShellLumium;
import exter.foundry.item.ammo.ItemShellNormal;
import exter.foundry.item.firearm.ItemRevolver;
import exter.foundry.item.firearm.ItemShotgun;
import exter.foundry.item.ammo.ItemRoundAP;
import exter.foundry.item.ammo.ItemRoundFire;
import exter.foundry.item.ammo.ItemRoundHollow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
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
  static public ItemRoundLumium item_round_lumium;
  static public ItemRoundSnow item_round_snow;
  static public ItemShellNormal item_shell;
  static public ItemShellAP item_shell_ap;
  static public ItemShellLumium item_shell_lumium;

  
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
    item_round_lumium = new ItemRoundLumium();
    item_round_snow = new ItemRoundSnow();
    item_shell = new ItemShellNormal();
    item_shell_ap = new ItemShellAP();
    item_shell_lumium = new ItemShellLumium();
    item_container = new ItemRefractoryFluidContainer();
    
    GameRegistry.register(item_component);
    GameRegistry.register(item_mold);
    GameRegistry.register(item_revolver);
    GameRegistry.register(item_shotgun);
    GameRegistry.register(item_round);
    GameRegistry.register(item_round_jacketed);
    GameRegistry.register(item_round_hollow);
    GameRegistry.register(item_round_fire);
    GameRegistry.register(item_round_poison);
    GameRegistry.register(item_round_ap);
    GameRegistry.register(item_round_lumium);
    GameRegistry.register(item_round_snow);
    GameRegistry.register(item_shell);
    GameRegistry.register(item_shell_ap);
    GameRegistry.register(item_shell_lumium);
    GameRegistry.register(item_container);
  }

  static public ItemStack component(ItemComponent.SubItem sub)
  {
    return component(sub,1);
  }

  static public ItemStack component(ItemComponent.SubItem sub,int amount)
  {
    return new ItemStack(item_component,amount,sub.id);
  }

  static public ItemStack mold(ItemMold.SubItem sub)
  {
    return mold(sub,1);
  }

  static public ItemStack mold(ItemMold.SubItem sub,int amount)
  {
    return new ItemStack(item_mold,amount,sub.id);
  }
}
