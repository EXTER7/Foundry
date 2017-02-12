package exter.foundry.fluid;

import exter.foundry.config.FoundryConfig;
import net.minecraftforge.fluids.Fluid;

public class FoundryFluids
{
  static public Fluid liquid_iron;
  static public Fluid liquid_gold;
  static public Fluid liquid_copper;
  static public Fluid liquid_tin;
  static public Fluid liquid_bronze;
  static public Fluid liquid_electrum;
  static public Fluid liquid_invar;
  static public Fluid liquid_nickel;
  static public Fluid liquid_zinc;
  static public Fluid liquid_brass;
  static public Fluid liquid_silver;
  static public Fluid liquid_steel;
  static public Fluid liquid_cupronickel;
  static public Fluid liquid_lead;
  static public Fluid liquid_platinum;
  static public Fluid liquid_aluminium;
  static public Fluid liquid_alumina;
  static public Fluid liquid_chromium;
  static public Fluid liquid_signalum;
  static public Fluid liquid_lumium;
  static public Fluid liquid_enderium;
  
  static public void init()
  {
    liquid_iron = LiquidMetalRegistry.instance.registerLiquidMetal( "iron", 1800, 15);
    liquid_gold = LiquidMetalRegistry.instance.registerLiquidMetal( "gold", 1350, 15);
    liquid_copper = LiquidMetalRegistry.instance.registerLiquidMetal( "copper", 1300, 15);
    liquid_tin = LiquidMetalRegistry.instance.registerLiquidMetal( "tin", 550, 0);
    liquid_bronze = LiquidMetalRegistry.instance.registerLiquidMetal( "bronze", 1200, 15);
    liquid_electrum = LiquidMetalRegistry.instance.registerLiquidMetal( "electrum", 1350, 15);
    liquid_invar = LiquidMetalRegistry.instance.registerLiquidMetal( "invar", 1780, 15);
    liquid_nickel = LiquidMetalRegistry.instance.registerLiquidMetal( "nickel", 1750, 15);
    liquid_zinc = LiquidMetalRegistry.instance.registerLiquidMetal( "zinc", 700, 0);
    liquid_brass = LiquidMetalRegistry.instance.registerLiquidMetal( "brass", 1200, 15);
    liquid_silver = LiquidMetalRegistry.instance.registerLiquidMetal( "silver", 1250, 15);
    liquid_steel = LiquidMetalRegistry.instance.registerLiquidMetal( "steel", 1800, 15);
    liquid_cupronickel = LiquidMetalRegistry.instance.registerLiquidMetal( "cupronickel", 1750, 15);
    liquid_lead = LiquidMetalRegistry.instance.registerLiquidMetal( "lead", 650, 0);  
    liquid_platinum = LiquidMetalRegistry.instance.registerLiquidMetal( "platinum", 2100, 15);
    liquid_aluminium = LiquidMetalRegistry.instance.registerLiquidMetal( "aluminium", 900, 0);
    liquid_chromium = LiquidMetalRegistry.instance.registerLiquidMetal( "chrome", 2200, 10);   
    liquid_signalum = LiquidMetalRegistry.instance.registerLiquidMetal( "signalum", 1400, 12);
    liquid_lumium = LiquidMetalRegistry.instance.registerLiquidMetal( "lumium", 1250, 15);
    liquid_enderium = LiquidMetalRegistry.instance.registerLiquidMetal( "enderium", 1900, 12);
    if(!FoundryConfig.recipe_alumina_melts_to_aluminium)
    {
      liquid_alumina = LiquidMetalRegistry.instance.registerLiquidMetal( "alumina", 2100, 12);
    }

    LiquidMetalRegistry.instance.registerLiquidMetal( "manganese", 1550, 15);   
    LiquidMetalRegistry.instance.registerLiquidMetal( "titanium", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "rubber", 460, 0);
    LiquidMetalRegistry.instance.registerLiquidMetal( "stainless_steel", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "kanthal", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "nichrome", 1950, 15);    
  }
}
