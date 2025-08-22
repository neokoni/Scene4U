package ink.neokoni.env4u;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private static YamlConfiguration MESSAGES;
    private static YamlConfiguration PLAYER_DATA;

    private boolean isExist(String FileName) {
        return new File(Env4U.getInstance().getDataFolder(), FileName).exists();
    }

    private YamlConfiguration getFile(String FileName) {
        if (!isExist(FileName)) {
            Env4U.getInstance().saveResource(FileName, true);
        }

        File file = new File(Env4U.getInstance().getDataFolder(), FileName);
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

        if (yml.getKeys(false).isEmpty()) {
            Env4U.getInstance().saveResource(FileName, true);
            file = new File(Env4U.getInstance().getDataFolder(), FileName);
            yml = YamlConfiguration.loadConfiguration(file);
        }
        return yml;
    }

    public void loadAll() {
        MESSAGES = getFile("message.yml");
        PLAYER_DATA = getFile("data.yml");
    }

    public void loadData() {
        PLAYER_DATA = getFile("data.yml");
    }

    public void loadMessage() {
        MESSAGES = getFile("message.yml");
    }

    public YamlConfiguration getYaml(String FileName) {
        switch (FileName) {
            case "message.yml": return MESSAGES;
            case "data.yml": return PLAYER_DATA;
            default: return null;
        }
    }

    public void saveData(Player player, String key, String value) {
        key = player.getUniqueId() + "." + key;
        PLAYER_DATA.set(key, value);

        try {
            PLAYER_DATA.save(new File(Env4U.getInstance().getDataFolder(), "data.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
