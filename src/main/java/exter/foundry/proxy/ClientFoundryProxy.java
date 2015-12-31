package exter.foundry.proxy;

import java.util.List;
import java.util.Map;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.BlockMetal;
import exter.foundry.block.BlockMetalSlab;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.model.RFCModel;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class ClientFoundryProxy extends CommonFoundryProxy
{
  static private class LiquidMetalItemMeshDefinition implements ItemMeshDefinition
  {
    private ModelResourceLocation model;
    
    LiquidMetalItemMeshDefinition(String name)
    {
      model = new ModelResourceLocation("foundry:liquid" + name);
    }
    
    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
      return model;
    }
    
  }
 
  static private ResourceLocation SUBSTANCES_TEXTURE = new ResourceLocation("foundry:textures/gui/infuser_substances.png");
  
  
  private void registerItemModel(Block block,String name)
  {
    registerItemModel(Item.getItemFromBlock(block), name);
  }

  private void registerItemModel(Block block,String name,int meta)
  {
    registerItemModel(Item.getItemFromBlock(block), name, meta);
  }

  private void registerItemModel(Item item,String name)
  {
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    .register(item, 0,
      new ModelResourceLocation("foundry:" + name, "inventory"));
  }

  private void registerItemModel(Item item,String name,int meta)
  {
    name = "foundry:" + name;
    ModelBakery.registerItemVariants(item, new ResourceLocation(name));
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    .register(item, meta, new ModelResourceLocation(name, "inventory"));
  }

  @Override
  public void preInit()
  {
    ModelLoaderRegistry.registerLoader(RFCModel.Loader.instance);
    MaterialRegistry.instance.initIcons();
    InfuserRecipeManager.instance.InitTextures();
    for(Map.Entry<String,Fluid> e:LiquidMetalRegistry.instance.getFluids().entrySet())
    {
      Fluid fluid = e.getValue();
      Block block = fluid.getBlock();
      Item item = Item.getItemFromBlock(block);
      String name = e.getKey();
      ModelBakery.registerItemVariants(item);
      ModelLoader.setCustomMeshDefinition( item, new LiquidMetalItemMeshDefinition(name));
      ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(BlockFluidBase.LEVEL).build());
    }
    ModIntegrationManager.clientPreInit();
  }

  @Override
  public void init()
  {
    //MinecraftForgeClient.registerItemRenderer(FoundryItems.item_container, new RendererItemContainer());
    InfuserRecipeManager.instance.registerSubstanceTexture("carbon", SUBSTANCES_TEXTURE, 0, 0);
    int i;
    for(i = 0; i < 15; i++)
    {
      InfuserRecipeManager.instance.registerSubstanceTexture("dye." + i, SUBSTANCES_TEXTURE, 8, 0, ItemDye.dyeColors[i]);
    }   
    InfuserRecipeManager.instance.registerSubstanceTexture("silicon", SUBSTANCES_TEXTURE, 16, 0);

    RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonGun.class, new RenderSkeleton(Minecraft.getMinecraft().getRenderManager()));

    for(BlockFoundryOre.EnumOre ore:BlockFoundryOre.EnumOre.values())
    {
      registerItemModel(FoundryBlocks.block_ore,ore.oredict_name, ore.id);
    }

    for(BlockMetal block:FoundryBlocks.block_metal)
    {
      for(BlockMetal.Variant v:block.getVariants())
      {
        registerItemModel(block,"block" + v.metal, v.id);
      }
    }

    for(BlockMetalSlab block:FoundryBlocks.block_slab)
    {
      for(BlockMetalSlab.Variant v:block.getVariants())
      {
        registerItemModel(block,"slab" + v.metal, block.getBottomVariantMeta(v));
      }
    }

    for(Map.Entry<String, BlockStairs> e:FoundryBlocks.block_metal_stairs.entrySet())
    {
      registerItemModel(e.getValue(),"stairs" + e.getKey());
    }

    for(BlockFoundryMachine.EnumMachine m:BlockFoundryMachine.EnumMachine.values())
    {
      registerItemModel(FoundryBlocks.block_machine,m.model,m.id);
    }

    registerItemModel(FoundryBlocks.block_alloy_furnace,"alloyFurnace");
    registerItemModel(FoundryBlocks.block_refractory_casing,"casing");
    registerItemModel(FoundryBlocks.block_refractory_hopper,"refractoryHopper");

    for(i = 0; i < ItemIngot.OREDICT_NAMES.length; i++)
    {
      registerItemModel(FoundryItems.item_ingot,ItemIngot.OREDICT_NAMES[i], i);
    }

    for(i = 0; i < ItemComponent.REGISTRY_NAMES.length; i++)
    {
      registerItemModel(FoundryItems.item_component,ItemComponent.REGISTRY_NAMES[i], i);
    }

    for(i = 0; i < ItemMold.REGISTRY_NAMES.length; i++)
    {
      registerItemModel(FoundryItems.item_mold,ItemMold.REGISTRY_NAMES[i], i);
    }

    registerItemModel(FoundryItems.item_revolver,"revolver",0);
    registerItemModel(FoundryItems.item_shotgun,"shotgun",0);
    registerItemModel(FoundryItems.item_round,"roundNormal",0);
    registerItemModel(FoundryItems.item_round_hollow,"roundHollow",0);
    registerItemModel(FoundryItems.item_round_jacketed,"roundJacketed",0);
    registerItemModel(FoundryItems.item_round_fire,"roundFire",0);
    registerItemModel(FoundryItems.item_round_poison,"roundPoison",0);
    registerItemModel(FoundryItems.item_round_ap,"roundAP",0);
    registerItemModel(FoundryItems.item_shell,"shellNormal",0);
    registerItemModel(FoundryItems.item_shell_ap,"shellAP",0);

    registerItemModel(FoundryItems.item_container,"container",0);

    ModIntegrationManager.clientInit();
  }
  

  @Override
  public void postInit()
  {
    for(OreDictMaterial material : OreDictMaterial.MATERIALS)
    {
      List<ItemStack> ores = OreDictionary.getOres(material.default_prefix + material.suffix);
      if(ores.size() > 0)
      {
        MaterialRegistry.instance.registerMaterialIcon(material.suffix, ores.get(0));
      } else
      {
        for(OreDictType type : OreDictType.TYPES)
        {
          ores = OreDictionary.getOres(type.prefix + material.suffix);
          if(ores.size() > 0)
          {
            MaterialRegistry.instance.registerMaterialIcon(material.suffix, ores.get(0));
            break;
          }
        }
      }
    }

    for(OreDictType type : OreDictType.TYPES)
    {
      List<ItemStack> ores = OreDictionary.getOres(type.prefix + type.default_suffix);
      if(ores.size() > 0)
      {
        MaterialRegistry.instance.registerTypeIcon(type.name, ores.get(0));
      } else
      {
        for(OreDictMaterial material : OreDictMaterial.MATERIALS)
        {
          ores = OreDictionary.getOres(type.prefix + material.suffix);
          if(ores.size() > 0)
          {
            MaterialRegistry.instance.registerTypeIcon(type.name, ores.get(0));
            break;
          }
        }
      }
    }
    ModIntegrationManager.clientPostInit();
  }

}
