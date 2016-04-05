package exter.foundry.integration.jei;

import exter.foundry.container.ContainerAlloyFurnace;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalAtomizer;
import exter.foundry.container.ContainerMetalCaster;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@JEIPlugin
public class JEIFoundryPlugin implements IModPlugin
{
  
  @Override
  public void register(IModRegistry registry)
  {
    IJeiHelpers helpers = registry.getJeiHelpers();
    registry.addRecipeCategories(
        new AlloyFurnaceJEI.Category(helpers),
        new MeltingJEI.Category(helpers),
        new CastingJEI.Category(helpers),
        new AlloyMixerJEI.Category(helpers),
        new AtomizerJEI.Category(helpers)
    );

    registry.addRecipeHandlers(
        new AlloyFurnaceJEI.Handler(),
        new MeltingJEI.Handler(),
        new CastingJEI.Handler(),
        new AlloyMixerJEI.Handler(),
        new AtomizerJEI.Handler()
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
    transfer_registry.addRecipeTransferHandler(ContainerAlloyMixer.class, "foundry.alloymixer",
        ContainerAlloyMixer.SLOTS_TE,
        ContainerAlloyMixer.SLOTS_TE_SIZE, 
        ContainerAlloyMixer.SLOTS_INVENTORY, 36);
    /*
    transfer_registry.addRecipeTransferHandler(ContainerMetalInfuser.class, "foundry.infuser",
        ContainerMetalInfuser.SLOTS_TE,
        ContainerMetalInfuser.SLOTS_TE_SIZE, 
        ContainerMetalInfuser.SLOTS_INVENTORY, 36);
    transfer_registry.addRecipeTransferHandler(ContainerMetalInfuser.class, "foundry.infuser.substance",
        ContainerMetalInfuser.SLOTS_TE,
        ContainerMetalInfuser.SLOTS_TE_SIZE, 
        ContainerMetalInfuser.SLOTS_INVENTORY, 36);
        */
    transfer_registry.addRecipeTransferHandler(ContainerMetalAtomizer.class, "foundry.atomizer",
        ContainerMetalAtomizer.SLOTS_TE,
        ContainerMetalAtomizer.SLOTS_TE_SIZE, 
        ContainerMetalAtomizer.SLOTS_INVENTORY, 36);

    registry.addRecipes(AlloyFurnaceJEI.getRecipes(helpers));
    registry.addRecipes(MeltingJEI.getRecipes(helpers));
    registry.addRecipes(CastingJEI.getRecipes());
    registry.addRecipes(AlloyMixerJEI.getRecipes());
    registry.addRecipes(AtomizerJEI.getRecipes());
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
  {
    
  }
}
