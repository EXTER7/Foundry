package exter.foundry.integration.nei;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IAlloyRecipe;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.recipes.manager.AlloyRecipeManager;


public class AlloyRecipeHandler extends FoundryRecipeHandler {

	public class CachedAlloyRecipe extends CachedFoundryRecipe {
		public List<FluidTank> allTanks = Lists.newLinkedList();
		public FluidTank output;
		public CachedAlloyRecipe(IAlloyRecipe recipe) {
			int maxSize = 0;
			for (int idx = 0; idx < recipe.GetInputCount(); idx++)
				if (recipe.GetInput(idx) != null) {
					allTanks.add(new FluidTank(recipe.GetInput(idx), 2000, new Rectangle(21 + idx * 21, 34, 16, 31)));
					maxSize = Math.max(maxSize, recipe.GetInput(idx).amount);
				}
			output = new FluidTank(recipe.GetOutput(), 2000, new Rectangle(128, 34, 16, 31));
			maxSize = Math.max(maxSize, recipe.GetOutput().amount);
			allTanks.add(output);
			for (FluidTank tank : allTanks) tank.capacity = maxSize;
		}
		
		@Override
		public List<FluidTank> getTanks() {
			return allTanks;
		}
	}
	
	@Override
	public String getRecipeName() {
		return "Alloy Mixer";
	}

	@Override
	public String getGuiTexture() {
		return "foundry:textures/gui/alloymixer.png";
	}

	@Override
	public void drawExtras(int recipe) {
		CachedAlloyRecipe foundryRecipe = (CachedAlloyRecipe)arecipes.get(recipe);
		drawTanks(foundryRecipe.getTanks(), 0, TANK_OVERLAY);
	}
	
	public void loadAllRecipes() {
		for (IAlloyRecipe recipe : AlloyRecipeManager.instance.GetRecipes())
			arecipes.add(new CachedAlloyRecipe(recipe));
	}
	
	@Override
	public void loadUsageRecipes(String outputId, Object... results) {
		if(outputId.equals("foundry.alloy")) 
			loadAllRecipes();
		if(outputId.equals("liquid") || outputId.equals("item")) {
			FluidStack fluid = getFluidStackFor(results[0]);
			if (fluid == null) return;
			for (IAlloyRecipe recipe : AlloyRecipeManager.instance.GetRecipes()) {
				for (int idx = 0; idx < recipe.GetInputCount(); idx++)
					if (recipe.GetInput(idx).isFluidEqual(fluid))
						arecipes.add(new CachedAlloyRecipe(recipe));
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("foundry.alloy")) 
			loadAllRecipes();
		if(outputId.equals("liquid") || outputId.equals("item")) {
			FluidStack fluid = getFluidStackFor(results[0]);
			if (fluid == null) return;
			for (IAlloyRecipe recipe : AlloyRecipeManager.instance.GetRecipes())
				if (recipe.GetOutput().isFluidEqual(fluid))
					arecipes.add(new CachedAlloyRecipe(recipe));
		}
	}
	
	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(105, 42, 22, 15), "foundry.alloy", new Object[0]));
	}

	@Override
	public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis() {
		return ImmutableList.<Class<? extends GuiContainer>>of(GuiAlloyMixer.class);
	}
}
