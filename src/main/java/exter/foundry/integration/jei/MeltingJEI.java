package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.gui.GuiMeltingCrucible;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
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

public class MeltingJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final IDrawable temp;
    
    private final IMeltingRecipe recipe;
    

    public Wrapper(IJeiHelpers helpers,@Nonnull IMeltingRecipe recipe)
    {
      this.recipe = recipe;
      ResourceLocation background_location = new ResourceLocation("foundry", "textures/gui/crucible.png");

      temp = helpers.getGuiHelper().createDrawable(background_location, 176, 53,
          (recipe.getMeltingPoint() * 100 - TileEntityMeltingCrucibleBasic.TEMP_MIN) * 54 / (500000 - TileEntityMeltingCrucibleBasic.TEMP_MIN), 12);
    }

    @Override
    @Deprecated
    public List<List<ItemStack>> getInputs()
    {
      return null;
    }

    @Override
    @Deprecated
    public List<ItemStack> getOutputs()
    {
      return null;
    }

    @Override
    @Deprecated
    public List<FluidStack> getFluidInputs()
    {
      return null;
    }

    @Override
    @Deprecated
    public List<FluidStack> getFluidOutputs()
    {
      return Collections.singletonList(recipe.getOutput());
    }

    @Override
    @Deprecated
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
      return null;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
      temp.draw(minecraft,11,41);
      minecraft.fontRendererObj.drawString(recipe.getMeltingPoint() + " °K", 14, 28, 0);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
      return false;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
      ingredients.setInputLists(ItemStack.class, Collections.singletonList(recipe.getInput().getItems()));
      ingredients.setOutput(FluidStack.class, recipe.getOutput());
    }
  }

  static public class Category implements IRecipeCategory<Wrapper>
  {

    protected final ResourceLocation backgroundLocation;
    @Nonnull
    protected final IDrawableAnimated arrow;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;


    public Category(IJeiHelpers helpers)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/crucible.png");


      IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 78, 24, 17);
      this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/crucible.png");
      background = guiHelper.createDrawable(location, 30, 16, 94, 54);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.melting");

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
      arrow.draw(minecraft, 49, 7);
    }

    @Override
    public String getTitle()
    {
      return localizedName;
    }

    @Nonnull
    @Override
    public String getUid()
    {
      return "foundry.melting";
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
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiItemStacks.init(0, true, 24, 6);
      guiFluidStacks.init(1, false, 77, 6, 16, GuiMeltingCrucible.TANK_HEIGHT, FoundryAPI.CRUCIBLE_TANK_CAPACITY,false,tank_overlay);
      guiItemStacks.set(0,ingredients.getInputs(ItemStack.class).get(0));
      guiFluidStacks.set(1,ingredients.getOutputs(FluidStack.class).get(0));
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
      return "foundry.melting";
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
      return "foundry.melting";
    }
  }

  static public List<Wrapper> getRecipes(IJeiHelpers helpers)
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IMeltingRecipe recipe : MeltingRecipeManager.instance.getRecipes())
    {
      List<ItemStack> input = recipe.getInput().getItems();

      if(!input.isEmpty())
      {
        recipes.add(new Wrapper(helpers, recipe));
      }
    }

    return recipes;
  }
}
