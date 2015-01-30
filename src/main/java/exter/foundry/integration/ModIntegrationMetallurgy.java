package exter.foundry.integration;

import com.teammetallurgy.metallurgy.api.IMetalSet;
import com.teammetallurgy.metallurgy.api.MetallurgyApi;

import cpw.mods.fml.common.Loader;
import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationMetallurgy extends ModIntegration
{

  public ModIntegrationMetallurgy(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {

  }

  @Override
  public void OnInit()
  {

  }
  
  private void RegisterCasting(ItemStack item,Fluid liquid_metal,int ingots,int mold_meta,ItemStack extra)
  {
    if(item != null)
    {
      ItemStack mold = new ItemStack(FoundryItems.item_mold, 1, mold_meta);
      if(CastingRecipeManager.instance.FindRecipe(new FluidStack(liquid_metal,FoundryAPI.CASTER_TANK_CAPACITY), mold, extra) == null)
      {
        CastingRecipeManager.instance.AddRecipe(item, new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots), mold, extra);
      }
      FoundryMiscUtils.RegisterMoldRecipe(mold_meta, item);
    }
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Metallurgy"))
    {
      is_loaded = false;
      return;
    }

    for(String setname:MetallurgyApi.getSetNames())
    {
      IMetalSet metalset = MetallurgyApi.getMetalSet(setname);
      for(String metalname:metalset.getMetalNames())
      {
        Fluid liquid_metal = LiquidMetalRegistry.instance.GetFluid(metalname.replace(" ", ""));
        if(liquid_metal != null)
        {
          if(!metalname.equals("Gold") && !metalname.equals("Midasium"))
          {
            AlloyMixerRecipeManager.instance.AddRecipe(
                new FluidStack(
                    LiquidMetalRegistry.instance.GetFluid("Gold"),
                    1),
                new FluidStack[] {
                  new FluidStack(
                      LiquidMetalRegistry.instance.GetFluid("Midasium"),
                      1),
                  new FluidStack(liquid_metal, 1)
                });

          }
        
          RegisterCasting(metalset.getIngot(metalname),liquid_metal,1,ItemMold.MOLD_INGOT,null);
          RegisterCasting(metalset.getBlock(metalname),liquid_metal,9,ItemMold.MOLD_BLOCK,null);

          if(FoundryConfig.recipe_tools_armor)
          {
            ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
            ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);

            RegisterCasting(metalset.getAxe(metalname),liquid_metal,3,ItemMold.MOLD_AXE,extra_sticks2);
            RegisterCasting(metalset.getSword(metalname),liquid_metal,2,ItemMold.MOLD_SWORD,extra_sticks1);
            RegisterCasting(metalset.getPickaxe(metalname),liquid_metal,3,ItemMold.MOLD_PICKAXE,extra_sticks2);
            RegisterCasting(metalset.getShovel(metalname),liquid_metal,1,ItemMold.MOLD_SHOVEL,extra_sticks2);
            RegisterCasting(metalset.getHoe(metalname),liquid_metal,2,ItemMold.MOLD_HOE,extra_sticks2);

            RegisterCasting(metalset.getHelmet(metalname),liquid_metal,5,ItemMold.MOLD_HELMET,null);
            RegisterCasting(metalset.getChestplate(metalname),liquid_metal,8,ItemMold.MOLD_CHESTPLATE,null);
            RegisterCasting(metalset.getLeggings(metalname),liquid_metal,7,ItemMold.MOLD_LEGGINGS,null);
            RegisterCasting(metalset.getBoots(metalname),liquid_metal,4,ItemMold.MOLD_BOOTS,null);
          }
        }
      }
    }

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("DamascusSteel"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Iron"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Bronze"),
              1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Angmallen"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Iron"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Gold"),
              1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Haderoth"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Mithril"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Rubracium"),
              1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Tartarite"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Adamantine"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Atlarus"),
              1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Celenegil"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Orichalcum"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Platinum"),
              1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Quicksilver"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Mithril"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Silver"),
              1)
        });

  }
}
