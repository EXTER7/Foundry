package exter.foundry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.integration.ModIntegration;
import exter.foundry.integration.ModIntegrationBotania;
import exter.foundry.integration.ModIntegrationBuildcraft;
import exter.foundry.integration.ModIntegrationForestry;
import exter.foundry.integration.ModIntegrationGregtech;
import exter.foundry.integration.ModIntegrationIC2;
import exter.foundry.integration.ModIntegrationMetallurgy;
import exter.foundry.integration.ModIntegrationProjectRed;
import exter.foundry.integration.ModIntegrationRailcraft;
import exter.foundry.integration.ModIntegrationRedstoneArsenal;
import exter.foundry.integration.ModIntegrationTE4;
import exter.foundry.integration.ModIntegrationTF;
import exter.foundry.integration.ModIntegrationThaumcraft;
import exter.foundry.integration.ModIntegrationTiCon;
import exter.foundry.integration.ModIntegrationTwilightForest;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.network.FoundryNetworkChannel;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.ItemRegistry;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.worldgen.FoundryWorldGenerator;
import exter.foundry.worldgen.WordGenOre;

@Mod(
  modid = ModFoundry.MODID,
  name = ModFoundry.MODNAME,
  version = ModFoundry.MODVERSION,
  dependencies = "required-after:Forge@[10.13.4.1448,);"
      + "after:TConstruct;"
      + "after:BuildCraft|Core;"
      + "after:Railcraft;"
      + "after:ThermalExpansion;"
      + "after:RedstoneArsenal;"
      + "after:IC2;"
      + "after:Forestry;"
      + "after:gregtech;"
      + "after:Thaumcraft;"
      + "after:Botania"
)
public class ModFoundry
{
  public static final String MODID = "foundry";
  public static final String MODNAME = "Foundry";
  public static final String MODVERSION = "1.1.0.0-beta1";

  @Instance(MODID)
  public static ModFoundry instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(
    clientSide = "exter.foundry.proxy.ClientFoundryProxy",
    serverSide = "exter.foundry.proxy.CommonFoundryProxy"
  )
  public static CommonFoundryProxy proxy;

  
  public static Logger log = LogManager.getLogger(MODNAME);

  public CraftingEvents crafting_events;
  
  public static FoundryNetworkChannel network_channel;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    ModIntegration.RegisterIntegration(config,ModIntegrationIC2.class,"ic2");
    ModIntegration.RegisterIntegration(config,ModIntegrationBuildcraft.class,"buildcraft");
    ModIntegration.RegisterIntegration(config,ModIntegrationForestry.class,"forestry");
    ModIntegration.RegisterIntegration(config,ModIntegrationRailcraft.class,"railcraft");
    ModIntegration.RegisterIntegration(config,ModIntegrationTF.class,"tf");
    ModIntegration.RegisterIntegration(config,ModIntegrationTE4.class,"te4");
    ModIntegration.RegisterIntegration(config,ModIntegrationRedstoneArsenal.class,"redarsenal");
    ModIntegration.RegisterIntegration(config,ModIntegrationTiCon.class,"ticon");
    ModIntegration.RegisterIntegration(config,ModIntegrationGregtech.class,"gregtech");
    ModIntegration.RegisterIntegration(config,ModIntegrationThaumcraft.class,"thaumcraft");
    ModIntegration.RegisterIntegration(config,ModIntegrationBotania.class,"botania");
    ModIntegration.RegisterIntegration(config,ModIntegrationMetallurgy.class,"metallurgy");
    ModIntegration.RegisterIntegration(config,ModIntegrationTwilightForest.class,"twf");
    ModIntegration.RegisterIntegration(config,ModIntegrationProjectRed.class,"projectred");
    

    FoundryAPI.items = ItemRegistry.instance;
    FoundryAPI.fluids = LiquidMetalRegistry.instance;
    
    FoundryAPI.recipes_melting = MeltingRecipeManager.instance;
    FoundryAPI.recipes_casting = CastingRecipeManager.instance;
    FoundryAPI.recipes_alloymixer = AlloyMixerRecipeManager.instance;
    FoundryAPI.recipes_infuser = InfuserRecipeManager.instance;
    FoundryAPI.recipes_alloyfurnace = AlloyFurnaceRecipeManager.instance;


    
    OreDictionary.registerOre("ingotIron", Items.iron_ingot);
    OreDictionary.registerOre("blockIron", Blocks.iron_block);
    OreDictionary.registerOre("ingotGold", Items.gold_ingot);
    OreDictionary.registerOre("blockGold", Blocks.gold_block);
    OreDictionary.registerOre("nuggetGold", Items.gold_nugget);
    OreDictionary.registerOre("dustRedstone", Items.redstone);
    OreDictionary.registerOre("blockRedstone", Blocks.redstone_block);


    FoundryConfig.Load(config);
    FoundryItems.RegisterItems(config);
    FoundryBlocks.RegisterBlocks(config);

    OreDictionary.registerOre("dustSmallGunpowder", FoundryItems.Component(ItemComponent.COMPONENT_GUNPOWDER_SMALL));
    OreDictionary.registerOre("dustSmallBlaze", FoundryItems.Component(ItemComponent.COMPONENT_BLAZEPOWDER_SMALL));

    FoundryRecipes.PreInit();
    
    ModIntegration.PreInit(config);

    
    config.save();

    crafting_events = new CraftingEvents();
    
    network_channel = new FoundryNetworkChannel();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    proxy.PreInit();
  }
  
 
  @SuppressWarnings("unchecked")
  @EventHandler
  public void load(FMLInitializationEvent event)
  {
    ModIntegration.Init();
    
    GameRegistry.registerTileEntity(TileEntityInductionCrucibleFurnace.class, "Foundry_ICF");
    GameRegistry.registerTileEntity(TileEntityMetalCaster.class, "Foundry_MetalCaster");
    GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, "Foundry_AlloyMixer");
    GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, "Foundry_MetalInfuser");
    GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, "Foundry_AlloyFurnace");
    GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, "Foundry_MaterialRouter");
    GameRegistry.registerTileEntity(TileEntityRefractoryHopper.class, "Foundry_RefractoryHopper");


    FoundryRecipes.Init();

    if(FoundryConfig.worldgen_copper)
    {
      WordGenOre.RegisterOre(16, 80, 12, FoundryBlocks.block_ore, BlockFoundryOre.ORE_COPPER);
    }
    if(FoundryConfig.worldgen_tin)
    {
      WordGenOre.RegisterOre(16, 52, 8, FoundryBlocks.block_ore, BlockFoundryOre.ORE_TIN);
    }
    if(FoundryConfig.worldgen_zinc)
    {
      WordGenOre.RegisterOre(8, 48, 6, FoundryBlocks.block_ore, BlockFoundryOre.ORE_ZINC);
    }
    if(FoundryConfig.worldgen_nickel)
    {
      WordGenOre.RegisterOre(8, 36, 5, FoundryBlocks.block_ore, BlockFoundryOre.ORE_NICKEL);
    }
    if(FoundryConfig.worldgen_silver)
    {
      WordGenOre.RegisterOre(2, 30, 3, FoundryBlocks.block_ore, BlockFoundryOre.ORE_SILVER);
    }
    if(FoundryConfig.worldgen_lead)
    {
      WordGenOre.RegisterOre(8, 48, 5, FoundryBlocks.block_ore, BlockFoundryOre.ORE_LEAD);
    }
    GameRegistry.registerWorldGenerator(new FoundryWorldGenerator(),0);

    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET),1,5,8));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),1,5,7));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING),1,5,8));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_PELLET),1,5,7));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),1,5,7));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_GUN_BARREL),1,3,6));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_DRUM),1,2,6));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_FRAME),1,2,6));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_PUMP),1,2,5));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_FRAME),1,2,5));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(FoundryItems.item_revolver.Empty(),1,1,2));
    ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,new WeightedRandomChestContent(new ItemStack(FoundryItems.item_round),4,16,3));

    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET),1,5,12));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),1,5,10));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING),1,5,12));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_PELLET),1,5,11));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),1,5,11));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_GUN_BARREL),1,3,9));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_DRUM),1,2,9));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_FRAME),1,2,9));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_PUMP),1,2,8));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_FRAME),1,2,8));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(FoundryItems.item_revolver.Empty(),1,1,2));
    ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(new ItemStack(FoundryItems.item_round),4,16,3));

    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET),1,5,14));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),1,5,12));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING),1,5,14));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_PELLET),1,5,13));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),1,5,13));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_GUN_BARREL),1,3,11));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_DRUM),1,2,11));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_FRAME),1,2,11));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_PUMP),1,2,10));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_FRAME),1,2,10));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(FoundryItems.item_revolver.Empty(),1,1,2));
    ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,new WeightedRandomChestContent(new ItemStack(FoundryItems.item_round),4,16,3));

    EntityRegistry.registerModEntity(EntitySkeletonGun.class, "gunSkeleton", 0, this, 80, 1, true);
    EntityRegistry.addSpawn(EntitySkeletonGun.class, 5, 0, 1, EnumCreatureType.creature,
        ((Set<BiomeGenBase>)BiomeGenBase.explorationBiomesList).toArray(new BiomeGenBase[BiomeGenBase.explorationBiomesList.size()]));
    
    proxy.Init();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    ModIntegration.PostInit();
    FoundryRecipes.PostInit();
    proxy.PostInit();
    ModIntegration.AfterPostInit();
  }
}
