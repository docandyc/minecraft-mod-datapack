package com.example.universalfurnace.furnace;

import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

public final class FuelEvents {
    private static final int DEFAULT_BURN_TIME = 200;

    private FuelEvents() {
    }

    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().isEmpty() && event.getBurnTime() <= 0) {
            event.setBurnTime(DEFAULT_BURN_TIME);
        }
    }
}
