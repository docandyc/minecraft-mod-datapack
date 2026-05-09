package com.example.universalfurnace.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FuelSmeltingInput(ItemStack input, ItemStack fuel) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case 0 -> input;
            case 1 -> fuel;
            default -> throw new IllegalArgumentException("No item for index " + slot);
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
