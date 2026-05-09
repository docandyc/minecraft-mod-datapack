package com.example.universalfurnace;

import com.example.universalfurnace.furnace.FuelEvents;
import com.example.universalfurnace.recipe.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UniversalFurnace.MOD_ID)
public class UniversalFurnace {
    public static final String MOD_ID = "universal_furnace";

    public UniversalFurnace(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        ModRecipes.RECIPE_TYPES.register(modBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modBus);
        MinecraftForge.EVENT_BUS.addListener(FuelEvents::onFuelBurnTime);
    }
}
