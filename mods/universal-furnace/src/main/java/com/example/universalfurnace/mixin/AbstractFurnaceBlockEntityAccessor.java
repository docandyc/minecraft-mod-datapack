package com.example.universalfurnace.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractFurnaceBlockEntity.class, remap = false)
public interface AbstractFurnaceBlockEntityAccessor {
    @Accessor("items")
    NonNullList<ItemStack> universalFurnace$getItems();

    @Accessor("litTime")
    int universalFurnace$getLitTime();

    @Accessor("litTime")
    void universalFurnace$setLitTime(int litTime);

    @Accessor("litDuration")
    void universalFurnace$setLitDuration(int litDuration);

    @Accessor("cookingProgress")
    int universalFurnace$getCookingProgress();

    @Accessor("cookingProgress")
    void universalFurnace$setCookingProgress(int cookingProgress);

    @Accessor("cookingTotalTime")
    void universalFurnace$setCookingTotalTime(int cookingTotalTime);
}
