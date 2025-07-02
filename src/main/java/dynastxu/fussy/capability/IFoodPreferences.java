package dynastxu.fussy.capability;

import net.minecraft.world.item.Item;

import java.util.Map;

public interface IFoodPreferences {
    public Map<Item, Float> getFoodPreferences();

    public float getPreference(Item item);

    public void setFoodPreference(Item item, float preference);

    /**
     * 清除所有偏好等于默认值的内容
     */
    public void cleanUp();

    public void reduce(Item item);

    public void reduce(Item item, float amount);

    public void reducePercent(Item item, float percent);
}
