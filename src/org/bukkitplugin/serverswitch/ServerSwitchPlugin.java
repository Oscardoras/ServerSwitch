package org.bukkitplugin.serverswitch;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkitutils.BukkitPlugin;
import org.bukkitutils.command.v1_14_3_V1.Argument;
import org.bukkitutils.command.v1_14_3_V1.CommandRegister;
import org.bukkitutils.command.v1_14_3_V1.CommandRegister.CommandExecutorType;
import org.bukkitutils.command.v1_14_3_V1.arguments.EntitySelectorArgument;
import org.bukkitutils.command.v1_14_3_V1.arguments.EntitySelectorArgument.EntitySelector;
import org.bukkitutils.command.v1_14_3_V1.arguments.StringArgument;
import org.bukkitutils.io.TranslatableMessage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerSwitchPlugin extends BukkitPlugin {
	
	public static ServerSwitchPlugin plugin;
	
	public ServerSwitchPlugin() {
		plugin = this;
	}
	
	
	@Override
	public void onLoad() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("targets", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
		arguments.put("server", new StringArgument());
		CommandRegister.register("switch", arguments, new Permission("serverswitch.command.switch"), CommandExecutorType.ALL, (cmd) -> {
			@SuppressWarnings("unchecked")
			Collection<Entity> targets = (Collection<Entity>) cmd.getArg(0);
			for (Entity entity : targets) {
				Player player = (Player) entity;
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF((String) cmd.getArg(1));
				player.sendPluginMessage(ServerSwitchPlugin.plugin, "BungeeCord", out.toByteArray());
				cmd.broadcastMessage(new TranslatableMessage(ServerSwitchPlugin.plugin, "command.switch", (String) cmd.getArg(1)));
			}
			return targets.size();
		});
	}
	
	@Override
	public void onEnable() {
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
}
