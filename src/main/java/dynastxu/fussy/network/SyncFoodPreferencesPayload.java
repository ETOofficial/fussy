package dynastxu.fussy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.HashMap;

import static dynastxu.fussy.Fussy.MODID;

public record SyncFoodPreferencesPayload(Map<String, Float> foodPreferencesMap) implements CustomPacketPayload {
    public static final Type<SyncFoodPreferencesPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "sync_food_preferences"));

    public static final StreamCodec<ByteBuf, SyncFoodPreferencesPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new, // Map 工厂
                    ByteBufCodecs.STRING_UTF8, // 键的编解码器 (String)
                    ByteBufCodecs.FLOAT // 值的编解码器 (Float)
            ),
            SyncFoodPreferencesPayload::foodPreferencesMap, // 从 payload 中提取数据
            SyncFoodPreferencesPayload::new // 构造函数
    );

    @Override
    public @Nonnull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
