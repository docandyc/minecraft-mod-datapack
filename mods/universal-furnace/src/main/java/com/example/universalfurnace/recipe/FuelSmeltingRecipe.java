package com.example.universalfurnace.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class FuelSmeltingRecipe implements Recipe<FuelSmeltingInput> {
    private final Ingredient input;
    private final Ingredient fuel;
    private final ItemStack result;
    private final int cookTime;
    private final float experience;

    public FuelSmeltingRecipe(Ingredient input, Ingredient fuel, ItemStack result, int cookTime, float experience) {
        this.input = input;
        this.fuel = fuel;
        this.result = result;
        this.cookTime = cookTime;
        this.experience = experience;
    }

    public Ingredient input() {
        return input;
    }

    public Ingredient fuel() {
        return fuel;
    }

    public ItemStack result() {
        return result;
    }

    public int cookTime() {
        return cookTime;
    }

    public float experience() {
        return experience;
    }

    @Override
    public boolean matches(FuelSmeltingInput recipeInput, Level level) {
        return input.test(recipeInput.input()) && fuel.test(recipeInput.fuel());
    }

    @Override
    public ItemStack assemble(FuelSmeltingInput recipeInput, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        ingredients.add(fuel);
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FUEL_SMELTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FUEL_SMELTING_TYPE.get();
    }
}
