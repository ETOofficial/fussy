package dynastxu.fussy.network;

import dynastxu.fussy.attachment.AttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {
    public static void handleSyncFoodPreferences(SyncFoodPreferencesPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            player.setData(AttachmentRegistry.FOOD_PREFERENCES, payload.foodPreferencesMap());
        });
    }
}
