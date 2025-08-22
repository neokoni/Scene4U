package ink.neokoni.scene4u;

import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    public EventListener(Scene4U instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new Configuration().loadData();

        Player player = event.getPlayer();
        String weather = getData(player, "weather");
        String time = getData(player, "time");

        if (weather == null || weather.isEmpty()) {
            new Configuration().saveData(player, "weather", "");
        } else {
            switch (weather) {
                case "CLEAR": player.setPlayerWeather(WeatherType.CLEAR);
                case "DOWNFALL": player.setPlayerWeather(WeatherType.DOWNFALL);
            }
        }

        if (time == null || time.isEmpty()) {
            new Configuration().saveData(player, "time", "");
        } else {
            player.setPlayerTime(Long.parseLong(time), false);
        }


    }

    private String getData(Player player, String key) {
        return new Configuration().getYaml("data.yml")
                .getString(player.getUniqueId()+"."+key);
    }
}
