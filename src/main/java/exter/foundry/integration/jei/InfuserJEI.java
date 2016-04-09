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
    @Nonnull
    private final List<FluidStack> input_fluid;
    @Nonnull
    private final List<FluidStack> output;
    @Nonnull
    private final List<List<ItemStack>> input;

    private final int energy;

    public Wrapper(@Nonnull FluidStack output, @Nonnull FluidStack input_fluid, @Nonnull List<ItemStack> input, int energy)
    {
      this.input_fluid = Collections.singletonList(input_fluid);
      this.output = Collections.singletonList(output);
      this.input = Collections.singletonList(input);
      this.energy = energy;
    }

    @Nonnull
    public List<List<ItemStack>> getInputs()
    {
      return input;
    }

    @Nonnull
    public List<ItemStack> getOutputs()
    {
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
      return input_fluid;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return output;
    }


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
      minecraft.fontRendererObj.drawString(energy / TileEntityFoundryPowered.RATIO_RF + " RF", 0, 38, 0);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
      return false;
    }
  }

  static public class Category implements IRecipeCategory
  {

    protected final ResourceLocation background_location;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;

    private final IJeiHelpers helpers;
    
    @Nonnull
    protected final IDrawableAnimated arrow;
    
    public Category(IJeiHelpers helpers)
    {
      this.helpers = helpers;
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiFluidStacks.init(0, true, 59, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.init(1, false, 108, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.set(0, recipeWrapper.getFluidInputs().get(0));
      guiFluidStacks.set(1, recipeWrapper.getFluidOutputs().get(0));

      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

      guiItemStacks.init(0, true, 14, 17);
      guiItemStacks.setFromRecipe(0, helpers.getStackHelper().toItemStackList(recipeWrapper.getInputs().get(0)));
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
  }

  static public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IInfuserRecipe recipe : InfuserRecipeManager.instance.getRecipes())
    {
      List<ItemStack> input = recipe.getInput().getItems();
      if(!input.isEmpty())
      {
        recipes.add(new Wrapper(
            recipe.getOutput(),
            recipe.getInputFluid(),
            input,
            recipe.getEnergyNeeded()));
      }
    }

    return recipes;
  }
}
