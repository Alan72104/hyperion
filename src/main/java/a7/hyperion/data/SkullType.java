package a7.hyperion.data;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.world.item.component.ResolvableProfile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public enum SkullType {
    WITHER_ESSENCE("c4db4adfa9bf48ff5d41707ae34ea78bd2371659fcd8cd8934749af4cce9b");

    public final String textureHash;

    SkullType(String textureHash) {
        this.textureHash = textureHash;
    }

    public ResolvableProfile getProfile() {
        var properties = new PropertyMap();
        var textures = new Property("textures",
                Base64.getEncoder().encodeToString((
                        "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + textureHash + "\"}}}")
                        .getBytes(StandardCharsets.UTF_8)));
        properties.put("textures", textures);
        return new ResolvableProfile(Optional.empty(), Optional.empty(), properties);
    }
}
