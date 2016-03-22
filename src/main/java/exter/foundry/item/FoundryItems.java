package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

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
  
  @Deprecated
  static public final Map<String,ItemStack> ingot_stacks = new HashMap<String,ItemStack>();
  @Deprecated
  static public final Map<String,ItemStack> dust_stacks = new HashMap<String,ItemStack>();
  @Deprecated
  static public final Map<String,ItemStack> nugget_stacks = new HashMap<String,ItemStack>();

  
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
    
    GameRegistry.registerItem(item_component, "foundryComponent");
    GameRegistry.registerItem(item_mold, "foundryMold");
    GameRegistry.registerItem(item_revolver, "foundryRevolver");
    GameRegistry.registerItem(item_shotgun, "foundryShotgun");
    GameRegistry.registerItem(item_round, "foundryRound");
    GameRegistry.registerItem(item_round_jacketed, "foundryRoundJacketed");
    GameRegistry.registerItem(item_round_hollow, "foundryRoundHollow");
    GameRegistry.registerItem(item_round_fire, "foundryRoundFire");
    GameRegistry.registerItem(item_round_poison, "foundryRoundPoison");
    GameRegistry.registerItem(item_shell, "foundryShell");
    GameRegistry.registerItem(item_round_ap, "foundryRoundAP");
    GameRegistry.registerItem(item_shell_ap, "foundryShellAP");
    

    item_container = new ItemRefractoryFluidContainer(FluidContainerRegistry.BUCKET_VOLUME);
    GameRegistry.registerItem(item_container, "foundryContainer");
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
