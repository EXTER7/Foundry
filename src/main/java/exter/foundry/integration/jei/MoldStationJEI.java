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
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public class MoldStationJEI
{
  static public class Wrapper implements IRecipeWrapper
  {
    private final IMoldRecipe recipe;

    private final IDrawable[] carve_drawables;
    
    public Wrapper(IDrawable[] carve_drawables,IMoldRecipe recipe)
    {
      this.carve_drawables = carve_drawables;
      this.recipe = recipe;
    }

    @Override
    public List<String> getTooltipStrings(int mx, int my)
    {
      int width = recipe.getWidth();
      int height = recipe.getHeight();
      if(mx >= 7 && mx < 73 && my >= 7 && my < 73)
      {
        int x = (mx - 7) / 11 - (3 - FoundryMiscUtils.divCeil(width,2));
        int y = (my - 7) / 11 - (3 - FoundryMiscUtils.divCeil(height,2));

        int depth = 0;
        if(x >= 0 && x < width && y >= 0 && y < height)
        {
          depth = recipe.getRecipeGrid()[y * width + x];
        }
        return Collections.singletonList("Depth: " + depth);
      }
      return null;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
      int width = recipe.getWidth();
      int height = recipe.getHeight();
      int[] grid = recipe.getRecipeGrid();

      int left = (3 - FoundryMiscUtils.divCeil(width,2));
      int top = (3 - FoundryMiscUtils.divCeil(height,2));
      
      for(int y = 0; y < height; y++)
      {
        for(int x = 0; x < width; x++)
        {
          int i = grid[y*width + x];
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

    @Override
    public void getIngredients(IIngredients ingredients)
    {
      ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }
  }

  static public class Category implements IRecipeCategory<Wrapper>
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

    public Category(IJeiHelpers helpers)
    {
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
    public IDrawable getIcon()
    {
      return null;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
    {
      IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();

      gui_items.init(0, false, 110, 23);
      gui_items.set(0, ingredients.getOutputs(ItemStack.class).get(0));
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
       return "foundry.mold";
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
      recipes.add(new Wrapper(carve_drawables,recipe));
    }

    return recipes;
  }
}
