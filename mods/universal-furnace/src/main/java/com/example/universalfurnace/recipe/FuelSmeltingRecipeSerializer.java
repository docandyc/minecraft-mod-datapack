package com.example.universalfurnace.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FuelSmeltingRecipeSerializer implements RecipeSerializer<FuelSmeltingRecipe> {
    private static final MapCodec<FuelSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(FuelSmeltingRecipe::input),
            Ingredient.CODEC.fieldOf("fuel").forGetter(FuelSmeltingRecipe::fuel),
            ItemStack.CODEC.fieldOf("result").forGetter(FuelSmeltingRecipe::result),
            Codec.INT.optionalFieldOf("cook_time", 200).forGetter(FuelSmeltingRecipe::cookTime),
            Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(FuelSmeltingRecipe::experience)
    ).apply(instance, FuelSmeltingRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, FuelSmeltingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, FuelSmeltingRecipe::input,
            Ingredient.CONTENTS_STREAM_CODEC, FuelSmeltingRecipe::fuel,
            ItemStack.STREAM_CODEC, FuelSmeltingRecipe::result,
            ByteBufCodecs.INT, FuelSmeltingRecipe::cookTime,
            ByteBufCodecs.FLOAT, FuelSmeltingRecipe::experience,
            FuelSmeltingRecipe::new
    );

    @Override
    public MapCodec<FuelSmeltingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FuelSmeltingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
