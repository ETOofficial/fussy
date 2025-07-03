package dynastxu.fussy.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static dynastxu.fussy.Fussy.MODID;

@EventBusSubscriber(modid = MODID)
public class NetworkHandler {
    @SubscribeEvent
    public static void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SyncFoodPreferencesPayload.TYPE,
                SyncFoodPreferencesPayload.STREAM_CODEC,
                ClientPacketHandler::handleSyncFoodPreferences
        );
    }
}
