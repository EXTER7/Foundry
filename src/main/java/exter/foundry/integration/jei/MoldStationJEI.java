package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;


public class MoldStationJEI
{
  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final List<ItemStack> output;
    
    private final int width;
    private final int height;
    private final int[] recipe;

    private final IDrawable[] carve_drawables;
    
    public Wrapper(IDrawable[] carve_drawables,@Nonnull ItemStack output, int width,int height,int[] recipe)
    {
      this.carve_drawables = carve_drawables;
      this.output = Collections.singletonList(output);
      this.width = width;
      this.height = height;
      this.recipe = recipe;
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
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return Collections.emptyList();
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

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
      int left = (3 - FoundryMiscUtils.divCeil(width,2));
      int top = (3 - FoundryMiscUtils.divCeil(height,2));
      for(int y = 0; y < height; y++)
      {
        for(int x = 0; x < width; x++)
        {
          int i = recipe[y*width + x];
          if(i > 0)
          {
            carve_drawables[i - 1].draw(minecraft, 7 + (x + left) * 11, 7 + (y + top) * 11);
          }
        }
      }
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
    protected final IDrawableAnimated arrow;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    @Nonnull
    private final IDrawable grid_drawable;

    
    private IJeiHelpers helpers;

    public Category(IJeiHelpers helpers)
    {
      this.helpers = helpers;
      IGuiHelper guiHelper = helpers.getGuiHelper();
      background_location = new ResourceLocation("foundry", "textures/gui/moldstation.png");

      grid_drawable = guiHelper.createDrawable(background_location, 176, 31, 76, 76);
      
      IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_location, 176, 14, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/moldstation.png");
      background = guiHelper.createDrawable(location, 36, 14, 133, 81);
      localizedName = Translator.translateToLocal("gui.jei.mold");
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
      grid_drawable.draw(minecraft, 2, 2);
    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {
      arrow.draw(minecraft, 81, 25);
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
      return "foundry.mold";
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();
      IStackHelper stack_helper = helpers.getStackHelper();

      gui_items.init(0, false, 110, 23);
      gui_items.setFromRecipe(0, stack_helper.toItemStackList(recipeWrapper.getOutputs().get(0)));
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
      return "foundry.mold";
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
    IGuiHelper guiHelper = helpers.getGuiHelper();
    IDrawable[] carve_drawables = new IDrawable[4];
    ResourceLocation location = new ResourceLocation("foundry", "textures/gui/moldstation.png");
    for(int i = 0; i < 4; i++)
    {
      carve_drawables[i] = guiHelper.createDrawable(location, 176, 107 + i * 11, 11, 11);
    }
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IMoldRecipe recipe : MoldRecipeManager.instance.getRecipes())
    {
      recipes.add(new Wrapper(carve_drawables,
          recipe.getOutput(),recipe.getWidth(),recipe.getHeight(),recipe.getRecipeGrid()));
    }

    return recipes;
  }
}
