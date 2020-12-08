package mekanism.common.integration.crafttweaker.ingredient;

import com.blamejared.crafttweaker.api.brackets.CommandStringDisplayable;
import com.blamejared.crafttweaker.impl.tag.MCTag;
import java.util.function.Function;
import java.util.function.IntFunction;
import mekanism.api.recipes.inputs.InputIngredient;
import net.minecraft.tags.ITag;

public class CrTIngredientWrapper<TYPE, INGREDIENT extends InputIngredient<TYPE>> {

    protected static void assertValidAmount(String ingredientType, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(ingredientType + " can only be created with a size of at least one. Received size was: " + amount);
        }
    }

    protected static <TYPE, CRT_TYPE extends CommandStringDisplayable> ITag<TYPE> assertValidAndGet(MCTag<CRT_TYPE> crtTag, long amount,
          Function<MCTag<CRT_TYPE>, ITag<TYPE>> getter, String ingredientType) {
        assertValidAmount(ingredientType, amount);
        ITag<TYPE> tag = getter.apply(crtTag);
        if (tag == null) {
            throw new IllegalArgumentException("Tag " + crtTag.getCommandString() + " does not exist.");
        }
        return tag;
    }

    @SafeVarargs
    protected static <TYPE, INGREDIENT extends InputIngredient<TYPE>, CRT_INGREDIENT extends CrTIngredientWrapper<TYPE, INGREDIENT>> CRT_INGREDIENT
    createMulti(String ingredientType, IntFunction<INGREDIENT[]> arrayCreator, Function<INGREDIENT[], CRT_INGREDIENT> multiCreator, CRT_INGREDIENT... crtIngredients) {
        if (crtIngredients.length == 0) {
            throw new IllegalArgumentException("Multi " + ingredientType + " ingredients cannot be made out of no ingredients!");
        } else if (crtIngredients.length == 1) {
            return crtIngredients[0];
        }
        INGREDIENT[] ingredients = arrayCreator.apply(crtIngredients.length);
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = crtIngredients[i].getInternal();
        }
        return multiCreator.apply(ingredients);
    }

    private final INGREDIENT ingredient;

    protected CrTIngredientWrapper(INGREDIENT ingredient) {
        this.ingredient = ingredient;
    }

    public INGREDIENT getInternal() {
        return ingredient;
    }
}