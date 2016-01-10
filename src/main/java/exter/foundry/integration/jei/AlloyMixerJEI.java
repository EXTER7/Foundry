package exter.foundry.integration.jei;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class AlloyMixerJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final List<FluidStack> input;
    @Nonnull
    private final List<FluidStack> output;

    public Wrapper(@Nonnull FluidStack output, List<FluidStack> input)
    {
      this.input = input;
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
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
      return input;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return output;
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
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;

    public Category(IJeiHelpers helpers)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/alloymixer.png");

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloymixer.png");
      background = guiHelper.createDrawable(location, 18, 44, 132, 37);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 35);
      localizedName = Translator.translateToLocal("gui.jei.alloymixer");
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
      return "foundry.alloymixer";
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      int i;
      for(i = 0; i < recipeWrapper.getFluidInputs().size(); i++)
      {
        guiFluidStacks.init(i, true, 8 + 21 * i, 1, 16, GuiAlloyMixer.TANK_HEIGHT, recipeWrapper.getFluidInputs().get(i).amount,false,tank_overlay);
        guiFluidStacks.set(i, recipeWrapper.getFluidInputs().get(i));
      }
      guiFluidStacks.init(5, false, 115, 1, 16, GuiAlloyMixer.TANK_HEIGHT, recipeWrapper.getFluidOutputs().get(0).amount,false,tank_overlay);
      guiFluidStacks.set(5, recipeWrapper.getFluidOutputs().get(0));
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
      return "foundry.alloymixer";
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

    for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.getRecipes())
    {
      recipes.add(new Wrapper(
          recipe.getOutput(),recipe.getInputs()));
    }

    return recipes;
  }
}
