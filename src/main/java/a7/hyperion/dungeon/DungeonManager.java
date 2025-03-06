package a7.hyperion.dungeon;

import net.minecraft.server.level.ServerLevel;

public class DungeonManager {
    public void generateDungeon() {

    }

    public DungeonSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(DungeonSavedData.FACTORY, "dungeon");
    }
}
