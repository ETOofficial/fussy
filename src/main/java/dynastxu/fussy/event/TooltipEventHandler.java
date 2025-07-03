package dynastxu.fussy.event;

import dynastxu.fussy.Config;
import dynastxu.fussy.attachment.AttachmentRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static dynastxu.fussy.Fussy.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class TooltipEventHandler {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        Player player = event.getEntity();
        if (player == null) return;
        FoodProperties foodProperties = event.getItemStack().getFoodProperties(player);
        if (foodProperties == null) return;

        float preference = player.getData(AttachmentRegistry.FOOD_PREFERENCES).getOrDefault(
                BuiltInRegistries.ITEM.getKey(item).toString(),
                Config.DEFAULT_PREFERENCE_PERCENT.get() / 100f
        );
        event.getToolTip().add(
                Component.translatable("tooltip_preference", preference * 100)
        );
    }
}
