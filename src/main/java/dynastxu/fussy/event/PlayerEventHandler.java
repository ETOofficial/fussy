package dynastxu.fussy.event;

import dynastxu.fussy.Config;
import dynastxu.fussy.attachment.AttachmentRegistry;
import dynastxu.fussy.network.SyncFoodPreferencesPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

import static dynastxu.fussy.Fussy.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerEventHandler {

    // 玩家登录时同步
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    }

    // 玩家重生时同步
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    }

    // 跨维度时同步
    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
    }

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        ItemStack itemStack = event.getItem();
        Item item = itemStack.getItem();
        FoodProperties foodProp = itemStack.getFoodProperties(serverPlayer);
        if (foodProp == null) return;

        // 获取物品的键
        String itemKey = BuiltInRegistries.ITEM.getKey(item).toString();
        // 获取玩家的食物偏好
        Map<String, Float> foodPreference = serverPlayer.getData(AttachmentRegistry.FOOD_PREFERENCES);
        // 获取物品的偏好
        float preference = foodPreference.getOrDefault(
                itemKey,
                Config.DEFAULT_PREFERENCE_PERCENT.get() / 100f // 默认偏好百分比
        );
        // 设置物品的偏好
        foodPreference.put(
                itemKey,
                preference * (1 - Config.PREFERENCE_REDUCE_PERCENT.get() / 100f) // 减少偏好
        );

        serverPlayer.setData(AttachmentRegistry.FOOD_PREFERENCES, foodPreference);
        syncFoodPreferences(serverPlayer);
    }

    public static void syncFoodPreferences(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(
                serverPlayer,
                new SyncFoodPreferencesPayload(
                        serverPlayer.getData(AttachmentRegistry.FOOD_PREFERENCES)
                )
        );
    }
}
