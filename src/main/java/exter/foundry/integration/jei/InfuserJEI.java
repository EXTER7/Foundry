package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.gui.GuiMetalInfuser;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.tileentity.TileEntityFoundryPowered;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;


public class InfuserJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    private final IInfuserRecipe recipe;

    public Wrapper(IInfuserRecipe recipe)
    {
      this.recipe = recipe;
    }

    @Deprecated
    @Override
    public List<List<ItemStack>> getInputs()
    {
      return null;
    }

    @Deprecated
    @Override
    public List<ItemStack> getOutputs()
    {
      return null;
    }

    @Deprecated
    @Override
    public List<FluidStack> getFluidInputs()
    {
      return null;
    }

    @Deprecated
    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return null;
    }


    @Deprecated
    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public List<String> getTooltipStrings(int x, int y)
    {
      return null;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
      minecraft.fontRendererObj.drawString(recipe.getEnergyNeeded() / TileEntityFoundryPowered.RATIO_FE + " FE", 0, 38, 0);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
      return false;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
      ingredients.setInput(FluidStack.class, recipe.getInputFluid());
      ingredients.setInputLists(ItemStack.class, Collections.singletonList(recipe.getInput().getItems()));
      ingredients.setOutput(FluidStack.class, recipe.getOutput());
    }
  }

  static public class Category implements IRecipeCategory<Wrapper>
  {

    protected final ResourceLocation background_location;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;
    
    @Nonnull
    protected final IDrawableAnimated arrow;
    
    public Category(IJeiHelpers helpers)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      background_location = new ResourceLocation("foundry", "textures/gui/infuser.png");
      
      
      IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_location, 176, 53, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/infuser.png");
      background = guiHelper.createDrawable(location, 15, 41, 137, 51);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.infuser");

    }

    @Override
    @Nonnull
    public IDrawable getBackground()
    {
      return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {
      arrow.draw(minecraft, 34, 18);
    }

    @Nonnull
    @Override
    public String getTitle()
    {
      return localizedName;
    }

    @Nonnull
    @Override
    public String getUid()
    {
      return "foundry.infuser";
    }

    @Override
    @Deprecated
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull Wrapper recipeWrapper)
    {

    }

    @Override
    public IDrawable getIcon()
    {
      return null;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
    {
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiFluidStacks.init(0, true, 59, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.init(1, false, 108, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.set(0, ingredients.getInputs(FluidStack.class).get(0));
      guiFluidStacks.set(1, ingredients.getOutputs(FluidStack.class).get(0));

      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

      guiItemStacks.init(0, true, 14, 17);
      guiItemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
      return Collections.emptyList();
    }
  }

  static public class Handler implements IRecipeHandler<Wrapper>
  {
    @Override
    @Nonnull
    public Class<Wrapper> getRecipeClass()
    {
      return Wrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
      return "foundry.infuser";
    }

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull Wrapper recipe)
    {
      return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull Wrapper recipe)
    {
      return true;
    }

    @Override
    public String getRecipeCategoryUid(Wrapper recipe)
    {
      return "foundry.infuser";
    }
  }

  static public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IInfuserRecipe recipe : InfuserRecipeManager.instance.getRecipes())
    {
      List<ItemStack> input = recipe.getInput().getItems();
      if(!input.isEmpty())
      {
        recipes.add(new Wrapper(recipe));
      }
    }

    return recipes;
  }
}
