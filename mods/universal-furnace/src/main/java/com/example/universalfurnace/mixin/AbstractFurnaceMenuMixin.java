package com.example.universalfurnace.mixin;

import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractFurnaceMenu.class, remap = false)
public class AbstractFurnaceMenuMixin {
    @Inject(method = "isFuel", at = @At("HEAD"), cancellable = true)
    private void universalFurnace$allowAnyFuel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.isEmpty()) {
            cir.setReturnValue(true);
        }
    }
}
