package exter.foundry;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.integration.ModIntegration;
import exter.foundry.integration.ModIntegrationBuildcraft;
import exter.foundry.integration.ModIntegrationGregtech;
import exter.foundry.integration.ModIntegrationIC2;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.network.FoundryNetworkChannel;
import exter.foundry.proxy.CommonFoundryProxy;
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
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.worldgen.FoundryWorldGenerator;
import exter.foundry.worldgen.WordGenOre;

@Mod(
  modid = ModFoundry.MODID,
  name = ModFoundry.MODNAME,
  version = ModFoundry.MODVERSION,
  dependencies = "required-after:Forge@[10.12.0.1046,);"
      + "after:TConstruct;"
      + "after:BuildCraft|Core;"
      + "after:Railcraft;"
      + "after:ThermalExpansion;"
      + "after:Redstone Arsenal;"
      + "after:IC2;"
      + "after:Forestry;"
      + "after:gregtech_addon"
)
public class ModFoundry
{
  public static final String MODID = "foundry";
  public static final String MODNAME = "Foundry";
  public static final String MODVERSION = "1.0.0.0-pre";

  @Instance(MODID)
  public static ModFoundry instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(
    clientSide = "exter.foundry.proxy.ClientFoundryProxy",
    serverSide = "exter.foundry.proxy.CommonFoundryProxy"
  )
  public static CommonFoundryProxy proxy;

  
  public static Logger log = Logger.getLogger(MODNAME);

  public CraftingEvents crafting_events;
  
  public static FoundryNetworkChannel network_channel;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    int i;
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    ModIntegration.RegisterIntegration(config,new ModIntegrationIC2("ic2"));
    ModIntegration.RegisterIntegration(config,new ModIntegrationBuildcraft("buildcraft"));
    //ModIntegration.RegisterIntegration(config,new ModIntegrationForestry("forestry"));
    //ModIntegration.RegisterIntegration(config,new ModIntegrationRailcraft("railcraft"));
    //ModIntegration.RegisterIntegration(config,new ModIntegrationTE3("te3"));
    //ModIntegration.RegisterIntegration(config,new ModIntegrationRedstoneArsenal("redarsenal"));
    //ModIntegration.RegisterIntegration(config,new ModIntegrationTiCon("ticon"));
    //ModIntegration.RegisterIntegration(config,new ModIntegrationGregtech("gregtech"));
    

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


    FoundryConfig.Load(config);
    FoundryItems.RegisterItems(config);
    FoundryBlocks.RegisterBlocks(config);
    Fluid liquid_iron = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Iron", 1850, 15);
    Fluid liquid_gold = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Gold", 1350, 15);
    Fluid liquid_copper = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Copper", 1400, 15);
    Fluid liquid_tin = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Tin", 550, 7);
    Fluid liquid_bronze = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Bronze", 1400, 15);
    Fluid liquid_electrum = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Electrum", 1350, 15);
    Fluid liquid_invar = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Invar", 1850, 15);
    Fluid liquid_nickel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Nickel", 1750, 15);
    Fluid liquid_zinc = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Zinc", 700, 15);
    Fluid liquid_brass = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Brass", 1400, 15);
    Fluid liquid_silver = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Silver", 1250, 15);
    Fluid liquid_steel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Steel", 1850, 15);
    Fluid liquid_cupronickel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Cupronickel", 1750, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Lead", 650, 1);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Aluminum", 1100, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Chromium", 2200, 8);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Platinum", 2050, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Manganese", 1550, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Titanium", 2000, 15);
    
    ModIntegration.PreInit(config);

    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      FoundryUtils.RegisterBasicMeltingRecipes(name,LiquidMetalRegistry.instance.GetFluid(name));
    }
    FoundryUtils.RegisterBasicMeltingRecipes("Chrome",LiquidMetalRegistry.instance.GetFluid("Chromium"));
    FoundryUtils.RegisterBasicMeltingRecipes("Aluminium",LiquidMetalRegistry.instance.GetFluid("Aluminum"));


    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 3, ItemIngot.INGOT_BRONZE),
        new OreStack("ingotCopper", 3),
        new OreStack("ingotTin", 1)
        );
    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 3, ItemIngot.INGOT_BRASS),
        new OreStack("ingotCopper", 3),
        new OreStack("ingotZinc", 1)
        );
    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 2, ItemIngot.INGOT_INVAR),
        new OreStack("ingotIron", 2),
        new OreStack("ingotNickel", 1)
        );

    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 3, ItemIngot.INGOT_ELECTRUM),
        new OreStack("ingotGold", 2),
        new OreStack("ingotSilver", 2)
        );
    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 3, ItemIngot.INGOT_CUPRONICKEL),
        new OreStack("ingotCopper", 2),
        new OreStack("ingotNickel", 2)
        );
    

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_bronze, 4),
        new FluidStack[] {
          new FluidStack(liquid_copper, 3),
          new FluidStack(liquid_tin, 1)
          });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_brass, 4),
        new FluidStack[] {
          new FluidStack(liquid_copper, 3),
          new FluidStack(liquid_zinc, 1)
        });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_invar, 3),
        new FluidStack[] {
          new FluidStack(liquid_iron, 2),
          new FluidStack(liquid_nickel, 1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_electrum, 2),
        new FluidStack[] {
          new FluidStack(liquid_gold, 1),
          new FluidStack(liquid_silver, 1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_cupronickel, 2),
        new FluidStack[] {
          new FluidStack(liquid_copper, 1),
          new FluidStack(liquid_nickel, 1)
        });

    ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
    ItemStack mold_cable_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CABLE_IC2);
    ItemStack mold_casing_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CASING_IC2);
    ItemStack mold_slab = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SLAB);
    ItemStack mold_stairs = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_STAIRS);
    ItemStack mold_plate_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PLATE_IC2);
    ItemStack mold_block = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);
    ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
    ItemStack extra_sticks1 = new ItemStack(Items.stick,1);
    ItemStack extra_sticks2 = new ItemStack(Items.stick,2);
    
    CastingRecipeManager.instance.AddMold(mold_ingot);
    CastingRecipeManager.instance.AddMold(mold_cable_ic2);
    CastingRecipeManager.instance.AddMold(mold_casing_ic2);
    CastingRecipeManager.instance.AddMold(mold_slab);
    CastingRecipeManager.instance.AddMold(mold_stairs);
    CastingRecipeManager.instance.AddMold(mold_plate_ic2);
    CastingRecipeManager.instance.AddMold(mold_block);
    CastingRecipeManager.instance.AddMold(mold_gear);

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);

      CastingRecipeManager.instance.AddMold(mold_chestplate);
      CastingRecipeManager.instance.AddMold(mold_pickaxe);
      CastingRecipeManager.instance.AddMold(mold_axe);
      CastingRecipeManager.instance.AddMold(mold_shovel);
      CastingRecipeManager.instance.AddMold(mold_hoe);
      CastingRecipeManager.instance.AddMold(mold_sword);
      CastingRecipeManager.instance.AddMold(mold_leggings);
      CastingRecipeManager.instance.AddMold(mold_helmet);
      CastingRecipeManager.instance.AddMold(mold_boots);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_chestplate, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_chestplate, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_pickaxe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_pickaxe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_axe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_axe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_shovel, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_shovel, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_sword, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_sword, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_hoe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_hoe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_leggings, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_leggings, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_helmet, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_helmet, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_boots, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_boots, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

    }
    
    //Ingot casting recipes.
    for(Entry<String,ItemStack> entry:FoundryItems.ingot_stacks.entrySet())
    {
      CastingRecipeManager.instance.AddRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
              FoundryAPI.FLUID_AMOUNT_INGOT),
          mold_ingot, null);
    }
    
    
    //Metal block casting recipes.
    for(Entry<String,ItemStack> entry:FoundryBlocks.block_stacks.entrySet())
    {
      Fluid fluid = LiquidMetalRegistry.instance.GetFluid(entry.getKey());
      if(fluid != null)
      {
        CastingRecipeManager.instance.AddRecipe(
            entry.getValue(),
            new FluidStack(
                fluid,
                FoundryAPI.FLUID_AMOUNT_BLOCK),
            mold_block, null);
      }
    }
    
    //Metal slab casting recipes
    for(Entry<String,ItemStack> entry:FoundryBlocks.slab_stacks.entrySet())
    {
      ItemStack stack = entry.getValue();
      FluidStack fluid = new FluidStack(
          LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
          FoundryAPI.FLUID_AMOUNT_BLOCK / 2);

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
            FoundryAPI.FLUID_AMOUNT_BLOCK * 3 / 4);
        
        CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_stairs, null);
        MeltingRecipeManager.instance.AddRecipe(stack, fluid);
      }
    }
    
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",36), new ItemStack(Items.coal,1,0), 240000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",12), new ItemStack(Items.coal,1,1), 480000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",324), new ItemStack(Blocks.coal_block,1), 1920000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",36), "dustCoal", 160000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",12), "dustCharcoal", 320000);

    InfuserRecipeManager.instance.AddRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), new InfuserSubstance("carbon", 2));

    
    if(FoundryConfig.recipe_gear_useoredict)
    {
      for(String name:LiquidMetalRegistry.instance.GetFluidNames())
      {
        Fluid fluid = LiquidMetalRegistry.instance.GetFluid(name);
        MeltingRecipeManager.instance.AddRecipe("gear" + name, new FluidStack(fluid,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.AddRecipe("gear" + name, new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
           
    if(FoundryConfig.recipe_glass)
    {
      final String[] oredict_names = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

      int temp = 1550;
      Fluid liquid_glass = LiquidMetalRegistry.instance.RegisterLiquidMetal("Glass", temp, 12);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.sand), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glass), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glass_pane), new FluidStack(liquid_glass,375),temp,250);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glass), new FluidStack(liquid_glass,1000),mold_block,null,400);
      
      for(i = 0; i < ItemDye.field_150921_b/*icon_names*/.length; i++)
      {
        String name = ItemDye.field_150921_b/*icon_names*/[i];
        int color = ItemDye.field_150922_c/*colors*/[i];
        int c1 = 63 + (color & 0xFF) * 3 / 4;
        int c2 = 63 + ((color >> 8 ) & 0xFF) * 3 / 4;
        int c3 = 63 + ((color >> 16) & 0xFF) * 3 / 4;
        int fluid_color = c1 | (c2 << 8) | (c3 << 16);
        
        Fluid liquid_glass_colored = LiquidMetalRegistry.instance.RegisterLiquidMetal("Glass." + name, temp, 12,"liquidGlass",fluid_color);

        int meta = ~i & 15;
        MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.stained_glass,1,meta), new FluidStack(liquid_glass_colored,1000),temp,250);
        MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.stained_glass_pane,1,i), new FluidStack(liquid_glass_colored,375),temp,250);
        CastingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.stained_glass,1,meta), new FluidStack(liquid_glass_colored,1000),mold_block,null,400);
        
        InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("dye." + name,200), oredict_names[i], 25000);
        InfuserRecipeManager.instance.AddRecipe(new FluidStack(liquid_glass_colored,40),new FluidStack(liquid_glass,40),new InfuserSubstance("dye." + name,1));
      }
    }

    config.save();

    crafting_events = new CraftingEvents();
    
    network_channel = new FoundryNetworkChannel();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    proxy.PreInit();
  }
  
 
  @EventHandler
  public void load(FMLInitializationEvent event)
  {
    //log.setParent(FMLLog.getLogger());
    ModIntegration.Init();
    
    GameRegistry.registerTileEntity(TileEntityInductionCrucibleFurnace.class, "Foundry_ICF");
    GameRegistry.registerTileEntity(TileEntityMetalCaster.class, "Foundry_MetalCaster");
    GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, "Foundry_AlloyMixer");
    GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, "Foundry_MetalInfuser");
    GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, "Foundry_AlloyFurnace");
    GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, "Foundry_MaterialRouter");

    ItemStack iron_stack = new ItemStack(Items.iron_ingot);
    ItemStack redstone_stack = new ItemStack(Items.redstone);
    ItemStack furnace_stack = new ItemStack(Blocks.furnace);
    ItemStack clay_stack = new ItemStack(Items.clay_ball);
    ItemStack sand_stack = new ItemStack(Blocks.sand,1,-1);
    ItemStack clayblock_stack = new ItemStack(Blocks.clay, 1, -1);
    ItemStack crucible_stack = new ItemStack(FoundryBlocks.block_refractory_casing);
    ItemStack piston_stack = new ItemStack(Blocks.piston);
    ItemStack goldnugget_stack = new ItemStack(Items.gold_nugget);
    ItemStack chest_stack = new ItemStack(Blocks.chest);
    ItemStack foundryclay_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundryclay8_stack = new ItemStack(FoundryItems.item_component,8,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundrybrick_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYBRICK);
    ItemStack blankmold_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD);
    ItemStack heatingcoil_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_HEATINGCOIL);
    ItemStack glasspane_stack = new ItemStack(Blocks.glass_pane);
    ItemStack emptycontainer2_stack = FoundryItems.item_container.EmptyContainer(2);
    
    GameRegistry.addRecipe(foundryclay8_stack,
        "CCC",
        "CSC",
        "CCC",
        'C', clay_stack,
        'S', sand_stack);

    GameRegistry.addShapelessRecipe(foundryclay8_stack,clayblock_stack, clayblock_stack, sand_stack);

    FurnaceRecipes.smelting().func_151394_a/*addSmelting*/(
        new ItemStack(FoundryItems.item_component, 1, ItemFoundryComponent.COMPONENT_FOUNDRYCLAY),
        foundrybrick_stack, 0.0f);

    GameRegistry.addRecipe(blankmold_stack,
        "CC",
        'C', foundryclay_stack);


    ModIntegrationGregtech gti = (ModIntegrationGregtech)ModIntegration.GetIntegration("gregtech");
    if(gti == null || !gti.IsLoaded() || !gti.change_recipes)
    {
      ItemStack heatingcoil2_stack = new ItemStack(FoundryItems.item_component,2,ItemFoundryComponent.COMPONENT_HEATINGCOIL);

      GameRegistry.addRecipe(new ShapedOreRecipe(
          emptycontainer2_stack,
          " T ",
          "BGB",
          " T ",
          'T', "ingotTin",
          'B', foundrybrick_stack,
          'G', glasspane_stack));

      GameRegistry.addRecipe(new ShapedOreRecipe(
          heatingcoil2_stack,
          "RCR",
          "CGC",
          "RCR",
          'C', "ingotCupronickel",
          'G', goldnugget_stack,
          'R', redstone_stack));

      GameRegistry.addRecipe(
          crucible_stack,
          "IBI",
          "B B",
          "IBI",
          'I', iron_stack, 'B',
          foundrybrick_stack);

      GameRegistry.addRecipe(new ShapedOreRecipe(
          new ItemStack(FoundryBlocks.block_machine, 1, BlockFoundryMachine.MACHINE_ICF),
          "IFI",
          "HCH",
          "HRH",
          'F', furnace_stack,
          'I', "ingotCopper",
          'C', crucible_stack,
          'R', redstone_stack,
          'H', heatingcoil_stack));

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
    }
    
    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_alloy_furnace),
        "BBB",
        "BFB",
        "BBB",
        'B', foundrybrick_stack, 
        'F', furnace_stack);

    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_CASTER),
        " H ",
        "RCR",
        "IPI",
        'H', chest_stack, 
        'I', iron_stack, 
        'P', piston_stack,
        'C', crucible_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER),
        "GIG",
        "GCG",
        "IRI",
        'I', iron_stack, 
        'C', crucible_stack,
        'R', redstone_stack,
        'G', "gearStone"));

    //Mold crafting with vanilla items
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Blocks.planks,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Blocks.stone,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, new ItemStack(Items.brick));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, new ItemStack(Items.netherbrick));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, foundrybrick_stack);
    if(FoundryConfig.recipe_tools_armor)
    {
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Items.iron_chestplate,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Items.golden_chestplate,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Items.diamond_chestplate,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Items.iron_leggings,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Items.golden_leggings,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Items.diamond_leggings,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Items.iron_helmet,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Items.golden_helmet,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Items.diamond_helmet,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Items.iron_boots,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Items.golden_boots,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Items.diamond_boots,1,-1));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Items.wooden_pickaxe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Items.stone_pickaxe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Items.iron_pickaxe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Items.golden_pickaxe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Items.diamond_pickaxe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Items.wooden_axe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Items.stone_axe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Items.iron_axe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Items.golden_axe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Items.diamond_axe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Items.wooden_shovel));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Items.stone_shovel));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Items.iron_shovel));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Items.golden_shovel));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Items.diamond_shovel));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Items.wooden_hoe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Items.stone_hoe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Items.iron_hoe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Items.golden_hoe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Items.diamond_hoe));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Items.wooden_sword));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Items.stone_sword));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Items.iron_sword));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Items.golden_sword));
	    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Items.diamond_sword));
    }

    for(Field f:Blocks.class.getFields())
    {
      Object obj;
      try
      {
        obj = f.get(Blocks.class);
      } catch(IllegalArgumentException e)
      {
        continue;
      } catch(IllegalAccessException e)
      {
        continue;
      }
      if(obj instanceof Block)
      {
        Block block = (Block) obj;
        if(obj instanceof BlockStairs)
        {

          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_STAIRS_CLAY, new ItemStack(block, 1, -1));
        } else if(block instanceof BlockSlab && !block.isOpaqueCube())
        {
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SLAB_CLAY, new ItemStack(block, 1, -1));
        }
      }
    }
    
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gear" + name);
    }
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearWood");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearDiamond");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearStone");

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
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_INGOT_CLAY,ItemMold.MOLD_INGOT);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CABLE_IC2_CLAY,ItemMold.MOLD_CABLE_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CASING_IC2_CLAY,ItemMold.MOLD_CASING_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SLAB_CLAY,ItemMold.MOLD_SLAB);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_STAIRS_CLAY,ItemMold.MOLD_STAIRS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PLATE_IC2_CLAY,ItemMold.MOLD_PLATE_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_GEAR_CLAY,ItemMold.MOLD_GEAR);

    if(FoundryConfig.recipe_tools_armor)
    {
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CHESTPLATE_CLAY,ItemMold.MOLD_CHESTPLATE);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PICKAXE_CLAY,ItemMold.MOLD_PICKAXE);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_AXE_CLAY,ItemMold.MOLD_AXE);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SHOVEL_CLAY,ItemMold.MOLD_SHOVEL);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SWORD_CLAY,ItemMold.MOLD_SWORD);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HOE_CLAY,ItemMold.MOLD_HOE);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_LEGGINGS_CLAY,ItemMold.MOLD_LEGGINGS);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HELMET_CLAY,ItemMold.MOLD_HELMET);
	    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BOOTS_CLAY,ItemMold.MOLD_BOOTS);
    }
    

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

    proxy.Init();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    ModIntegration.PostInit();
    //Check for the existence of a stone gear, add it's own if it doesn't exist.
    if(OreDictionary.getOres("gearStone").size() == 0)
    {
      ItemStack cobble_stack = new ItemStack(Blocks.cobblestone, 1, -1);
      ItemStack stick_stack = new ItemStack(Items.stick);

      ItemStack gear_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_GEAR);
      OreDictionary.registerOre("gearStone", gear_stack);
      GameRegistry.addRecipe((ItemStack)gear_stack,
          " C ",
          "CSC",
          " C ",
          'C', cobble_stack,
          'S', stick_stack);
    }
    
    for(OreDictType type:OreDictType.TYPES)
    {
      for(OreDictMaterial material:OreDictMaterial.MATERIALS)
      {
        String od_name = type.prefix + material.suffix;
        for(ItemStack item:OreDictionary.getOres(od_name))
        {
          ModFoundry.log.info("Registering MR Item: " + item.getUnlocalizedName());
          ModFoundry.log.info("  OD Name: " + od_name);
          ModFoundry.log.info("  Material: " + material.suffix);
          ModFoundry.log.info("  Type: " + type.name);
          MaterialRegistry.instance.RegisterItem(item, material.suffix, type.name);
        }
      }
    }
    proxy.PostInit();
  }
}
