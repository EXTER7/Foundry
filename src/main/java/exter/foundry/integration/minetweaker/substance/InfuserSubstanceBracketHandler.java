package exter.foundry.integration.minetweaker.substance;

import java.util.List;

import exter.foundry.api.substance.InfuserSubstance;
import minetweaker.IBracketHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.item.IngredientAny;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;

@BracketHandler(priority = 100)
public class InfuserSubstanceBracketHandler implements IBracketHandler
{
  private final IZenSymbol symbolAny;
  private final IJavaMethod method;

  public InfuserSubstanceBracketHandler()
  {
    symbolAny = MineTweakerAPI.getJavaStaticFieldSymbol(IngredientAny.class, "INSTANCE");
    method = MineTweakerAPI.getJavaMethod(InfuserSubstanceBracketHandler.class, "getSubstance", String.class);
  }
  
  public static IInfuserSubstance getSubstance(String name)
  {
    return new MTInfuserSubstance(new InfuserSubstance(name,1));
  }

  @Override
  public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens)
  {
    if(tokens.size() == 1 && tokens.get(0).getValue().equals("*"))
    {
      return symbolAny;
    }

    if(tokens.size() > 2)
    {
      if(tokens.get(0).getValue().equals("infusersubstance") && tokens.get(1).getValue().equals(":"))
      {
        StringBuilder substance_builder = new StringBuilder();
        int i;
        for(i = 2; i < tokens.size(); i++)
        {
          Token token = tokens.get(i);
          substance_builder.append(token.getValue());
        }

        String substance = substance_builder.toString();
        if(substance != null)
        {
          return new Symbol(environment, substance_builder.toString());
        }
      }
    }

    return null;
  }

  private class Symbol implements IZenSymbol
  {
    private final IEnvironmentGlobal environment;
    private final String name;

    public Symbol(IEnvironmentGlobal environment, String name)
    {
      this.environment = environment;
      this.name = name;
    }

    @Override
    public IPartialExpression instance(ZenPosition position)
    {
      return new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name));
    }
  }
}
