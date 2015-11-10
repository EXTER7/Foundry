package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationEnderIO extends ModIntegration
{
  private Fluid liquid_redstone_alloy;
  private Fluid liquid_energetic_alloy;
  private Fluid liquid_vibrant_alloy;
  private Fluid liquid_dark_steel;
  private Fluid liquid_electrical_steel;
  private Fluid liquid_phased_iron;
  
  public ModIntegrationEnderIO(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    liquid_redstone_alloy = LiquidMetalRegistry.instance.RegisterLiquidMetal( "RedstoneAlloy", 1000, 14);
    liquid_energetic_alloy = LiquidMetalRegistry.instance.RegisterLiquidMetal( "EnergeticAlloy", 2500, 15);    
    liquid_vibrant_alloy = LiquidMetalRegistry.instance.RegisterLiquidMetal( "VibrantAlloy", 2500, 15);    
    liquid_dark_steel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "DarkSteel", 1850, 12);
    liquid_electrical_steel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "ElectricalSteel", 1850, 15);    
    liquid_phased_iron = LiquidMetalRegistry.instance.RegisterLiquidMetal( "PhasedIron", 1850, 15);
    Fluid liquid_soularium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Soularium", 1350, 12);
    
    FoundryUtils.RegisterBasicMeltingRecipes( "RedstoneAlloy", liquid_redstone_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "EnergeticAlloy", liquid_energetic_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "VibrantAlloy", liquid_vibrant_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "PhasedGold", liquid_vibrant_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "DarkSteel", liquid_dark_steel);    
    FoundryUtils.RegisterBasicMeltingRecipes( "PhasedIron", liquid_phased_iron);
    FoundryUtils.RegisterBasicMeltingRecipes( "ElectricalSteel", liquid_electrical_steel);    
    FoundryUtils.RegisterBasicMeltingRecipes( "Soularium", liquid_soularium);    
  }

  @Override
  public void OnInit()
  {
    
  }
  

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("EnderIO"))
    {
      is_loaded = false;
      return;
    }
    


    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);


      ItemStack dark_steel_pickaxe = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_pickaxe"));
      ItemStack dark_steel_axe = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_axe"));
      ItemStack dark_steel_shovel = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_shovel"));
      ItemStack dark_steel_sword = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_sword"));

      ItemStack dark_steel_helmet = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_helmet"));
      ItemStack dark_steel_chestplate = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_chestplate"));
      ItemStack dark_steel_leggings = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_leggings"));
      ItemStack dark_steel_boots = newItemStack(GameRegistry.findItem("EnderIO", "item.darkSteel_boots"));

      RegisterCasting(dark_steel_chestplate, liquid_dark_steel, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(dark_steel_helmet, liquid_dark_steel, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(dark_steel_leggings, liquid_dark_steel, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(dark_steel_boots, liquid_dark_steel, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(dark_steel_pickaxe, liquid_dark_steel, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(dark_steel_axe, liquid_dark_steel, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(dark_steel_shovel, liquid_dark_steel, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(dark_steel_sword, liquid_dark_steel, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, dark_steel_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, dark_steel_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, dark_steel_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, dark_steel_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, dark_steel_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, dark_steel_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, dark_steel_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, dark_steel_sword);
    }
    
    Fluid liquid_redstone = LiquidMetalRegistry.instance.GetFluid("Redstone");
    Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
    Fluid liquid_ender = FluidRegistry.getFluid("ender");
    Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
    
    InfuserRecipeManager.instance.AddSubstanceRecipe(
        new InfuserSubstance("silicon",36),
        new ItemStack(GameRegistry.findItem("EnderIO","itemMaterial"),1,0), 6000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(
        new InfuserSubstance("silicon",36),
        new ItemStack(Blocks.sand,1,0), 240000);

    InfuserRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_redstone_alloy,3),
        new FluidStack(liquid_redstone,3),
        new InfuserSubstance("silicon", 1));

    InfuserRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_electrical_steel,3),
        new FluidStack(FoundryRecipes.liquid_steel,3),
        new InfuserSubstance("silicon", 1));

    if(destabilized_redstone != null)
    {
      InfuserRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_redstone_alloy,27),
          new FluidStack(destabilized_redstone,25),
          new InfuserSubstance("silicon", 9));
    }
    
    if(liquid_glowstone != null)
    {
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_energetic_alloy, 54),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_gold, 54),
            new FluidStack(liquid_redstone, 54),
            new FluidStack(liquid_glowstone, 125)
          });
      
      if(destabilized_redstone != null)
      {
        AlloyMixerRecipeManager.instance.AddRecipe(
            new FluidStack(liquid_energetic_alloy, 54),
            new FluidStack[] {
              new FluidStack(FoundryRecipes.liquid_gold, 54),
              new FluidStack(destabilized_redstone, 50),
              new FluidStack(liquid_glowstone, 125)
            });
      }
    }

    if(liquid_ender != null)
    {
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_vibrant_alloy, 54),
          new FluidStack[] {
            new FluidStack(liquid_energetic_alloy, 54),
            new FluidStack(liquid_ender, 125)
          });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_phased_iron, 54),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_iron, 54),
            new FluidStack(liquid_ender, 125)
          });
    }

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_dark_steel, 27),
        new FluidStack[] {
          new FluidStack(FoundryRecipes.liquid_steel, 27),
          new FluidStack(FluidRegistry.LAVA, 250),
        });
    
    CastingRecipeManager.instance.AddRecipe(
        "ingotPhasedGold",
        new FluidStack(liquid_vibrant_alloy,FoundryAPI.FLUID_AMOUNT_INGOT), FoundryItems.Mold(ItemMold.MOLD_INGOT), null);
    CastingRecipeManager.instance.AddRecipe(
        "blockPhasedGold",
        new FluidStack(liquid_vibrant_alloy,FoundryAPI.FLUID_AMOUNT_BLOCK), FoundryItems.Mold(ItemMold.MOLD_BLOCK), null);
  }
}
