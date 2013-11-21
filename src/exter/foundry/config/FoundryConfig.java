package exter.foundry.config;

import net.minecraftforge.common.Configuration;

public class FoundryConfig
{
  public static boolean wordgen_copper;
  public static boolean wordgen_tin;
  public static boolean wordgen_zinc;
  public static boolean wordgen_nickel;
  public static boolean wordgen_silver;
  public static boolean wordgen_lead;

  public static int recipe_bronze_yield;
  public static int recipe_brass_yield;
  public static int recipe_invar_yield;
  public static int recipe_electrum_yield;
  
  static public void Load(Configuration config)
  {
    wordgen_copper = config.get("worldgen", "copper", true).getBoolean(true);
    wordgen_tin = config.get("worldgen", "tin", true).getBoolean(true);
    wordgen_zinc = config.get("worldgen", "zinc", true).getBoolean(true);
    wordgen_nickel = config.get("worldgen", "nickel", true).getBoolean(true);
    wordgen_silver = config.get("worldgen", "silver", true).getBoolean(true);
    wordgen_lead = config.get("worldgen", "lead", true).getBoolean(true);

    recipe_bronze_yield = config.get("recipe", "bronze_yield", 4).getInt(4);
    recipe_brass_yield = config.get("recipe", "brass_yield", 4).getInt(4);
    recipe_invar_yield = config.get("recipe", "invar_yield", 3).getInt(3);
    recipe_electrum_yield = config.get("recipe", "electrum_yield", 2).getInt(2);
    if(recipe_bronze_yield < 1 || recipe_bronze_yield > 4)
    {
      recipe_bronze_yield = 4;
    }
    if(recipe_brass_yield < 1 || recipe_brass_yield > 4)
    {
      recipe_brass_yield = 4;
    }
    if(recipe_invar_yield < 1 || recipe_invar_yield > 3)
    {
      recipe_invar_yield = 3;
    }
    if(recipe_electrum_yield < 1 || recipe_electrum_yield > 2)
    {
      recipe_electrum_yield = 2;
    }
  }
}
