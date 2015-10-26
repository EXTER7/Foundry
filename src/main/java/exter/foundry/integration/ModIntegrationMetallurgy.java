package exter.foundry.integration;

import com.teammetallurgy.metallurgy.api.IMetalSet;
import com.teammetallurgy.metallurgy.api.MetallurgyApi;

import cpw.mods.fml.common.Loader;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
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
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Adamantine", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Atlarus", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Rubracium", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Haderoth", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Tartarite", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Midasium", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "DamascusSteel", 1850, 13);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Angmallen", 1850, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Quicksilver", 2050, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Orichalcum", 2000, 10);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Celenegil", 2050, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Vyroxeres", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Sanguinite", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Carmot", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Infuscolium", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Meutoite", 2200, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Hepatizon", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Eximite", 2200, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Desichalkos", 2200, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "DeepIron", 1900, 14);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Ceruclase", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "BlackSteel", 1900, 14);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "AstralSilver", 1500, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Amordrine", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Alduorite", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Kalendrite", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Lemurite", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Inolashite", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "ShadowIron", 2000, 12);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "ShadowSteel", 2000, 12);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Oureclase", 1900, 14);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Ignatius", 2100, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Vulcanite", 2100, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Prometheum", 1900, 14);
  }

  @Override
  public void OnInit()
  {

  }
  

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Metallurgy"))
    {
      is_loaded = false;
      return;
    }

    ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
    ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
    for(String setname:MetallurgyApi.getSetNames())
    {
      IMetalSet metalset = MetallurgyApi.getMetalSet(setname);
      if(metalset == null)
      {
        continue;
      }
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
            ItemStack pickaxe = metalset.getPickaxe(metalname);
            ItemStack axe = metalset.getAxe(metalname);
            ItemStack shovel = metalset.getShovel(metalname);
            ItemStack hoe = metalset.getHoe(metalname);
            ItemStack sword = metalset.getSword(metalname);
            ItemStack helmet = metalset.getHelmet(metalname);
            ItemStack chestplate = metalset.getChestplate(metalname);
            ItemStack leggings = metalset.getLeggings(metalname);
            ItemStack boots = metalset.getBoots(metalname);

            RegisterCasting(axe,liquid_metal,3,ItemMold.MOLD_AXE,extra_sticks2);
            RegisterCasting(sword,liquid_metal,2,ItemMold.MOLD_SWORD,extra_sticks1);
            RegisterCasting(pickaxe,liquid_metal,3,ItemMold.MOLD_PICKAXE,extra_sticks2);
            RegisterCasting(shovel,liquid_metal,1,ItemMold.MOLD_SHOVEL,extra_sticks2);
            RegisterCasting(hoe,liquid_metal,2,ItemMold.MOLD_HOE,extra_sticks2);
            RegisterCasting(helmet,liquid_metal,5,ItemMold.MOLD_HELMET,null);
            RegisterCasting(chestplate,liquid_metal,8,ItemMold.MOLD_CHESTPLATE,null);
            RegisterCasting(leggings,liquid_metal,7,ItemMold.MOLD_LEGGINGS,null);
            RegisterCasting(boots,liquid_metal,4,ItemMold.MOLD_BOOTS,null);
            
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, chestplate);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, leggings);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, helmet);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, boots);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, pickaxe);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, axe);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, shovel);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, hoe);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, sword); 
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
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Desichalkos"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Eximite"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Meutoite"),
              1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Inolashite"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Alduorite"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Ceruclase"),
              1)
        });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Amordrine"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Kalendrite"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Platinum"),
              1)
        });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("ShadowSteel"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("ShadowIron"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Lemurite"),
              1)
        });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("BlackSteel"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Infuscolium"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("DeepIron"),
              1)
        });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(
            LiquidMetalRegistry.instance.GetFluid("Hepatizon"),
            2),
        new FluidStack[] {
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Bronze"),
              1),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid("Gold"),
              1)
        });
  }
}
