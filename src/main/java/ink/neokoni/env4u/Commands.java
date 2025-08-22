package ink.neokoni.env4u;

import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor {
    public Commands(Env4U instance) {
        instance.getCommand("env4u").setExecutor(this);
        instance.getCommand("env4u").setTabCompleter((sender, command, string, args) -> {
            List<String> subCommands = new ArrayList<>();
            List<String> values = new ArrayList<>();
            subCommands.add("clear");
            subCommands.add("set");
            subCommands.add("help");

            values.add("clear");
            values.add("downfall");
            values.add("day");
            values.add("midnight");
            values.add("night");
            values.add("noon");

            if (args.length <= 1) {
                if (sender.hasPermission("env4u.reload")) {
                    subCommands.add("reload");
                    return subCommands;
                }
                subCommands.remove("reload");
                return subCommands;
            }

            if (args.length == 2 && args[0].equals("set")) {
                return values;
            }
            return Collections.emptyList();
        });
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            if (!commandSender.hasPermission("env4u.help")) {
                noPermission(commandSender);
                return true;
            }
            help(commandSender);
            return true;
        }

        if (strings[0].equals("help")) {
            if (!commandSender.hasPermission("env4u.help")) {
                noPermission(commandSender);
                return true;
            }
            help(commandSender);
            return true;
        }

        if (strings[0].equals("reload")) {
            if (!commandSender.hasPermission("env4u.reload")) {
                noPermission(commandSender);
                return true;
            }
            new Configuration().loadAll();
            commandSender.sendMessage(
                    new Configuration().getYaml("message.yml").getString("done"));
            return true;
        }

        if (!isPlayer(commandSender)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (strings[0].equals("clear")) {
            if (!player.hasPermission("env4u.clear")) {
                noPermission(player);
                return true;
            }

            player.resetPlayerTime();
            player.resetPlayerWeather();
            new Configuration().saveData(player, "time", "");
            new Configuration().saveData(player, "weather", "");

            player.sendMessage(
                    new Configuration().getYaml("message.yml").getString("done"));
        }

        if (strings[0].equals("set")) {
            if (!player.hasPermission("env4u.set")) {
                noPermission(player);
                return true;
            }

            if (strings.length >= 2) {
                String env = strings[1];
                if (env.equals("clear")) {
                    setWeather(player, WeatherType.CLEAR);
                } else if (env.equals("downfall")) {
                    setWeather(player, WeatherType.DOWNFALL);
                } else if (env.equals("day")) {
                    setTime(player, 1000L);
                } else if (env.equals("midnight")) {
                    setTime(player, 18000L);
                } else if (env.equals("night")) {
                    setTime(player, 13000L);
                } else if (env.equals("noon")) {
                    setTime(player, 6000L);
                } else {
                    long time;
                    try {
                        time = Long.parseLong(env);
                        setTime(player, time);
                    } catch (NumberFormatException e) {
                        player.sendMessage(
                                new Configuration().getYaml("message.yml").getString("error_value"));
                    }
                }

                return true;
            }
        }

        if (!commandSender.hasPermission("env4u.help")) {
            noPermission(commandSender);
            return true;
        }

        return true;
    }

    private boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage(
                    new Configuration().getYaml("message.yml").getString("only_player"));
            return false;
        }
    }

    private void noPermission(CommandSender sender) {
        sender.sendMessage(
                new Configuration().getYaml("message.yml").getString("no_permission"));
    }

    private void help(CommandSender sender) {
        sender.sendMessage("/env4u clear " +
                new Configuration().getYaml("message.yml").getString("help_clear"));
        sender.sendMessage("/env4u set <clear|downfall|day|midnight|night|noon|gametick> " +
                new Configuration().getYaml("message.yml").getString("help_set"));
        sender.sendMessage("/env4u help " +
                new Configuration().getYaml("message.yml").getString("help_main"));
        if (sender.hasPermission("env4u.reload")) {
            sender.sendMessage("/env4u reload " +
                    new Configuration().getYaml("message.yml").getString("help_reload"));
        }
    }

    private void setTime(Player player, Long time) {
        player.setPlayerTime(time, false);
        new Configuration().saveData(player, "time", String.valueOf(time));
        player.sendMessage(
                new Configuration().getYaml("message.yml").getString("done"));
    }

    private void setWeather(Player player, WeatherType weather) {
        player.setPlayerWeather(weather);
        new Configuration().saveData(player, "weather", weather.name());
        player.sendMessage(
                new Configuration().getYaml("message.yml").getString("done"));
    }
}
