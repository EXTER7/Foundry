package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.gui.GuiMetalAtomizer;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.StackUtil;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;


public class AtomizerJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final List<FluidStack> input;
    @Nonnull
    private final List<ItemStack> output;

    private static final FluidStack WATER = new FluidStack(FluidRegistry.WATER,50);
    
    public Wrapper(@Nonnull ItemStack output, FluidStack input)
    {
      this.input = Lists.newArrayList(input,WATER);
      this.output = Collections.singletonList(output);
    }

    @Nonnull
    public List<List<ItemStack>> getInputs()
    {
      return Collections.emptyList();
    }

    @Nonnull
    public List<ItemStack> getOutputs()
    {
      return output;
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
      return input;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return Collections.emptyList();
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
      return null;
    }
  }

  static public class Category implements IRecipeCategory
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
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/atomizer.png");


      IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 53, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/atomizer.png");
      background = guiHelper.createDrawable(location, 8, 19, 133, 51);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.atomizer");
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
      arrow.draw(minecraft, 52, 18);
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
      return "foundry.atomizer";
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiItemStacks.init(0, false, 77, 17);
      guiFluidStacks.init(1, true, 31, 2, 16, GuiMetalAtomizer.TANK_HEIGHT, FoundryAPI.ATOMIZER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.init(2, true, 115, 2, 16, GuiMetalAtomizer.TANK_HEIGHT, FoundryAPI.ATOMIZER_TANK_CAPACITY,false,tank_overlay);
      guiItemStacks.setFromRecipe(0, StackUtil.toItemStackList(recipeWrapper.getOutputs().get(0)));
      guiFluidStacks.set(1, recipeWrapper.getFluidInputs().get(0));
      guiFluidStacks.set(2, recipeWrapper.getFluidInputs().get(1));
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
      return "foundry.atomizer";
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
  }

  static public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IAtomizerRecipe recipe : AtomizerRecipeManager.instance.getRecipes())
    {
      ItemStack output = recipe.getOutputItem();

      if(output != null)
      {
        recipes.add(new Wrapper(output,recipe.getInput()));
      }
    }

    return recipes;
  }
}
