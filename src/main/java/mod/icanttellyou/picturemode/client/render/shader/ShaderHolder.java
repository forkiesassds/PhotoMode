package mod.icanttellyou.picturemode.client.render.shader;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * A holder for a shader.
 *
 * @param id The ID of the shader to hold.
 */
public record ShaderHolder(@Nullable Identifier id) {
    public static final ShaderHolder EMPTY = new ShaderHolder();

    public ShaderHolder() {
        this(null);
    }

    /**
     * Gets the location of the shader to use when setting the shader.
     *
     * @return The {@link Identifier} of the shader path.
     */
    public Identifier getLocation() {
        //? if >=1.21.2 {
        return id;
        //? } else {
        /*return id != null ? id.withPrefix("post_effect/").withSuffix(".json") : null;
        *///? }
    }

    /**
     * Gets the translated name of the shader.
     *
     * @return The translated name of the shader.
     */
    public Component getTranslatedName() {
        return Component.translatable(id != null ? id.toLanguageKey("shader") : "gui.picturemode.none");
    }
}
