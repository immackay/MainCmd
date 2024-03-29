package plugin.maincmd.cmds;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import plugin.maincmd.MainCmd;

public class TestCommands
	implements CommandExecutor
{
	Logger log = Logger.getLogger("Minecraft");
	Configuration config = MainCmd.plugin.getConfig();
	private void boom(CommandSender s, Command c, String l, String[] args) { if ((s instanceof Player)) {
			if (args.length < 1) {
				if (MainCmd.plugin.permsCheck((Player)s, "MainCmd.test.boom")) {
					Location tblock = ((Player)s).getTargetBlock(null, 0).getLocation();
					((Player)s).getWorld().createExplosion(tblock, 5.0F);
					((Player)s).sendMessage(ChatColor.GREEN + "Boom!");
					log.info("[MainCmd] " + ((Player)s).getName() + " succesfully used the command /" + l.toString());
				}
				else {
					((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions"));
				}
			}
			else {
				Player t = Bukkit.getServer().getPlayer(args[0]);
				if (MainCmd.plugin.permsCheck((Player)s, "MainCmd.test.boom.others")) {
					if (t != null) {
						t.getWorld().createExplosion(t.getLocation(), 5.0F);
						((Player)s).sendMessage(ChatColor.GREEN + "Boom!");
						t.sendMessage(ChatColor.AQUA + ((Player)s).getName() + " exploded you!");
						log.info("[MainCmd] " + ((Player)s).getName() + " succesfully used the command /" + l.toString() + " " + t.getName());
					}
					else {
						((Player)s).sendMessage(ChatColor.RED + "That player may be offline. Please check your spelling.");
					}
				}
				else {
					((Player)s).sendMessage(config.getString(ChatColor.RED + "Messages.MissingPermissions"));
				}
			}

		}
		else if (args.length < 1) {
			s.sendMessage("Usage: /boom [player]");
		}
		else {
			Player t = Bukkit.getServer().getPlayer(args[0]);
			if (t != null) {
				t.getWorld().createExplosion(t.getLocation(), 5.0F);
				t.sendMessage(ChatColor.AQUA + "You exploded!");
				s.sendMessage("Boom!");
			}
			else {
				s.sendMessage("That player may be offline. Please check your spelling.");
			}
		} }

	private void ping(CommandSender s, Command c, String l, String[] args)
	{
		if ((s instanceof Player)) {
			if (MainCmd.plugin.permsCheck((Player)s, "MainCmd.test.ping")) {
				((Player)s).sendMessage(ChatColor.RED + "Pong!");
				log.info("[MainCmd] " + ((Player)s).getName() + " succesfully used the command /" + l.toString());
			}
			else {
				((Player)s).sendMessage(config.getString(ChatColor.RED + "Messages.MissingPermissions"));
			}
		}
		else
			s.sendMessage("Pong!");
	}

	private void say(CommandSender s, Command c, String l, String[] args) {
		if (args.length < 1) {
			if (s instanceof Player) {
				((Player)s).sendMessage(ChatColor.RED + "You must specify a name and message!");
			} 
			else { s.sendMessage("You must specify a name and message!"); }
		}
		if (args.length < 2) {
			if (s instanceof Player) {
				((Player)s).sendMessage(ChatColor.RED + "You must specify a message!");
			}
			else { s.sendMessage("You must specify a message!"); }
		}
		if (s instanceof Player) {
			if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.test.say")) {
				String name = ChatColor.GOLD + "[" + args[0] + "]" + ChatColor.WHITE;
				StringBuilder msg = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					msg.append(args[i] + " ");
				}
				Bukkit.broadcastMessage(name + " " +  MainCmd.plugin.replaceColors(msg.toString()));
			}
			else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); }
		}
		else {
			String name = ChatColor.GOLD + "[" + args[0] + "]" + ChatColor.WHITE;
			StringBuilder msg = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				msg.append(args[i] + " ");
			}
			Bukkit.broadcastMessage(name + " " +  MainCmd.plugin.replaceColors(msg.toString()));
		}
	}
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if ((s instanceof Player)) {
			log.info("[MainCmd] User " + ((Player)s).getName() + " used (or attempted to use) the command " + l.toString());
		}
		if (l.equalsIgnoreCase("boom")) {
			boom(s, c, l, args);
		}
		if (l.equalsIgnoreCase("ping")) {
			ping(s, c, l, args);
		}
		if (l.equalsIgnoreCase("say")) {
			say(s, c, l, args);
		}
		return false;
	}
}