package exter.foundry.proxy;

import java.util.List;
import java.util.Map;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.model.RFCModel;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityRefractoryTankBasic;
import exter.foundry.tileentity.TileEntityRefractoryTankStandard;
import exter.foundry.tileentity.TileEntityRefractoryTankAdvanced;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import exter.foundry.tileentity.renderer.CastingTableRenderer;
import exter.foundry.tileentity.renderer.CastingTableRendererBlock;
import exter.foundry.tileentity.renderer.HopperRenderer;
import exter.foundry.tileentity.renderer.SpoutRenderer;
import exter.foundry.tileentity.renderer.TankRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
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
      model = new ModelResourceLocation("foundry:liquid_" + name);
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
    ModelLoaderRegistry.registerLoader(RFCModel.Loader.instance);
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
        new IRenderFactory<EntitySkeleton>() { @Override public Render<AbstractSkeleton> createRenderFor(RenderManager manager) { return new RenderSkeleton(manager); }});
    ModIntegrationManager.clientPreInit();
  }

  
  @Override
  public void init()
  {
    for(BlockFoundryMachine.EnumMachine m:BlockFoundryMachine.EnumMachine.values())
    {
      registerItemModel(FoundryBlocks.block_machine,m.model,m.id);
    }
    for(BlockCastingTable.EnumTable m:BlockCastingTable.EnumTable.values())
    {
      registerItemModel(FoundryBlocks.block_casting_table,m.model,m.id);
    }

    registerItemModel(FoundryBlocks.block_refractory_glass,"refractory_glass");
    registerItemModel(FoundryBlocks.block_alloy_furnace,"alloy_furnace");
    registerItemModel(FoundryBlocks.block_mold_station,"mold_station");
    registerItemModel(FoundryBlocks.block_refractory_hopper,"refractory_hopper");
    registerItemModel(FoundryBlocks.block_burner_heater,"burner_heater");
    registerItemModel(FoundryBlocks.block_refractory_spout,"refractory_spout");
    registerItemModel(FoundryBlocks.block_refractory_tank_basic,"refractory_tank_basic");
    registerItemModel(FoundryBlocks.block_refractory_tank_standard,"refractory_tank_standard");
    registerItemModel(FoundryBlocks.block_refractory_tank_advanced,"refractory_tank_advanced");
    registerItemModel(FoundryBlocks.block_cauldron_bronze,"bronze_cauldron");
    if(FoundryConfig.block_cokeoven)
    {
      registerItemModel(FoundryBlocks.block_coke_oven,"coke_oven");
    }

    for(BlockComponent.EnumVariant v:BlockComponent.EnumVariant.values())
    {
      registerItemModel(FoundryBlocks.block_component,v.model, v.id);
    }


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
    registerItemModel(FoundryItems.item_round,"round_normal",0);
    registerItemModel(FoundryItems.item_round_hollow,"round_hollow",0);
    registerItemModel(FoundryItems.item_round_jacketed,"round_jacketed",0);
    registerItemModel(FoundryItems.item_round_fire,"round_fire",0);
    registerItemModel(FoundryItems.item_round_poison,"round_poison",0);
    registerItemModel(FoundryItems.item_round_ap,"round_ap",0);
    registerItemModel(FoundryItems.item_round_lumium,"round_lumium",0);
    registerItemModel(FoundryItems.item_round_snow,"round_snow",0);
    registerItemModel(FoundryItems.item_shell,"shell_normal",0);
    registerItemModel(FoundryItems.item_shell_ap,"shell_ap",0);
    registerItemModel(FoundryItems.item_shell_lumium,"shell_lumium",0);
    registerItemModel(FoundryItems.item_container,"container",0);

    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableIngot.class, new CastingTableRenderer(6,10,4,12,9,12,"foundry:blocks/castingtable_top_ingot"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTablePlate.class, new CastingTableRenderer(3,13,3,13,11,12,"foundry:blocks/castingtable_top_plate"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableRod.class, new CastingTableRenderer(7,9,2,14,10,12,"foundry:blocks/castingtable_top_rod"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableBlock.class, new CastingTableRendererBlock());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractorySpout.class, new SpoutRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryHopper.class, new HopperRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankBasic.class, new TankRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankStandard.class, new TankRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankAdvanced.class, new TankRenderer());
    
    
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
