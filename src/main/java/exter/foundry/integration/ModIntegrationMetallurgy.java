package exter.foundry.integration;

//import com.teammetallurgy.metallurgy.api.IMetalSet;
//import com.teammetallurgy.metallurgy.api.MetallurgyApi;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ModIntegrationMetallurgy extends ModIntegration
{

  public ModIntegrationMetallurgy(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void onPreInit(Configuration config)
  {

  }

  @Override
  public void onInit()
  {

  }
  

  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("Metallurgy"))
    {
      is_loaded = false;
      return;
    }
//
//    ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
//    ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
//    for(String setname:MetallurgyApi.getSetNames())
//    {
//      IMetalSet metalset = MetallurgyApi.getMetalSet(setname);
//      for(String metalname:metalset.getMetalNames())
//      {
//        Fluid liquid_metal = LiquidMetalRegistry.instance.getFluid(metalname.replace(" ", ""));
//        if(liquid_metal != null)
//        {
//          if(!metalname.equals("Gold") && !metalname.equals("Midasium"))
//          {
//            AlloyMixerRecipeManager.instance.addRecipe(
//                new FluidStack(
//                    LiquidMetalRegistry.instance.getFluid("Gold"),
//                    1),
//                new FluidStack[] {
//                  new FluidStack(
//                      LiquidMetalRegistry.instance.getFluid("Midasium"),
//                      1),
//                  new FluidStack(liquid_metal, 1)
//                });
//
//          }
//        
//          RegisterCasting(metalset.getIngot(metalname),liquid_metal,1,ItemMold.MOLD_INGOT,null);
//          RegisterCasting(metalset.getBlock(metalname),liquid_metal,9,ItemMold.MOLD_BLOCK,null);
//
//          if(FoundryConfig.recipe_tools_armor)
//          {
//            ItemStack pickaxe = metalset.getPickaxe(metalname);
//            ItemStack axe = metalset.getAxe(metalname);
//            ItemStack shovel = metalset.getShovel(metalname);
//            ItemStack hoe = metalset.getHoe(metalname);
//            ItemStack sword = metalset.getSword(metalname);
//            ItemStack helmet = metalset.getHelmet(metalname);
//            ItemStack chestplate = metalset.getChestplate(metalname);
//            ItemStack leggings = metalset.getLeggings(metalname);
//            ItemStack boots = metalset.getBoots(metalname);
//
//            RegisterCasting(axe,liquid_metal,3,ItemMold.MOLD_AXE,extra_sticks2);
//            RegisterCasting(sword,liquid_metal,2,ItemMold.MOLD_SWORD,extra_sticks1);
//            RegisterCasting(pickaxe,liquid_metal,3,ItemMold.MOLD_PICKAXE,extra_sticks2);
//            RegisterCasting(shovel,liquid_metal,1,ItemMold.MOLD_SHOVEL,extra_sticks2);
//            RegisterCasting(hoe,liquid_metal,2,ItemMold.MOLD_HOE,extra_sticks2);
//            RegisterCasting(helmet,liquid_metal,5,ItemMold.MOLD_HELMET,null);
//            RegisterCasting(chestplate,liquid_metal,8,ItemMold.MOLD_CHESTPLATE,null);
//            RegisterCasting(leggings,liquid_metal,7,ItemMold.MOLD_LEGGINGS,null);
//            RegisterCasting(boots,liquid_metal,4,ItemMold.MOLD_BOOTS,null);
//            
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, chestplate);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, leggings);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, helmet);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, boots);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, pickaxe);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, axe);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, shovel);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, hoe);
//            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, sword); 
//          }
//        }
//      }
//    }
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("DamascusSteel"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Iron"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Bronze"),
//              1)
//        });
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Angmallen"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Iron"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Gold"),
//              1)
//        });
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Haderoth"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Mithril"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Rubracium"),
//              1)
//        });
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Tartarite"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Adamantine"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Atlarus"),
//              1)
//        });
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Celenegil"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Orichalcum"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Platinum"),
//              1)
//        });
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Quicksilver"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Mithril"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Silver"),
//              1)
//        });
//    
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Desichalkos"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Eximite"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Meutoite"),
//              1)
//        });
//
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Inolashite"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Alduorite"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Ceruclase"),
//              1)
//        });
//    
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("Amordrine"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Kalendrite"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Platinum"),
//              1)
//        });
//    
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("ShadowSteel"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("ShadowIron"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Lemurite"),
//              1)
//        });
//
//    
//    AlloyMixerRecipeManager.instance.addRecipe(
//        new FluidStack(
//            LiquidMetalRegistry.instance.getFluid("BlackSteel"),
//            2),
//        new FluidStack[] {
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("Infuscolium"),
//              1),
//          new FluidStack(
//              LiquidMetalRegistry.instance.getFluid("DeepIron"),
//              1)
//        });
  }
}
