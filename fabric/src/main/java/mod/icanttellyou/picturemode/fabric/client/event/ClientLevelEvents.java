package mod.icanttellyou.picturemode.fabric.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class ClientLevelEvents {
    /**
     * An event which is called after the client level has been changed.
     */
    public static final Event<AfterClientLevelChange> AFTER_CLIENT_LEVEL_CHANGE =
        EventFactory.createArrayBacked(AfterClientLevelChange.class, callbacks -> (client, level) -> {
            for (AfterClientLevelChange callback : callbacks) {
                callback.afterLevelChange(client, level);
            }
        });

    /**
     * An event which is called after the client level has been unloaded.
     */
    public static final Event<AfterClientLevelUnload> AFTER_CLIENT_LEVEL_UNLOAD =
        EventFactory.createArrayBacked(AfterClientLevelUnload.class, callbacks -> client -> {
            for (AfterClientLevelUnload callback : callbacks) {
                callback.afterLevelUnload(client);
            }
        });

    private ClientLevelEvents() {
    }

    @FunctionalInterface
    public interface AfterClientLevelChange {
        /**
         * Called after the client level has been changed.
         *
         * @param client The {@link Minecraft} instance
         * @param level  The new {@link ClientLevel} instance
         */
        void afterLevelChange(Minecraft client, ClientLevel level);
    }

    @FunctionalInterface
    public interface AfterClientLevelUnload {
        /**
         * Called after the client level has been unloaded.
         *
         * @param client The {@link Minecraft} instance
         */
        void afterLevelUnload(Minecraft client);
    }
}
