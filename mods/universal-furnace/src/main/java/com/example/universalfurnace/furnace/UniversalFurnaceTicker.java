package com.example.universalfurnace.furnace;

import com.example.universalfurnace.mixin.AbstractFurnaceBlockEntityAccessor;
import com.example.universalfurnace.recipe.FuelSmeltingInput;
import com.example.universalfurnace.recipe.FuelSmeltingRecipe;
import com.example.universalfurnace.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class UniversalFurnaceTicker {
    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int RESULT_SLOT = 2;

    private UniversalFurnaceTicker() {
    }

    public static boolean tick(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity furnace) {
        AbstractFurnaceBlockEntityAccessor access = (AbstractFurnaceBlockEntityAccessor) furnace;
        ItemStack input = access.universalFurnace$getItems().get(INPUT_SLOT);
        ItemStack fuel = access.universalFurnace$getItems().get(FUEL_SLOT);

        Optional<RecipeHolder<FuelSmeltingRecipe>> recipeHolder = level.getRecipeManager().getRecipeFor(
                ModRecipes.FUEL_SMELTING_TYPE.get(),
                new FuelSmeltingInput(input, fuel),
                level
        );
        ItemStack result;
        int cookTime;

        if (recipeHolder.isPresent()) {
            FuelSmeltingRecipe recipe = recipeHolder.get().value();
            result = recipe.assemble(new FuelSmeltingInput(input, fuel), level.registryAccess());
            cookTime = recipe.cookTime();
        } else {
            Optional<BulkFuelSmeltingRecipes.Entry> bulkRecipe = BulkFuelSmeltingRecipes.find(input, fuel);
            if (bulkRecipe.isEmpty()) {
                return false;
            }

            BulkFuelSmeltingRecipes.Entry recipe = bulkRecipe.get();
            result = recipe.assemble();
            cookTime = recipe.cookTime();
        }

        boolean wasLit = access.universalFurnace$getLitTime() > 0;
        boolean changed = false;

        if (input.isEmpty() || fuel.isEmpty() || !canOutput(result, access)) {
            access.universalFurnace$setLitTime(0);
            access.universalFurnace$setCookingProgress(Math.max(0, access.universalFurnace$getCookingProgress() - 2));
            changed = true;
        } else {
            access.universalFurnace$setLitTime(1);
            access.universalFurnace$setLitDuration(cookTime);
            access.universalFurnace$setCookingTotalTime(cookTime);
            access.universalFurnace$setCookingProgress(access.universalFurnace$getCookingProgress() + 1);
            changed = true;

            if (access.universalFurnace$getCookingProgress() >= cookTime) {
                craft(result, access);
                access.universalFurnace$setCookingProgress(0);
            }
        }

        boolean isLit = access.universalFurnace$getLitTime() > 0;
        if (wasLit != isLit) {
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, isLit), 3);
            changed = true;
        }

        if (changed) {
            furnace.setChanged();
        }

        return true;
    }

    private static boolean canOutput(ItemStack result, AbstractFurnaceBlockEntityAccessor access) {
        ItemStack output = access.universalFurnace$getItems().get(RESULT_SLOT);

        if (result.isEmpty()) {
            return false;
        }
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private static void craft(ItemStack result, AbstractFurnaceBlockEntityAccessor access) {
        ItemStack resultCopy = result.copy();
        ItemStack output = access.universalFurnace$getItems().get(RESULT_SLOT);

        access.universalFurnace$getItems().get(INPUT_SLOT).shrink(1);
        access.universalFurnace$getItems().get(FUEL_SLOT).shrink(1);

        if (output.isEmpty()) {
            access.universalFurnace$getItems().set(RESULT_SLOT, resultCopy);
        } else {
            output.grow(resultCopy.getCount());
        }
    }
}
