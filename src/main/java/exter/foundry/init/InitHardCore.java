package exter.foundry.init;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import exter.foundry.config.FoundryConfig;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class InitHardCore
{
  static public void init()
  {
    if(FoundryConfig.hardcore_remove_ingot_nugget)
    { // Remove 9 nuggets -> ingot recipes (only if they can be melted)
      Set<IRecipe> remove = new HashSet<IRecipe>();
      InventoryCrafting grid = new InventoryCrafting(new Container(){@Override public boolean canInteractWith(EntityPlayer playerIn){return false;}},3,3);
      for(String ore_name:OreDictionary.getOreNames())
      {
        if(ore_name.startsWith("nugget"))
        {
          for(ItemStack item:OreDictionary.getOres(ore_name))
          {
            if(MeltingRecipeManager.instance.findRecipe(item) != null)
            {
              for(int i = 0; i < 9; i++)
              {
                grid.setInventorySlotContents(i, item);
              }
              for(IRecipe recipe : CraftingManager.getInstance().getRecipeList())
              {
                if(recipe.matches(grid, null))
                {
                  remove.add(recipe);
                }
              }
            }
          }
        }
      }
      CraftingManager.getInstance().getRecipeList().removeAll(remove);
    }
    
    if(FoundryConfig.hardcore_furnace_remove_ingots)
    { // Remove furnace recipes that outputs ingots (except the ones in the keep list or can't be melted).
      Iterator<Map.Entry<ItemStack,ItemStack>> iter = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator();
      while(iter.hasNext())
      {
        Map.Entry<ItemStack,ItemStack> recipe = iter.next();
        Set<String> ore_names = FoundryMiscUtils.getAllItemOreDictionaryNames(recipe.getValue());
        for(String name:ore_names)
        {
          if(name.startsWith("ingot") && !FoundryConfig.hardcore_furnace_keep_ingots.contains(name) && MeltingRecipeManager.instance.findRecipe(recipe.getValue()) != null)
          {
            iter.remove();
            break;
          }
        }
      }
    }    
  }
}
