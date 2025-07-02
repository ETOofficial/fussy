package dynastxu.fussy.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static dynastxu.fussy.Fussy.MODID;

@EventBusSubscriber(modid = MODID)
public class CapabilityRegistration {
    public static final EntityCapability<IFoodPreferences, Void> PLAYER_FOOD_PREFERENCES =
            EntityCapability.createVoid(
                    ResourceLocation.fromNamespaceAndPath(MODID, "player_food_preferences"),
                    IFoodPreferences.class
            );

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                PLAYER_FOOD_PREFERENCES,
                EntityType.PLAYER,
                (player, context) -> new PlayerFoodPreferences()
        );
    }
}
