package ink.neokoni.env4u;

import org.bukkit.plugin.java.JavaPlugin;

public final class Env4U extends JavaPlugin {
    private static Env4U instance;

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

    public static Env4U getInstance() {
        return instance;
    }
}
