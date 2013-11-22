package exter.foundry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.container.RefractoryFluidContainerManager;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.api.registry.FoundryRegistry;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.integration.ModIntegration;
import exter.foundry.integration.ModIntegrationBuildcraft;
import exter.foundry.integration.ModIntegrationForestry;
import exter.foundry.integration.ModIntegrationIC2;
import exter.foundry.integration.ModIntegrationRailcraft;
import exter.foundry.integration.ModIntegrationTE3;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemRefractoryFluidContainer;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.recipes.AlloyRecipe;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstance;
import exter.foundry.recipes.InfuserSubstanceRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.ItemRegistry;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.util.FoundryContainerHandler;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.worldgen.FoundryWorldGenerator;
import exter.foundry.worldgen.WordGenOre;

@Mod(
  modid = ModFoundry.MODID,
  name = ModFoundry.MODNAME,
  version = ModFoundry.MODVERSION,
  dependencies = "required-after:Forge@[9.11.1.951,)"
)
@NetworkMod(
  channels = { ModFoundry.CHANNEL },
  clientSideRequired = true,
  serverSideRequired = true,
  packetHandler = FoundryPacketHandler.class
)
public class ModFoundry
{
  public static final String MODID = "foundry";
  public static final String MODNAME = "Foundry";
  public static final String MODVERSION = "0.2.0.0";

  public static final String CHANNEL = "EXTER_Foundry";

  @Instance(MODID)
  public static ModFoundry instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(
    clientSide = "exter.foundry.proxy.ClientFoundryProxy",
    serverSide = "exter.foundry.proxy.CommonFoundryProxy"
  )
  public static CommonFoundryProxy proxy;



  public static Logger log = Logger.getLogger(MODNAME);

  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    int i;
    FoundryRegistry.items = ItemRegistry.instance;
    FoundryRegistry.fluids = LiquidMetalRegistry.instance;
    
    FoundryRecipes.melting = MeltingRecipeManager.instance;
    FoundryRecipes.casting = CastingRecipeManager.instance;
    FoundryRecipes.alloy = AlloyRecipeManager.instance;
    FoundryRecipes.infuser = InfuserRecipeManager.instance;
    RefractoryFluidContainerManager.handler = FoundryContainerHandler.instance;


    OreDictionary.registerOre("ingotIron", Item.ingotIron);
    OreDictionary.registerOre("blockIron", Block.blockIron);
    OreDictionary.registerOre("ingotGold", Item.ingotGold);
    OreDictionary.registerOre("blockGold", Block.blockGold);
    OreDictionary.registerOre("nuggetGold", Item.goldNugget);

    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    FoundryConfig.Load(config);
    FoundryItems.RegisterItems(config);
    FoundryBlocks.RegisterBlocks(config);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Iron", 1850, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Gold", 1350, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Copper", 1400, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Tin", 550, 7);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Bronze", 1400, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Electrum", 1350, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Invar", 1850, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Nickel", 1750, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Zinc", 700, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Brass", 1400, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Silver", 1250, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Steel", 1850, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Lead", 650, 1);

    config.save();



    Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
    Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
    Fluid liquid_zinc = LiquidMetalRegistry.instance.GetFluid("Zinc");
    Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
    Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
    Fluid liquid_nickel = LiquidMetalRegistry.instance.GetFluid("Nickel");
    Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
    Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
    Fluid liquid_invar = LiquidMetalRegistry.instance.GetFluid("Invar");
    Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
    Fluid liquid_brass = LiquidMetalRegistry.instance.GetFluid("Brass");
    Fluid liquid_steel = LiquidMetalRegistry.instance.GetFluid("Steel");

    MeltingRecipeManager.instance.AddRecipe("gearIron", new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearGold", new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearCopper", new FluidStack(liquid_copper,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearTin", new FluidStack(liquid_tin,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearBronze", new FluidStack(liquid_bronze,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearBrass", new FluidStack(liquid_brass,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearInvar", new FluidStack(liquid_invar,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearSteel", new FluidStack(liquid_steel,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
    MeltingRecipeManager.instance.AddRecipe("gearElectrum", new FluidStack(liquid_electrum,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));

    
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_bronze,3 * FoundryConfig.recipe_bronze_yield), new FluidStack(liquid_copper,9), new FluidStack(liquid_tin,3));
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_brass,3 * FoundryConfig.recipe_brass_yield), new FluidStack(liquid_copper,9), new FluidStack(liquid_zinc,3));
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_invar,4 * FoundryConfig.recipe_invar_yield), new FluidStack(liquid_iron,8), new FluidStack(liquid_nickel,4));
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_electrum,6 * FoundryConfig.recipe_electrum_yield), new FluidStack(liquid_gold,6), new FluidStack(liquid_silver,6));

    ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
    ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CHESTPLATE);
    ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PICKAXE);
    ItemStack mold_block = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);
    ItemStack mold_axe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_AXE);
    ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SHOVEL);
    ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_HOE);
    ItemStack mold_sword = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SWORD);
    ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_LEGGINGS);
    ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_HELMET);
    ItemStack mold_boots = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BOOTS);
    ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
    ItemStack mold_cable_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CABLE_IC2);
    ItemStack mold_casing_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CASING_IC2);
    ItemStack mold_slab = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SLAB);
    ItemStack mold_stairs = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_STAIRS);
    ItemStack mold_plate_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PLATE_IC2);
    ItemStack extra_sticks1 = new ItemStack(Item.stick,1);
    ItemStack extra_sticks2 = new ItemStack(Item.stick,2);
    
    
    CastingRecipeManager.instance.AddMold(mold_ingot);
    CastingRecipeManager.instance.AddMold(mold_chestplate);
    CastingRecipeManager.instance.AddMold(mold_pickaxe);
    CastingRecipeManager.instance.AddMold(mold_block);
    CastingRecipeManager.instance.AddMold(mold_axe);
    CastingRecipeManager.instance.AddMold(mold_shovel);
    CastingRecipeManager.instance.AddMold(mold_hoe);
    CastingRecipeManager.instance.AddMold(mold_sword);
    CastingRecipeManager.instance.AddMold(mold_leggings);
    CastingRecipeManager.instance.AddMold(mold_helmet);
    CastingRecipeManager.instance.AddMold(mold_boots);
    CastingRecipeManager.instance.AddMold(mold_gear);
    CastingRecipeManager.instance.AddMold(mold_cable_ic2);
    CastingRecipeManager.instance.AddMold(mold_casing_ic2);
    CastingRecipeManager.instance.AddMold(mold_slab);
    CastingRecipeManager.instance.AddMold(mold_stairs);
    CastingRecipeManager.instance.AddMold(mold_plate_ic2);

    //Ingot casting recipes.
    for(Entry<String,ItemStack> entry:FoundryItems.ingot_stacks.entrySet())
    {
      CastingRecipeManager.instance.AddRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
              FoundryRecipes.FLUID_AMOUNT_INGOT),
          mold_ingot, null);
    }
    
    //Metal block casting recipes.
    for(Entry<String,ItemStack> entry:FoundryBlocks.block_stacks.entrySet())
    {
      CastingRecipeManager.instance.AddRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
              FoundryRecipes.FLUID_AMOUNT_BLOCK),
          mold_block, null);
    }
    
    //Metal slab casting recipes
    for(Entry<String,ItemStack> entry:FoundryBlocks.slab_stacks.entrySet())
    {
      ItemStack stack = entry.getValue();
      FluidStack fluid = new FluidStack(
          LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
          FoundryRecipes.FLUID_AMOUNT_BLOCK / 2);

      CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_slab, null);
      MeltingRecipeManager.instance.AddRecipe(stack, fluid);
    }

    //Metal stairs casting recipes
    for(i = 0; i < FoundryBlocks.block_metal_stairs.length; i++)
    {
      if(FoundryBlocks.block_metal_stairs[i] != null)
      {
        FoundryBlocks.MetalStair mr = FoundryBlocks.STAIRS_BLOCKS[i];
        ItemStack stack = new ItemStack(FoundryBlocks.block_metal_stairs[i]);
        FluidStack fluid = new FluidStack(
            LiquidMetalRegistry.instance.GetFluid(mr.metal),
            FoundryRecipes.FLUID_AMOUNT_BLOCK * 3 / 4);
        
        CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_stairs, null);
        MeltingRecipeManager.instance.AddRecipe(stack, fluid);
      }
    }

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.plateIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.plateGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.pickaxeIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.pickaxeGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.axeIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.axeGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.shovelIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.shovelGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.swordIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.swordGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.hoeIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.hoeGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.legsIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.legsGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.helmetIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.helmetGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.bootsIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.bootsGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

    CastingRecipeManager.instance.AddRecipe("gearIron", new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearGold", new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearCopper", new FluidStack(liquid_copper,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearTin", new FluidStack(liquid_tin,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearBronze", new FluidStack(liquid_bronze,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearBrass", new FluidStack(liquid_brass,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearInvar", new FluidStack(liquid_invar,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearSteel", new FluidStack(liquid_steel,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearElectrum", new FluidStack(liquid_electrum,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);

    
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",36, new ItemStack(Item.coal,1,0), 2400);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",6, new ItemStack(Item.coal,1,1), 4800);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",324, new ItemStack(Block.coalBlock,1), 19200);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",36, "dustCoal", 1600);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",6, "dustCharcoal", 3200);

    InfuserRecipeManager.instance.AddRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), "carbon", 2);
    
    NetworkRegistry.instance().registerGuiHandler(this, proxy);
  }
  
 
  @EventHandler
  public void load(FMLInitializationEvent event)
  {
    int i;
    log.setParent(FMLLog.getLogger());

    ModIntegration.RegisterIntegration(new ModIntegrationIC2("ic2"));
    ModIntegration.RegisterIntegration(new ModIntegrationBuildcraft("buildcraft"));
    ModIntegration.RegisterIntegration(new ModIntegrationForestry("forestry"));
    ModIntegration.RegisterIntegration(new ModIntegrationRailcraft("railcraft"));
    ModIntegration.RegisterIntegration(new ModIntegrationTE3("te3"));
    
    GameRegistry.registerTileEntity(TileEntityInductionCrucibleFurnace.class, "Foundry_MeltingFurnace");
    GameRegistry.registerTileEntity(TileEntityMetalCaster.class, "Foundry_MetalCaster");
    GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, "Foundry_AlloyMixer");
    GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, "Foundry_MetalInfuser");

    ItemStack brick_stack = new ItemStack(Item.brick);
    ItemStack iron_stack = new ItemStack(Item.ingotIron);
    ItemStack redstone_stack = new ItemStack(Item.redstone);
    ItemStack cobble_stack = new ItemStack(Block.cobblestone, 1, -1);
    ItemStack furnace_stack = new ItemStack(Block.furnaceIdle);
    ItemStack clay_stack = new ItemStack(Item.clay);
    ItemStack sand_stack = new ItemStack(Block.sand,1,-1);
    ItemStack clayblock_stack = new ItemStack(Block.blockClay, 1, -1);
    ItemStack brickblock_stack = new ItemStack(Block.brick, 1, -1);
    ItemStack crucible_stack = new ItemStack(FoundryBlocks.block_foundry_crucible);
    ItemStack piston_stack = new ItemStack(Block.pistonBase);
    ItemStack goldnugget_stack = new ItemStack(Item.goldNugget);
    ItemStack stick_stack = new ItemStack(Item.stick);
    ItemStack foundryclay_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundryclay8_stack = new ItemStack(FoundryItems.item_component,8,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundrybrick_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYBRICK);
    ItemStack blankmold_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD);
    ItemStack heatingcoil_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_HEATINGCOIL);
    ItemStack glasspane_stack = new ItemStack(Block.thinGlass);
    ItemStack emptycontainer_stack = FoundryContainerHandler.instance.FromFluidStack(null);
    
    GameRegistry.addRecipe(foundryclay8_stack,
        "CCC",
        "CSC",
        "CCC",
        'C', clay_stack,
        'S', sand_stack);

    GameRegistry.addShapelessRecipe(foundryclay8_stack,clayblock_stack, clayblock_stack, sand_stack);

    FurnaceRecipes.smelting().addSmelting(FoundryItems.item_component.itemID, ItemFoundryComponent.COMPONENT_FOUNDRYCLAY,
        foundrybrick_stack, 0.0f);

    GameRegistry.addRecipe(blankmold_stack,
        "CC",
        'C', foundryclay_stack);


    GameRegistry.addRecipe(new ShapedOreRecipe(heatingcoil_stack,
        "RCR",
        "NGN",
        "RCR",
        'C', "ingotCopper",
        'N', "ingotNickel",
        'G', goldnugget_stack,
        'R', redstone_stack));

    GameRegistry.addRecipe(crucible_stack, 
        "IBI",
        "B B",
        "IBI",
        'I', iron_stack,
        'B', foundrybrick_stack);
    
    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ICF),
        "IFI",
        "HCH",
        "HRH",
        'F', furnace_stack, 
        'I', "ingotCopper", 
        'C', crucible_stack,
        'R', redstone_stack,
        'H', heatingcoil_stack));
    
    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_CASTER),
        " R ",
        "ICI",
        "IPI",
        'I', iron_stack, 
        'P', piston_stack,
        'C', crucible_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_INFUSER),
        "IRI",
        "GCG",
        "HRH",
        'I', iron_stack, 
        'R', redstone_stack, 
        'B', foundrybrick_stack,
        'C', crucible_stack,
        'G', "gearStone",
        'H', heatingcoil_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER),
        "GIG",
        "GCG",
        "IRI",
        'I', iron_stack, 
        'C', crucible_stack,
        'R', redstone_stack,
        'G', "gearStone"));

    GameRegistry.addRecipe(new ShapedOreRecipe(emptycontainer_stack,
        " T ",
        "BGB",
        " T ",
        'T', "ingotTin", 
        'B', foundrybrick_stack,
        'G', glasspane_stack));

    //Mold crafting with vanilla items
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Block.planks,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Block.stone,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, new ItemStack(Item.brick));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateGold,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateDiamond,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetGold,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetDiamond,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordDiamond));
    for(i = 0; i < Block.blocksList.length; i++)
    {
      Block block = Block.blocksList[i];
      if(block instanceof BlockStairs)
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_STAIRS_CLAY, new ItemStack(block,1,-1));
      } else if(block instanceof BlockHalfSlab && !block.isOpaqueCube())
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SLAB_CLAY, new ItemStack(block,1,-1));
      }
    }
    
    //Gear Mold crafting recipes.
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearWood");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearStone");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearIron");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearGold");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearDiamond");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearCopper");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearTin");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearBronze");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearBrass");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearInvar");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearSteel");

    //Ingot and block mold crafting recipes
    for(String name:OreDictionary.getOreNames())
    {
      if(name.startsWith("ingot"))
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, name);
      } else if(name.startsWith("block"))
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, name);
      }
    }

    //Ore -> ingot furnace recipes
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_COPPER,ItemIngot.INGOT_COPPER);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_TIN,ItemIngot.INGOT_TIN);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_ZINC,ItemIngot.INGOT_ZINC);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_NICKEL,ItemIngot.INGOT_NICKEL);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_SILVER,ItemIngot.INGOT_SILVER);
    
    //Clay mold furnace recipes
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BLOCK_CLAY,ItemMold.MOLD_BLOCK);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CHESTPLATE_CLAY,ItemMold.MOLD_CHESTPLATE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_INGOT_CLAY,ItemMold.MOLD_INGOT);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PICKAXE_CLAY,ItemMold.MOLD_PICKAXE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_AXE_CLAY,ItemMold.MOLD_AXE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SHOVEL_CLAY,ItemMold.MOLD_SHOVEL);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SWORD_CLAY,ItemMold.MOLD_SWORD);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HOE_CLAY,ItemMold.MOLD_HOE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_LEGGINGS_CLAY,ItemMold.MOLD_LEGGINGS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HELMET_CLAY,ItemMold.MOLD_HELMET);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BOOTS_CLAY,ItemMold.MOLD_BOOTS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_GEAR_CLAY,ItemMold.MOLD_GEAR);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CABLE_IC2_CLAY,ItemMold.MOLD_CABLE_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CASING_IC2_CLAY,ItemMold.MOLD_CASING_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SLAB_CLAY,ItemMold.MOLD_SLAB);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_STAIRS_CLAY,ItemMold.MOLD_STAIRS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PLATE_IC2_CLAY,ItemMold.MOLD_PLATE_IC2);

    GameRegistry.registerCraftingHandler(new MoldCraftingHandler());

    Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
    Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
    Fluid liquid_zinc = LiquidMetalRegistry.instance.GetFluid("Zinc");
    Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
    Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
    Fluid liquid_nickel = LiquidMetalRegistry.instance.GetFluid("Nickel");
    Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
    Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
    Fluid liquid_invar = LiquidMetalRegistry.instance.GetFluid("Invar");
    Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
    Fluid liquid_brass = LiquidMetalRegistry.instance.GetFluid("Brass");



    int ore_id = FoundryBlocks.block_ore.blockID;
    if(FoundryConfig.wordgen_copper)
    {
      WordGenOre.RegisterOre(16, 80, 12, ore_id, BlockFoundryOre.ORE_COPPER);
    }
    if(FoundryConfig.wordgen_tin)
    {
      WordGenOre.RegisterOre(16, 52, 8, ore_id, BlockFoundryOre.ORE_TIN);
    }
    if(FoundryConfig.wordgen_zinc)
    {
      WordGenOre.RegisterOre(8, 48, 6, ore_id, BlockFoundryOre.ORE_ZINC);
    }
    if(FoundryConfig.wordgen_nickel)
    {
      WordGenOre.RegisterOre(8, 36, 5, ore_id, BlockFoundryOre.ORE_NICKEL);
    }
    if(FoundryConfig.wordgen_silver)
    {
      WordGenOre.RegisterOre(2, 30, 3, ore_id, BlockFoundryOre.ORE_SILVER);
    }
    if(FoundryConfig.wordgen_lead)
    {
      WordGenOre.RegisterOre(8, 48, 5, ore_id, BlockFoundryOre.ORE_LEAD);
    }
    GameRegistry.registerWorldGenerator(new FoundryWorldGenerator());

    proxy.Init();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    //Check for the existence of a stone gear, add it's own if it doesn't.
    if(OreDictionary.getOres("gearStone").size() == 0)
    {
      ItemStack cobble_stack = new ItemStack(Block.cobblestone, 1, -1);
      ItemStack stick_stack = new ItemStack(Item.stick);

      ItemStack gear_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_GEAR);
      OreDictionary.registerOre("gearStone", gear_stack);
      GameRegistry.addRecipe((ItemStack)gear_stack,
          " C ",
          "CSC",
          " C ",
          'C', cobble_stack,
          'S', stick_stack);
    }
  }
}
