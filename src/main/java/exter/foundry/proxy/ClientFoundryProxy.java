package exter.foundry.proxy;

import java.util.List;
import java.util.Map;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.registry.FluidLiquidMetal;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
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
    //TODO
    //ModelLoaderRegistry.registerLoader(RFCModel.Loader.instance);
    MaterialRegistry.instance.initIcons();
    for(Map.Entry<String,FluidLiquidMetal> e:LiquidMetalRegistry.instance.getFluids().entrySet())
    {
      Fluid fluid = e.getValue();
      Block block = fluid.getBlock();
      Item item = Item.getItemFromBlock(block);
      String name = e.getKey();
      ModelBakery.registerItemVariants(item);
      ModelLoader.setCustomMeshDefinition( item, new LiquidMetalItemMeshDefinition(name));
      ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(BlockFluidBase.LEVEL).build());
    }
    RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonGun.class, 
        new IRenderFactory<EntitySkeleton>() { @Override public Render<EntitySkeleton> createRenderFor(RenderManager manager) { return new RenderSkeleton(manager); }});
    ModIntegrationManager.clientPreInit();
  }

  
  @Override
  public void init()
  {
    for(BlockFoundryMachine.EnumMachine m:BlockFoundryMachine.EnumMachine.values())
    {
      registerItemModel(FoundryBlocks.block_machine,m.model,m.id);
    }

    registerItemModel(FoundryBlocks.block_alloy_furnace,"alloyFurnace");
    registerItemModel(FoundryBlocks.block_component,"casing");
    registerItemModel(FoundryBlocks.block_refractory_hopper,"refractoryHopper");


    for(ItemComponent.SubItem c:ItemComponent.SubItem.values())
    {
      registerItemModel(FoundryItems.item_component,c.name, c.id);
    }

    for(ItemMold.SubItem m:ItemMold.SubItem.values())
    {
      registerItemModel(FoundryItems.item_mold,m.name, m.id);
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
