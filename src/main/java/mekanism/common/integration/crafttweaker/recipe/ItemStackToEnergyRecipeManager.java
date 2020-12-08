package mekanism.common.integration.crafttweaker.recipe;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.google.gson.JsonSyntaxException;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackToEnergyRecipe;
import mekanism.api.recipes.inputs.ItemStackIngredient;
import mekanism.common.integration.crafttweaker.CrTConstants;
import mekanism.common.integration.crafttweaker.ingredient.CrTItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.impl.EnergyConversionIRecipe;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name(CrTConstants.CLASS_RECIPE_ITEM_STACK_TO_ENERGY)
public abstract class ItemStackToEnergyRecipeManager extends MekanismRecipeManager<ItemStackToEnergyRecipe> {

    protected ItemStackToEnergyRecipeManager(MekanismRecipeType<ItemStackToEnergyRecipe> recipeType) {
        super(recipeType);
    }

    @ZenCodeType.Method
    public void addRecipe(String name, CrTItemStackIngredient input, String output) {
        FloatingLong outputEnergy = FloatingLong.parseFloatingLong(output, true);
        if (outputEnergy.isZero()) {
            throw new JsonSyntaxException("Output must be greater than zero.");
        }
        addRecipe(makeRecipe(getAndValidateName(name), input.getInternal(), outputEnergy));
    }

    protected abstract ItemStackToEnergyRecipe makeRecipe(ResourceLocation id, ItemStackIngredient input, FloatingLong output);

    @Override
    protected ActionAddMekanismRecipe getAction(ItemStackToEnergyRecipe recipe) {
        return new ActionAddMekanismRecipe(recipe) {
            @Override
            protected String describeOutputs() {
                //TODO: Figure out how we want to represent floating longs in CrT and how possible it is to make custom numbers in ZS
                // In theory we could make just add an implicit cast from string to a CrTFloatingLong?? might be a bit messy though
                return getRecipe().getOutputDefinition().toString();
            }
        };
    }

    @ZenRegister
    @ZenCodeType.Name(CrTConstants.CLASS_RECIPE_ENERGY_CONVERSION)
    public static class EnergyConversionRecipeManager extends ItemStackToEnergyRecipeManager {

        public static final EnergyConversionRecipeManager INSTANCE = new EnergyConversionRecipeManager();

        private EnergyConversionRecipeManager() {
            super(MekanismRecipeType.ENERGY_CONVERSION);
        }

        @Override
        protected ItemStackToEnergyRecipe makeRecipe(ResourceLocation id, ItemStackIngredient input, FloatingLong output) {
            return new EnergyConversionIRecipe(id, input, output);
        }
    }
}