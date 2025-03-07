package a7.hyperion.data;

import net.minecraft.world.item.component.ResolvableProfile;
import org.apache.commons.lang3.text.WordUtils;

public enum EssenceType {
    WITHER(SkullType.WITHER_ESSENCE);

    public final SkullType skullType;

    EssenceType(SkullType skullType) {
        this.skullType = skullType;
    }

    public ResolvableProfile getProfile() {
        return skullType.getProfile();
    }

    public String getDisplayName() {
        //noinspection deprecation
        return WordUtils.capitalizeFully(this.toString(), '_');
    }
}
