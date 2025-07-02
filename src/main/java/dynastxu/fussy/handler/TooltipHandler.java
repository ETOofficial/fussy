package dynastxu.fussy.handler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static dynastxu.fussy.Fussy.MODID;
import static dynastxu.fussy.capability.CapabilityRegistration.PLAYER_FOOD_PREFERENCES;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class TooltipHandler {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        FoodProperties foodProperties = event.getItemStack().getFoodProperties(player);
        if (foodProperties == null) return;
        var object = player.getCapability(PLAYER_FOOD_PREFERENCES);
        if (object == null) return;
        float preference = object.getPreference(event.getItemStack().getItem());
        event.getToolTip().add(
                Component.translatable("tooltip_preference", preference * 100)
        );
    }
}
