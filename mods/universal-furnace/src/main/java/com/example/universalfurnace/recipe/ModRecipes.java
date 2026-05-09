package com.example.universalfurnace.recipe;

import com.example.universalfurnace.UniversalFurnace;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, UniversalFurnace.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, UniversalFurnace.MOD_ID);

    public static final RegistryObject<RecipeType<FuelSmeltingRecipe>> FUEL_SMELTING_TYPE =
            RECIPE_TYPES.register("fuel_smelting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(
                    UniversalFurnace.MOD_ID,
                    "fuel_smelting"
            )));

    public static final RegistryObject<RecipeSerializer<FuelSmeltingRecipe>> FUEL_SMELTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fuel_smelting", FuelSmeltingRecipeSerializer::new);

    private ModRecipes() {
    }
}
