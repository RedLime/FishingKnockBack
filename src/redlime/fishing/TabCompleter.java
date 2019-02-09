package redlime.fishing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

public class TabCompleter implements org.bukkit.command.TabCompleter {

	public static Main plugin = Main.plugin;

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fishingknockback")) {
			if (args.length == 1) {
				return Arrays.asList("reload", "on", "off", "hook", "debug", "config", "help");
			}
			if (args[0].equalsIgnoreCase("config")) {
				if (args.length == 2) {
					List<String> con = new ArrayList<>();
					con.add("world");
					con.add("entity");
					return con;
				} else if (args[1].equalsIgnoreCase("entity") && args.length == 3) {
					ArrayList<String> entities = new ArrayList<String>();
					for(EntityType type : EntityType.values()) {
						if(type.isAlive()) {
							entities.add(type.toString());
						}
					}
					
					if(!args[2].isEmpty()) {
						return entities.stream().filter(str -> str.startsWith(args[2])).collect(Collectors.toList());
					} else {
						return entities;
					}
				} else if (args[1].equalsIgnoreCase("world") && args.length == 3) {
					List<String> wor = new ArrayList<>();
					int i;
					for (i = 0; i < Bukkit.getWorlds().size(); i = i + 1) {
						wor.add(Bukkit.getWorlds().get(i).getName());
					}
					return wor;
				}
			}

		}
		return null;
	}
}
