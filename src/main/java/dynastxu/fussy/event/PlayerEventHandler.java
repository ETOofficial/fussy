package dynastxu.fussy.event;

import dynastxu.fussy.Config;
import dynastxu.fussy.attachment.AttachmentRegistry;
import dynastxu.fussy.network.SyncFoodPreferencesPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

import static dynastxu.fussy.Fussy.MODID;
import static dynastxu.fussy.utils.FussyUtils.cleanUp;

@EventBusSubscriber(modid = MODID)
public class PlayerEventHandler {

    // 玩家登录时同步
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        syncFoodPreferences(serverPlayer);
    }

    // 玩家重生时同步
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        syncFoodPreferences(serverPlayer);
    }

    // 跨维度时同步
    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        syncFoodPreferences(serverPlayer);
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
        Map<String, Float> foodPreferences = new HashMap<>(serverPlayer.getData(AttachmentRegistry.FOOD_PREFERENCES));

        // 增加偏好
        for (String key : foodPreferences.keySet()) {
            if (key.equals(itemKey)) continue;
            float raiseAmount = (float) (foodPreferences.get(key) + Config.PREFERENCE_RAISE_AMOUNT.getAsDouble());
            if (raiseAmount > Config.DEFAULT_PREFERENCE_PERCENT.getAsInt() / 100f) raiseAmount = Config.DEFAULT_PREFERENCE_PERCENT.getAsInt() / 100f;
            foodPreferences.put(key, raiseAmount);
        }

        // 获取物品的偏好
        float preference = foodPreferences.getOrDefault(
                itemKey,
                Config.DEFAULT_PREFERENCE_PERCENT.getAsInt() / 100f // 默认偏好百分比
        );

        if (preference < 0.6f) {
            // 给予玩家恶心效果
            serverPlayer.addEffect(new MobEffectInstance(
                    MobEffects.CONFUSION,  // 恶心效果
                    (int) (200 * (1 - preference / 0.6f) + 100),
                    0                      // 效果等级
            ));

            if (preference >= 0.3f) {
                serverPlayer.sendSystemMessage(Component.translatable("eatLikeShit"));
            }
        }

        if (preference < 0.3f) {
            // 扣除玩家饱食度
            int exhaustion = foodProp.nutrition(); // 饱食度
            float saturation = foodProp.saturation(); // 饱和度

            float total = (exhaustion + 4 * saturation) * (1.5f * (1 - preference / 0.3f));

            serverPlayer.addEffect(new MobEffectInstance(
                    MobEffects.HUNGER,
                    100,
                    (int) (total * 2)
            ));

            serverPlayer.sendSystemMessage(Component.translatable("spitUp"));
        }

        // 设置物品的偏好
        foodPreferences.put(
                itemKey,
                preference * (1 - Config.PREFERENCE_REDUCE_PERCENT.getAsInt() / 100f) // 减少偏好
        );

        // 清理无用的偏好
        Map<String, Float> foodPreferenceClean = cleanUp(foodPreferences);

        serverPlayer.setData(AttachmentRegistry.FOOD_PREFERENCES, foodPreferenceClean);
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
