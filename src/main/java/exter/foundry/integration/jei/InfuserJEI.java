package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.gui.GuiMetalInfuser;
import exter.foundry.recipes.manager.InfuserRecipeManager;
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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;


public class InfuserJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final List<FluidStack> input;
    @Nonnull
    private final List<FluidStack> output;
    @Nonnull
    private final InfuserSubstance substance;
    @Nonnull
    private final IDrawable substance_drawable;
    
    public Wrapper(IJeiHelpers helpers,@Nonnull FluidStack output, @Nonnull FluidStack input, @Nonnull InfuserSubstance substance)
    {
      this.input = Collections.singletonList(input);
      this.output = Collections.singletonList(output);
      this.substance = substance;
      this.substance_drawable = JEIHelper.getSubstanceDrawable(helpers, substance);
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
      if(substance_drawable != null)
      {
        JEIHelper.setSubstanceGLColor(substance);
        substance_drawable.draw(minecraft, 56, 2 + 47 - substance_drawable.getHeight());
        GlStateManager.resetColor();
      }
    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public List<String> getTooltipStrings(int x, int y)
    {
      if(x >= 56 && x <= 66 && y >= 2 && y <= 49)
      {
        return Lists.newArrayList(
            StatCollector.translateToLocal("substance." + substance.type),
            substance.amount + " mL");
      }
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
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/infuser.png");

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

      guiFluidStacks.init(0, true, 70, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.init(1, false, 119, 2, 16, GuiMetalInfuser.TANK_HEIGHT, FoundryAPI.INFUSER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.set(0, recipeWrapper.getFluidInputs().get(0));
      guiFluidStacks.set(1, recipeWrapper.getFluidOutputs().get(0));
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

  static public List<Wrapper> getRecipes(IJeiHelpers helpers)
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IInfuserRecipe recipe : InfuserRecipeManager.instance.getRecipes())
    {
      recipes.add(new Wrapper(helpers,
          recipe.getOutput(),
          recipe.getInputFluid(),
          recipe.getInputSubstance()));
    }

    return recipes;
  }
}
