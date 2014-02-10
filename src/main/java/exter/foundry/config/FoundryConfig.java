package exter.foundry.config;

import java.util.Map;

import net.minecraftforge.common.Configuration;

public class FoundryConfig
{
  public static boolean worldgen_copper;
  public static boolean worldgen_tin;
  public static boolean worldgen_zinc;
  public static boolean worldgen_nickel;
  public static boolean worldgen_silver;
  public static boolean worldgen_lead;

  public static int recipe_alloy_bronze_yield;
  public static int recipe_alloy_brass_yield;
  public static int recipe_alloy_invar_yield;
  public static int recipe_alloy_electrum_yield;
  
  public static boolean recipe_gear_useoredict;
  
  public static Map<String,Integer> recipe_melting_gears;
  public static Map<String,Integer> recipe_casting_gears;
  
  public static boolean recipe_tools_armor;
  public static boolean recipe_glass;
  
  static public void Load(Configuration config)
  {
	  
    recipe_tools_armor = config.get("recipe", "recipe.tools_armor.use", true).getBoolean(true);
    recipe_glass = config.get("recipe", "recipe.glass.use", true).getBoolean(true);
    worldgen_copper = config.get("worldgen", "copper", true).getBoolean(true);
    worldgen_tin = config.get("worldgen", "tin", true).getBoolean(true);
    worldgen_zinc = config.get("worldgen", "zinc", true).getBoolean(true);
    worldgen_nickel = config.get("worldgen", "nickel", true).getBoolean(true);
    worldgen_silver = config.get("worldgen", "silver", true).getBoolean(true);
    worldgen_lead = config.get("worldgen", "lead", true).getBoolean(true);

    recipe_alloy_bronze_yield = config.get("recipe", "alloy.bronze_yield", 4).getInt(4);
    recipe_alloy_brass_yield = config.get("recipe", "alloy.brass_yield", 4).getInt(4);
    recipe_alloy_invar_yield = config.get("recipe", "alloy.invar_yield", 3).getInt(3);
    recipe_alloy_electrum_yield = config.get("recipe", "alloy.electrum_yield", 2).getInt(2);
    
    recipe_gear_useoredict = config.get("recipe", "recipe.gear_use_oredictionary", false).getBoolean(false);
    
    
    if(recipe_alloy_bronze_yield < 0 || recipe_alloy_bronze_yield > 4)
    {
      recipe_alloy_bronze_yield = 4;
    }
    if(recipe_alloy_brass_yield < 0 || recipe_alloy_brass_yield > 4)
    {
      recipe_alloy_brass_yield = 4;
    }
    if(recipe_alloy_invar_yield < 0 || recipe_alloy_invar_yield > 3)
    {
      recipe_alloy_invar_yield = 3;
    }
    if(recipe_alloy_electrum_yield < 0 || recipe_alloy_electrum_yield > 2)
    {
      recipe_alloy_electrum_yield = 2;
    }
  }
}
