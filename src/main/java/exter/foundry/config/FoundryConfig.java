package exter.foundry.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


public class FoundryConfig
{
  public static boolean debug;

  public static boolean recipe_steel;
  
  public static boolean recipe_equipment;
  public static boolean recipe_glass;
  
  public static boolean block_cokeoven;

  static public boolean getAndRemove(Configuration config,String category,String name,boolean def)
  {
    boolean res = config.get(category, name, def).getBoolean(def);
    config.getCategory(category).remove(name);
    return res;
  }
  
  static public void load(Configuration config)
  {
	  
    //Old config values
    recipe_equipment = getAndRemove(config, "recipe", "recipe.tools_armor.use", true);
    recipe_glass = getAndRemove(config, "recipe", "recipe.glass.use", true);
    recipe_steel = getAndRemove(config, "recipe", "recipe.enable_infuser_steel", true);
    block_cokeoven = getAndRemove(config, "block", "block.enable_coke_ovenl", true);
    if(config.hasCategory("recipe"))
    {
      config.removeCategory(config.getCategory("recipe"));
    }

    
    debug = config.getBoolean("debug", "debug", false, "Enable debug logging.");
    recipe_equipment = config.getBoolean("equipment", "recipes", recipe_equipment, "Enable/disable casting recipes for equipment");
    recipe_glass = config.getBoolean("glass", "recipes", recipe_glass, "Enable/disable glass melting and casting recipes");
    recipe_steel = config.getBoolean("steel", "recipes", recipe_steel, "Enable/disable steel infuser recipes");
    block_cokeoven = config.getBoolean("coke_oven", "block", block_cokeoven, "Enable/disable Coke Oven block");
  }
}
