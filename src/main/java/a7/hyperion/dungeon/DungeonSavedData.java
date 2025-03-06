package a7.hyperion.dungeon;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class DungeonSavedData extends SavedData {
    public static final SavedData.Factory<DungeonSavedData> FACTORY = new SavedData.Factory<>(DungeonSavedData::new, DungeonSavedData::load);
    public static DungeonSavedData create() {
        return new DungeonSavedData();
    }

    public static DungeonSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        DungeonSavedData data = DungeonSavedData.create();
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return tag;
    }

    public void foo() {
        this.setDirty();
    }
}