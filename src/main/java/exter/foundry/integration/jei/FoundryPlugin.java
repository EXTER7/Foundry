package exter.foundry.integration.jei;

import exter.foundry.container.ContainerAlloyFurnace;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class FoundryPlugin implements IModPlugin {

  @Override
  public boolean isModLoaded() {
    return true;
  }

  @Override
  public void register(IModRegistry registry) {
    registry.addRecipeCategories(new AlloyFurnaceJEI.Category());

    registry.addRecipeHandlers(
        new AlloyFurnaceJEI.Handler()
    );

    registry.addBasicRecipeTransferHelper(ContainerAlloyFurnace.class, "foundry.alloyfurnace",
        ContainerAlloyFurnace.SLOTS_TE,
        ContainerAlloyFurnace.SLOTS_TE_SIZE, 
        ContainerAlloyFurnace.SLOTS_INVENTORY, 36);
    registry.addRecipes(AlloyFurnaceJEI.getRecipes());
  }
}
