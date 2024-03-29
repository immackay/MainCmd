package plugin.maincmd.cmds;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import plugin.maincmd.MainCmd;

public class AdminCommands implements CommandExecutor {
	Logger log = Logger.getLogger("Minecraft");
	FileConfiguration config = MainCmd.plugin.getConfig();
	private void kick(CommandSender s, Command c, String l, String[] args) {
		if (args.length < 2) {
			Player t = Bukkit.getPlayer(args[0]);
			String tname = t.getName();
			if (s instanceof Player) {
				if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.kick")) {
					t.kickPlayer("You were kicked.");
					((Player)s).sendMessage(ChatColor.GREEN + tname + " successfully kicked!");
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + tname + " was kicked.");
					log.info("[MainCmd] " + ((Player)s).getName() + " kicked " + tname);
				}
				else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
			}
			else {
				t.kickPlayer("You were kicked.");
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + tname + " was kicked.");
			}
		}
		else {
			StringBuilder msg = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				msg.append(args[i] + " ");
			}
			Player t = Bukkit.getPlayer(args[0]);
			String tname = t.getName();
			if (s instanceof Player) {
				if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.kick")) {
					t.kickPlayer(msg.toString());
					((Player)s).sendMessage(ChatColor.GREEN + tname + " successfully kicked!");
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + tname + " was kicked for " + msg.toString());
					log.info("[MainCmd] " + ((Player)s).getName() + " succesfully used the command /" + l.toString() + " " + tname + " " + msg.toString());
				}
				else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
			}
			else {
				t.kickPlayer(msg.toString());
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + tname + " was kicked for " + msg.toString());
			}
		}
	}
	private void kickAll(CommandSender s, Command c, String l, String[] args) {
		if (s instanceof Player) {
			if (args.length < 2) {
				((Player)s).sendMessage(ChatColor.RED + "Kicking everyone requires a message.");
			}
			else {
				StringBuilder msg = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					msg.append(args[i] + " ");
				}
				if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.kick.all")) {
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						if (!p.isOp() || !MainCmd.plugin.permsCheck(p, "MainCmd.admin.kick.all.safe")) {
							p.kickPlayer(msg.toString());
						}
						else {
							p.sendMessage(ChatColor.AQUA + "All non-ops have been kicked");
						}
					}
				}
				else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
			}
		}
		else {
			if (args.length < 2) {
				s.sendMessage("Kicking everyone requires a message.");
			}
			else {
				StringBuilder msg = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					msg.append(args[i] + " ");
				}
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						if (!p.isOp() || !MainCmd.plugin.permsCheck(p, "MainCmd.admin.kick.all.safe")) {
							p.kickPlayer(msg.toString());
						} else {
							p.sendMessage(ChatColor.AQUA + "All non-ops have been kicked");
						}
					}
			}
		}
	}
	private void ban(CommandSender s, Command c, String l, String[] args) {
		if (args.length < 2) {
			if (s instanceof Player) {
				((Player)s).sendMessage(ChatColor.RED + "You must have a ban message!");
			}
			else {
				s.sendMessage("You must have a ban message!");
			}
		}
		else {
			Player t = Bukkit.getPlayerExact(args[0]);
			StringBuilder msg = new StringBuilder();
			for (int i = 1; i < 1; i++) {
				msg.append(args[i]);
			}
			if (s instanceof Player) {
				if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.ban")) {
					t.kickPlayer(msg.toString());
					t.setBanned(true);
					config.set("Players." + t.getName() + ".banreason", msg.toString());
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + t.getName() + " was banned for " + msg.toString());
					log.info("[MainCmd] " + ((Player)s).getName() + " banned " + t.getName());
					((Player)s).sendMessage(ChatColor.GREEN + t.getName() + " successfully banned!");

				}
				else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
			}
			else {
				t.kickPlayer(msg.toString());
				t.setBanned(true);
				config.set("Players." + t.getName() + ".banreason", msg.toString());
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + t.getName() + " was banned for " + msg.toString());
				s.sendMessage(t.getName() + " successfully banned!");
			}
		}
	}
	private void unban(CommandSender s, Command c, String l, String[] args) {
		Player t = Bukkit.getPlayerExact(args[0]);
		if (s instanceof Player) {
			if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.unban")) {
				if (t.isBanned()) {
					t.setBanned(false);
					log.info("[MainCmd] " + ((Player)s).getName() + " unbanned " + t.getName());
					((Player)s).sendMessage(ChatColor.GREEN + t.getName() + " unbanned!");
				}
				else { ((Player)s).sendMessage(ChatColor.RED + "That player isn't banned!"); }
			}
			else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
		}
		else {
			if (t.isBanned()) {
				t.setBanned(false);
				s.sendMessage(t.getName() + " unbanned!");
			}
			else { s.sendMessage("That player isn't banned!"); }
		}
	}
	private void mute(CommandSender s, Command c, String l, String[] args) {
		if (s instanceof Player) {
			if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.mute")) {
				Player t = Bukkit.getServer().getPlayer(args[0]);
				if (config.getBoolean("Players." + t.getName() + ".muted")) {
					((Player)s).sendMessage(ChatColor.RED + "That player is already muted");
				}
				else {
					config.set("Players." + t.getName() + ".muted", true);
					t.sendMessage(ChatColor.AQUA + "You have been muted.");
					((Player)s).sendMessage(ChatColor.GREEN + t.getName() + " has been muted.");
					
				}
			}
			else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
		}
		else {
			Player t = Bukkit.getServer().getPlayer(args[0]);
			if (config.getBoolean("Players." + t.getName() + ".muted")) {
				s.sendMessage("That player is already muted");
			}
			else {
				config.set("Players." + t.getName() + ".muted", true);
				t.sendMessage(ChatColor.AQUA + "You have been muted.");
				s.sendMessage(t.getName() + " has been muted.");
			}
		}
	}
	private void unmute(CommandSender s, Command c, String l, String[] args) {
		if (s instanceof Player) {
			if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.unmute")) {
				Player t = Bukkit.getServer().getPlayer(args[0]);
				if (!(config.getBoolean("Players." + t.getName() + ".muted"))) {
					((Player)s).sendMessage(ChatColor.RED + "That player isn't muted");
				}
				else {
					config.set("Players." + t.getName() + ".muted", false);
					t.sendMessage(ChatColor.AQUA + "You have been unmuted.");
					((Player)s).sendMessage(ChatColor.GREEN + t.getName() + " has been unmuted.");
					
				}
			}
			else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
		}
		else {
			Player t = Bukkit.getServer().getPlayer(args[0]);
			if (config.getBoolean("Players." + t.getName() + ".muted")) {
				s.sendMessage("That player isn't muted");
			}
			else {
				config.set("Players." + t.getName() + ".muted", false);
				t.sendMessage(ChatColor.AQUA + "You have been unmuted.");
				s.sendMessage(t.getName() + " has been unmuted.");
			}
		}
	}
	private void freeze(CommandSender s, Command c, String l, String[] args) { // TODO in future
		
	}
	private void unfreeze(CommandSender s, Command c, String l, String[] args) { // TODO in future
		
	}
	private void adminMode(CommandSender s, Command c, String l, String[] args) { 
		if (s instanceof Player) {
			if (MainCmd.plugin.permsCheck(((Player)s), "MainCmd.admin.admin")) {
				if (config.getBoolean("Admin_Mode")) {
					Bukkit.setWhitelist(false);
					config.set("Admin_Mode", false);
				}
				else {
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						if (MainCmd.plugin.areYouSure) {
							if (!(p.isOp() || MainCmd.plugin.permsCheck(p, "MainCmd.admin.kick.all.safe"))) {
								p.kickPlayer("Admin mode");
							}
							else {
								Bukkit.getWhitelistedPlayers().add(p);
							}
							for (OfflinePlayer pl : Bukkit.getServer().getOfflinePlayers()) {
								if (pl.isOp() || MainCmd.plugin.permsCheck((Player) pl, "MainCmd.admin.kick.all.safe")) {
									Bukkit.getWhitelistedPlayers().add(pl);
								}
							}
							Bukkit.setWhitelist(true);
							config.set("Admin_Mode", true);
							Bukkit.broadcastMessage(ChatColor.GOLD + "Admin mode in effect.");
						} 
					}
				}
			}
			else { ((Player)s).sendMessage(ChatColor.RED + config.getString("Messages.MissingPermissions")); } 
		}
		else {
			if (config.getBoolean("Admin_Mode")) {
				Bukkit.setWhitelist(false);
				config.set("Admin_Mode", false);
			}
			else {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (!(p.isOp() || MainCmd.plugin.permsCheck(p, "MainCmd.admin.kick.all.safe"))) {
						p.kickPlayer("Admin mode");
					}
					else {
						Bukkit.getWhitelistedPlayers().add(p);
					}
					for (OfflinePlayer pl : Bukkit.getServer().getOfflinePlayers()) {
						if (pl.isOp() || MainCmd.plugin.permsCheck((Player) pl, "MainCmd.admin.kick.all.safe")) {
							Bukkit.getWhitelistedPlayers().add(pl);
						}
					}
					Bukkit.setWhitelist(true);
					config.set("Admin_Mode", true);
					Bukkit.broadcastMessage(ChatColor.GOLD + "Admin mode in effect.");
				}
			}
		}
	}
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (l.equalsIgnoreCase("kick")) { 
			if (args[0].equalsIgnoreCase("-a")) {
				kickAll(s, c, l, args);
			}
			else {
				kick(s, c, l, args);
			}
		}
		if (l.equalsIgnoreCase("ban")) {
			ban(s, c, l, args);
		}
		if (l.equalsIgnoreCase("unban")) {
			unban(s, c, l, args);
		}
		if (l.equalsIgnoreCase("admin")) {
			adminMode(s, c, l, args);
		}
		if (l.equalsIgnoreCase("mute")) {
			mute(s, c, l, args);
		}
		if (l.equalsIgnoreCase("unmute")) {
			unmute(s, c, l, args);
		}
		return false;
	}
}