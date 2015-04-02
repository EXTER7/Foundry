package exter.foundry.config;

import net.minecraftforge.common.config.Configuration;


public class FoundryConfig
{
  public static boolean worldgen_copper;
  public static boolean worldgen_tin;
  public static boolean worldgen_zinc;
  public static boolean worldgen_nickel;
  public static boolean worldgen_silver;
  public static boolean worldgen_lead;

  public static boolean recipe_gear_useoredict;
  public static boolean recipe_steel_enable;
  
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

    recipe_steel_enable = config.get("recipe", "recipe.enable_infuser_steel", true).getBoolean(true);    
    recipe_gear_useoredict = config.get("recipe", "recipe.gear_use_oredictionary", false).getBoolean(false);
  }
}
