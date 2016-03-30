package exter.foundry.recipes;

import java.util.Arrays;

import exter.foundry.api.recipe.IMoldRecipe;
import net.minecraft.item.ItemStack;

public class MoldRecipe implements IMoldRecipe
{
  public final int width;
  public final int height;
  public final int[] recipe;
  
  private int[][] match_recipes;
  
  public final ItemStack output;


  @Override
  public ItemStack getOutput()
  {
    return output.copy();
  }
  
  public MoldRecipe(ItemStack output,int width,int height,int[] recipe)
  {
    if(output == null)
    {
      throw new IllegalArgumentException("Mold recipe output cannot be null");
    }
    this.output = output.copy();

    if(width < 1 || width > 6)
    {
      throw new IllegalArgumentException("Mold recipe width must be between 1 and 6.");
    }
    if(height < 1 || height > 6)
    {
      throw new IllegalArgumentException("Mold recipe height must be between 1 and 6.");
    }
    if(recipe == null || recipe.length < width*height)
    {
      throw new IllegalArgumentException("Mold recipe array cannot be null and the length must be at least width*height.");
    }
    
    for(int v:recipe)
    {
      if(v < 0 || v > 4)
      {
        throw new IllegalArgumentException("Mold recipe grid values must be between 0 and 4.");
      }
    }
    
    this.width = width;
    this.height = height;
    this.recipe = recipe.clone();

    int match_x = 6 - width + 1;
    int match_y = 6 - height + 1;
    match_recipes = new int[match_x * match_y][36];
    
    for(int j = 0; j < match_y; j++)
    {
      for(int i = 0; i < match_x; i++)
      {
        int[] match_grid = match_recipes[j * match_x + i];
        Arrays.fill(match_grid, 0);
        for(int y = 0; y < height; y++)
        {
          for(int x = 0; x < width; x++)
          {
            match_grid[(y + j)*6 + (x + i)] = recipe[y * width + x];
          }
        }
      }
    }
  }

  @Override
  public int[] getRecipeGrid()
  {
    return recipe.clone();
  }

  @Override
  public int getWidth()
  {
    return width;
  }

  @Override
  public int getHeight()
  {
    return height;
  }

  @Override
  public boolean matchesRecipe(int[] grid)
  {
    for(int i = 0; i < match_recipes.length; i++)
    {
      boolean matches = true;
      for(int j = 0; j < 36; j++)
      {
        if(grid[j] != match_recipes[i][j])
        {
          matches = false;
          break;
        }
      }
      if(matches)
      {
        return true;
      }
    }
    return false;
  }
}
