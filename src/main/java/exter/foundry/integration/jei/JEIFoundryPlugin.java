package exter.foundry.integration.jei;

import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.container.ContainerAlloyFurnace;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.container.ContainerMoldStation;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIFoundryPlugin implements IModPlugin
{
  
  @Override
  public void register(IModRegistry registry)
  {
    IJeiHelpers helpers = registry.getJeiHelpers();
    
    
    CastingTableJEI table_ingot = new CastingTableJEI(BlockCastingTable.EnumTable.INGOT);
    CastingTableJEI table_plate = new CastingTableJEI(BlockCastingTable.EnumTable.PLATE);
    CastingTableJEI table_rod = new CastingTableJEI(BlockCastingTable.EnumTable.ROD);
    CastingTableJEI table_block = new CastingTableJEI(BlockCastingTable.EnumTable.BLOCK);
    
    registry.addRecipeCategories(
        new AlloyFurnaceJEI.Category(helpers),
        new MeltingJEI.Category(helpers),
        new CastingJEI.Category(helpers),
        new AlloyMixerJEI.Category(helpers),
        new AlloyingCrucibleJEI.Category(helpers),
        new InfuserJEI.Category(helpers),
        new AtomizerJEI.Category(helpers),
        new MoldStationJEI.Category(helpers),
        table_ingot.new Category(helpers),
        table_plate.new Category(helpers),
        table_rod.new Category(helpers),
        table_block.new Category(helpers)
    );

    registry.addRecipeHandlers(
        new AlloyFurnaceJEI.Handler(),
        new MeltingJEI.Handler(),
        new CastingJEI.Handler(),
        new AlloyMixerJEI.Handler(),
        new AlloyingCrucibleJEI.Handler(),
        new InfuserJEI.Handler(),
        new AtomizerJEI.Handler(),
        new MoldStationJEI.Handler(),
        new CastingTableJEI.Handler()
    );
    IRecipeTransferRegistry transfer_registry = registry.getRecipeTransferRegistry();

    transfer_registry.addRecipeTransferHandler(ContainerAlloyFurnace.class, "foundry.alloyfurnace",
        ContainerAlloyFurnace.SLOTS_TE,
        ContainerAlloyFurnace.SLOTS_TE_SIZE, 
        ContainerAlloyFurnace.SLOTS_INVENTORY, 36);
    transfer_registry.addRecipeTransferHandler(ContainerMeltingCrucible.class, "foundry.melting",
        ContainerMeltingCrucible.SLOTS_TE,
        ContainerMeltingCrucible.SLOTS_TE_SIZE, 
        ContainerMeltingCrucible.SLOTS_INVENTORY, 36);
    transfer_registry.addRecipeTransferHandler(ContainerMetalCaster.class, "foundry.casting",
        ContainerMetalCaster.SLOTS_TE,
        ContainerMetalCaster.SLOTS_TE_SIZE, 
        ContainerMetalCaster.SLOTS_INVENTORY, 36);
    transfer_registry.addRecipeTransferHandler(ContainerMetalInfuser.class, "foundry.infuser",
        ContainerMetalInfuser.SLOTS_TE,
        ContainerMetalInfuser.SLOTS_TE_SIZE, 
        ContainerMetalInfuser.SLOTS_INVENTORY, 36);
    transfer_registry.addRecipeTransferHandler(ContainerMoldStation.class, "foundry.mold",
        ContainerMoldStation.SLOTS_TE,
        ContainerMoldStation.SLOTS_TE_SIZE, 
        ContainerMoldStation.SLOTS_INVENTORY, 36);

    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_BASIC), "foundry.melting");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_STANDARD), "foundry.melting");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_ADVANCED), "foundry.melting");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CASTER), "foundry.casting");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ALLOYMIXER), "foundry.alloymixer");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.INFUSER), "foundry.infuser");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ATOMIZER), "foundry.atomizer");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ALLOYING_CRUCIBLE), "foundry.alloyingcrucible");
    registry.addRecipeCategoryCraftingItem(new ItemStack(FoundryBlocks.block_mold_station), "foundry.mold");
    registry.addRecipeCategoryCraftingItem(new ItemStack(FoundryBlocks.block_alloy_furnace), "foundry.alloyfurnace");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.INGOT), "foundry.casting_table.ingot");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.PLATE), "foundry.casting_table.plate");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.ROD), "foundry.casting_table.rod");
    registry.addRecipeCategoryCraftingItem(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.BLOCK), "foundry.casting_table.block");

    registry.addRecipes(AlloyFurnaceJEI.getRecipes(helpers));
    registry.addRecipes(MeltingJEI.getRecipes(helpers));
    registry.addRecipes(CastingJEI.getRecipes());
    registry.addRecipes(AlloyMixerJEI.getRecipes());
    registry.addRecipes(AlloyingCrucibleJEI.getRecipes());
    registry.addRecipes(InfuserJEI.getRecipes());
    registry.addRecipes(AtomizerJEI.getRecipes());
    registry.addRecipes(MoldStationJEI.getRecipes(helpers));
    registry.addRecipes(table_ingot.getRecipes());
    registry.addRecipes(table_plate.getRecipes());
    registry.addRecipes(table_rod.getRecipes());
    registry.addRecipes(table_block.getRecipes());
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
  {
    
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registry)
  {
    // TODO Auto-generated method stub
    
  }
}
