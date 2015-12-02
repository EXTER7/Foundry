package exter.foundry.integration.minetweaker;

import minetweaker.IUndoableAction;

abstract public class AddRemoveAction
{
  private final class Add implements IUndoableAction
  {
    @Override
    public void apply()
    {
      add();
    }

    @Override
    public boolean canUndo()
    {
      return true;
    }

    @Override
    public void undo()
    {
      remove();
    }

    @Override
    public String describe()
    {
      return String.format("Adding %s recipe: %s", getRecipeType(), getDescription());
    }

    @Override
    public String describeUndo()
    {
      return String.format("Removing %s recipe: %s", getRecipeType(), getDescription());
    }

    @Override
    public Object getOverrideKey()
    {
      return null;
    }
  }

  private final class Remove implements IUndoableAction
  {
    @Override
    public void apply()
    {
      remove();
    }

    @Override
    public boolean canUndo()
    {
      return true;
    }

    @Override
    public void undo()
    {
      add();
    }

    @Override
    public String describe()
    {
      return String.format("Removing %s recipe: %s", getRecipeType(), getDescription());
    }

    @Override
    public String describeUndo()
    {
      return String.format("Adding %s recipe: %s", getRecipeType(), getDescription());
    }

    @Override
    public Object getOverrideKey()
    {
      return null;
    }
  }

  public final IUndoableAction action_add = new Add();
  public final IUndoableAction action_remove = new Remove();
  
  
  abstract protected void add();
  abstract protected void remove();
  abstract public String getDescription();
  abstract public String getRecipeType();
}
