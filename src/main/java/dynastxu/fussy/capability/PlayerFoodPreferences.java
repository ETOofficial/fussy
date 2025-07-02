package dynastxu.fussy.capability;

import dynastxu.fussy.Config;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerFoodPreferences implements IFoodPreferences, INBTSerializable<CompoundTag> {
    private static final float DEFAULT_PREFERENCE = (float) Config.DEFAULT_PREFERENCE_PERCENT.getAsInt() / 100;
    private static final float PREFERENCE_REDUCE = (float) Config.PREFERENCE_REDUCE_PERCENT.getAsInt() / 100;

    private final Map<Item, Float> foodPreferences = new HashMap<>();

    public PlayerFoodPreferences() {
    }

    @Override
    public Map<Item, Float> getFoodPreferences() {
        return foodPreferences;
    }

    @Override
    public float getPreference(Item item) {
        return foodPreferences.getOrDefault(item, DEFAULT_PREFERENCE);
    }

    @Override
    public void setFoodPreference(Item item, float preference) {
        foodPreferences.put(item, preference);
        cleanUp();
    }

    @Override
    public void cleanUp() {
        for (Item item : foodPreferences.keySet() ) {
            if (foodPreferences.get(item) == DEFAULT_PREFERENCE) {
                foodPreferences.remove(item);
            }
        }
    }

    @Override
    public void reduce(Item item) {
        reducePercent(item, PREFERENCE_REDUCE);
    }

    @Override
    public void reduce(Item item, float amount) {
        foodPreferences.put(
                item,
                foodPreferences.getOrDefault(item, DEFAULT_PREFERENCE - amount)
        );
        cleanUp();
    }

    @Override
    public void reducePercent(Item item, float percent) {
        foodPreferences.put(item, foodPreferences.getOrDefault(item, DEFAULT_PREFERENCE * (1 - percent)));
        cleanUp();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        ListTag preferencesList = new ListTag();

        for (Map.Entry<Item, Float> entry : foodPreferences.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(entry.getKey());
            entryTag.putString("item", itemId.toString());
            entryTag.putFloat("preference", entry.getValue());
            preferencesList.add(entryTag);
        }

        nbt.put("preferences", preferencesList);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        foodPreferences.clear();
        ListTag preferencesList = nbt.getList("preferences", Tag.TAG_COMPOUND);

        for (int i = 0; i < preferencesList.size(); i++) {
            CompoundTag entryTag = preferencesList.getCompound(i);
            ResourceLocation itemId = ResourceLocation.tryParse(entryTag.getString("item"));
            if (itemId != null && BuiltInRegistries.ITEM.containsKey(itemId)) {
                Item item = BuiltInRegistries.ITEM.get(itemId);
                if (item != Items.AIR) {
                    foodPreferences.put(item, entryTag.getFloat("preference"));
                }
            }
        }
    }
}
