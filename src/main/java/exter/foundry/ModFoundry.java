package exter.foundry;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import exter.foundry.api.FoundryAPI;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.item.FoundryItems;
import exter.foundry.network.MessageTileEntitySync;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.sound.FoundrySounds;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityInductionHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMeltingCrucible;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityMoldStation;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractorySpout;

@Mod(
  modid = ModFoundry.MODID,
  name = ModFoundry.MODNAME,
  version = ModFoundry.MODVERSION,
  dependencies = "required-after:Forge@[12.16.0.1858,);required-after:substratum@[1.3.0.0,)"
)
public class ModFoundry
{
  public static final String MODID = "foundry";
  public static final String MODNAME = "Foundry";
  public static final String MODVERSION = "2.0.0.0";

  @Instance(MODID)
  public static ModFoundry instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(
    clientSide = "exter.foundry.proxy.ClientFoundryProxy",
    serverSide = "exter.foundry.proxy.CommonFoundryProxy"
  )
  public static CommonFoundryProxy proxy;

  
  public static Logger log = LogManager.getLogger(MODNAME);
  
  public static SimpleNetworkWrapper network_channel;
  
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    

    FoundryAPI.fluids = LiquidMetalRegistry.instance;
    
    FoundryAPI.recipes_melting = MeltingRecipeManager.instance;
    FoundryAPI.recipes_casting = CastingRecipeManager.instance;
    FoundryAPI.recipes_alloymixer = AlloyMixerRecipeManager.instance;
    FoundryAPI.recipes_infuser = InfuserRecipeManager.instance;
    FoundryAPI.recipes_alloyfurnace = AlloyFurnaceRecipeManager.instance;
    FoundryAPI.recipes_atomizer = AtomizerRecipeManager.instance;
    FoundryAPI.recipes_mold = MoldRecipeManager.instance;

    FoundryConfig.load(config);
    FoundryItems.registerItems(config);
    FoundryBlocks.registerBlocks(config);

    FoundryRecipes.preInit();
    
    ModIntegrationManager.preInit(config);

    
    config.save();

    
    network_channel = NetworkRegistry.INSTANCE.newSimpleChannel("EXTER.FOUNDRY");
    network_channel.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.SERVER);
    network_channel.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.CLIENT);
    
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    proxy.preInit();
  }
  
 
  @EventHandler
  public void load(FMLInitializationEvent event)
  {
    ModIntegrationManager.init();
    FoundrySounds.init();
    
    GameRegistry.registerTileEntity(TileEntityMeltingCrucible.class, "Foundry_MeltingCrucible");
    GameRegistry.registerTileEntity(TileEntityMetalCaster.class, "Foundry_MetalCaster");
    GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, "Foundry_AlloyMixer");
    GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, "Foundry_MetalInfuser");
    GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, "Foundry_AlloyFurnace");
    GameRegistry.registerTileEntity(TileEntityMoldStation.class, "Foundry_MoldStation");
    GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, "Foundry_MaterialRouter");
    GameRegistry.registerTileEntity(TileEntityRefractoryHopper.class, "Foundry_RefractoryHopper");
    GameRegistry.registerTileEntity(TileEntityMetalAtomizer.class, "Foundry_MetalAtomizer");
    GameRegistry.registerTileEntity(TileEntityInductionHeater.class, "Foundry_InductionHeater");
    GameRegistry.registerTileEntity(TileEntityBurnerHeater.class, "Foundry_BurnerHeater");
    GameRegistry.registerTileEntity(TileEntityCastingTableIngot.class, "Foundry_CastingTable_Ingot");
    GameRegistry.registerTileEntity(TileEntityCastingTablePlate.class, "Foundry_CastingTable_Plate");
    GameRegistry.registerTileEntity(TileEntityCastingTableRod.class, "Foundry_CastingTable_Rod");
    GameRegistry.registerTileEntity(TileEntityRefractorySpout.class, "Foundry_RefractorySpout");


    FoundryRecipes.init();
/* TODO: Implement this once the loot API is added to Forge
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET),1,3,8));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),1,5,7));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING),1,5,8));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET),1,5,7));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),1,5,7));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL),1,3,6));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_DRUM),1,1,6));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_FRAME),1,1,6));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_PUMP),1,1,5));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_FRAME),1,1,5));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.item_revolver.empty(),1,1,2));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(new ItemStack(FoundryItems.item_round),4,16,3));

    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET),1,3,12));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),1,2,10));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING),1,3,12));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET),1,5,11));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),1,2,11));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL),1,3,9));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_DRUM),1,1,9));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_FRAME),1,1,9));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_PUMP),1,1,8));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_FRAME),1,1,8));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.item_revolver.empty(),1,1,2));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(new ItemStack(FoundryItems.item_round),4,16,3));

    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET),1,3,14));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),1,2,12));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING),1,3,14));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET),1,5,13));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),1,2,13));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL),1,3,11));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_DRUM),1,2,11));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_FRAME),1,2,11));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_PUMP),1,2,10));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_FRAME),1,2,10));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.item_revolver.empty(),1,1,2));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(new ItemStack(FoundryItems.item_round),4,16,3));
*/
    EntityRegistry.registerModEntity(EntitySkeletonGun.class, "gunSkeleton", 0, this, 80, 1, true);

    List<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();
    for(BiomeDictionary.Type type : BiomeDictionary.Type.values())
    {
      for(BiomeGenBase bio : BiomeDictionary.getBiomesForType(type))
      {
        if(!biomes.contains(bio))
        {
          biomes.add(bio);
        }
      }
    }
    for(BiomeGenBase bio : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.END))
    {
      if(biomes.contains(bio))
      {
        biomes.remove(bio);
      }
    }
    for(BiomeGenBase bio : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.NETHER))
    {
      if(biomes.contains(bio))
      {
        biomes.remove(bio);
      }
    }

    List<BiomeGenBase> toremove = new ArrayList<BiomeGenBase>();
    for(BiomeGenBase bio : biomes)
    {
      boolean remove = true;
      for(BiomeGenBase.SpawnListEntry e : (List<BiomeGenBase.SpawnListEntry>)bio.getSpawnableList(EnumCreatureType.MONSTER))
      {
        if(e.entityClass == EntitySkeleton.class)
        {
          remove = false;
          break;
        }
      }
      if(remove)
      {
        toremove.add(bio);
      }
    }
    biomes.removeAll(toremove);

    EntityRegistry.addSpawn(EntitySkeletonGun.class, 8, 1, 2, EnumCreatureType.MONSTER, biomes.toArray(new BiomeGenBase[0]));
    
    proxy.init();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    ModIntegrationManager.postInit();
    FoundryRecipes.postInit();
    proxy.postInit();
    ModIntegrationManager.afterPostInit();
  }
}
