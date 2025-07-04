package dynastxu.fussy.attachment;


import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static dynastxu.fussy.Fussy.MODID;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<Map<String, Float>>> FOOD_PREFERENCES =
            ATTACHMENT_TYPES.register(
                    "food_preferences",
                    // TODO 死亡是否重置配置设置实现
                    () -> AttachmentType.<Map<String, Float>>builder(() -> new HashMap<>()) // 默认空Map
                            .serialize(Codec.unboundedMap(Codec.STRING, Codec.FLOAT)) // Map序列化编解码器
                            .copyOnDeath() // 启用死亡自动复制
                            .build()
            );
}
