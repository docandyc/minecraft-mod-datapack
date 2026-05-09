package com.example.universalfurnace.furnace;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BulkFuelSmeltingRecipes {
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkFuelSmeltingRecipes.class);
    private static final Gson GSON = new Gson();
    private static final String RESOURCE_PATH = "/data/universal_furnace/recipes.json";

    private static volatile Map<Key, Entry> recipes;

    private BulkFuelSmeltingRecipes() {
    }

    public static Optional<Entry> find(ItemStack input, ItemStack fuel) {
        if (input.isEmpty() || fuel.isEmpty()) {
            return Optional.empty();
        }

        ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.getItem());
        ResourceLocation fuelId = BuiltInRegistries.ITEM.getKey(fuel.getItem());
        return Optional.ofNullable(getRecipes().get(new Key(inputId, fuelId)));
    }

    private static Map<Key, Entry> getRecipes() {
        Map<Key, Entry> current = recipes;
        if (current == null) {
            synchronized (BulkFuelSmeltingRecipes.class) {
                current = recipes;
                if (current == null) {
                    current = loadRecipes();
                    recipes = current;
                }
            }
        }
        return current;
    }

    private static Map<Key, Entry> loadRecipes() {
        try (InputStream stream = BulkFuelSmeltingRecipes.class.getResourceAsStream(RESOURCE_PATH)) {
            if (stream == null) {
                LOGGER.warn("Universal furnace bulk recipe file not found: {}", RESOURCE_PATH);
                return Map.of();
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
                Map<Key, Entry> loaded = new HashMap<>(array.size());

                for (JsonElement element : array) {
                    JsonObject object = element.getAsJsonObject();
                    RecipeJson recipe = GSON.fromJson(object, RecipeJson.class);
                    addRecipe(loaded, recipe);
                }

                LOGGER.info("Loaded {} universal furnace bulk recipes", loaded.size());
                return Map.copyOf(loaded);
            }
        } catch (RuntimeException | java.io.IOException exception) {
            LOGGER.error("Failed to load universal furnace bulk recipes from {}", RESOURCE_PATH, exception);
            return Map.of();
        }
    }

    private static void addRecipe(Map<Key, Entry> loaded, RecipeJson recipe) {
        ResourceLocation inputId = parseItemId(recipe.input);
        ResourceLocation fuelId = parseItemId(recipe.fuel);
        ResourceLocation outputId = parseItemId(recipe.output);

        if (inputId == null || fuelId == null || outputId == null) {
            return;
        }

        int count = recipe.count == null ? 1 : Math.max(1, recipe.count);
        int cookTime = recipe.cook_time == null ? 200 : Math.max(1, recipe.cook_time);
        float experience = recipe.experience == null ? 0.0F : recipe.experience;
        loaded.put(new Key(inputId, fuelId), new Entry(outputId, count, cookTime, experience));
    }

    private static ResourceLocation parseItemId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        ResourceLocation location = ResourceLocation.tryParse(id);
        if (location == null || !BuiltInRegistries.ITEM.containsKey(location)) {
            LOGGER.warn("Skipping universal furnace recipe with unknown item id: {}", id);
            return null;
        }
        return location;
    }

    private record Key(ResourceLocation input, ResourceLocation fuel) {
    }

    private record RecipeJson(String input, String fuel, String output, Integer count, Integer cook_time, Float experience) {
    }

    public record Entry(ResourceLocation output, int count, int cookTime, float experience) {
        public ItemStack assemble() {
            Item item = BuiltInRegistries.ITEM.get(output);
            return new ItemStack(item, count);
        }
    }
}
