package ink.neokoni.scene4u;

import org.bukkit.plugin.java.JavaPlugin;

public final class Scene4U extends JavaPlugin {
    private static Scene4U instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        new Configuration().loadAll();

        new Commands(this);
        new EventListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Scene4U getInstance() {
        return instance;
    }
}
