package exter.foundry.config;

import net.minecraftforge.common.config.Configuration;


public class FoundryConfig
{
  public static boolean debug;

  public static boolean recipe_gear_useoredict;
  public static boolean recipe_steel_enable;
  
  public static boolean recipe_tools_armor;
  public static boolean recipe_glass;
  
  public static boolean legacy_items_enable;
  public static boolean legacy_register_oredict;

  static public void load(Configuration config)
  {
    debug = config.getBoolean("debug", "debug", false, "Enable debug logging.");
	  
    recipe_tools_armor = config.get("recipe", "recipe.tools_armor.use", true).getBoolean(true);
    recipe_glass = config.get("recipe", "recipe.glass.use", true).getBoolean(true);

    recipe_steel_enable = config.get("recipe", "recipe.enable_infuser_steel", true).getBoolean(true);    

    legacy_items_enable = config.get("legacy", "legacy_items_enable", true).getBoolean(true);
    legacy_register_oredict = config.get("legacy", "legacy_register_oredict", true).getBoolean(true);
  }
}
