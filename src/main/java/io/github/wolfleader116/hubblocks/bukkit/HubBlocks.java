package io.github.wolfleader116.hubblocks.bukkit;

import io.github.wolfleader116.hubblocks.bukkit.commands.AnvilSC;
import io.github.wolfleader116.hubblocks.bukkit.commands.MigrateSC;
import io.github.wolfleader116.hubblocks.bukkit.commands.ReloadSC;
import io.github.wolfleader116.hubblocks.bukkit.commands.ResetSC;
import io.github.wolfleader116.hubblocks.bukkit.events.PlayerInteractEH;
import io.github.wolfleader116.hubblocks.bukkit.tabcompleters.HubBlocksTC;
import io.github.wolfleader116.wolfapi.bukkit.CommandDescription;
import io.github.wolfleader116.wolfapi.bukkit.HelpSC;
import io.github.wolfleader116.wolfapi.bukkit.MainCommand;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import io.github.wolfleader116.wolfapi.bukkit.WolfPlugin;

public class HubBlocks extends WolfPlugin {
	
	public static HubBlocks plugin;
	public static Message message;

	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	
	private static ConfigOptions configOptions;
	
	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		plugin = this;
		configOptions = new ConfigOptions();
		ConfigOptions.updateConfig();
		message = new Message(this);
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		initializeCommands();
		initializeSubCommands();
		initializeListeners();
		initializeTabcompleters();
		setCommandDescriptions();
		configOptions.reloadConfig();
		WolfAPI.registerConfig(configOptions);
	}

	public void onDisable() {
		super.onDisable();
	}
	
	public static ConfigOptions getConfigOptions() {
		return HubBlocks.configOptions;
	}

	private void initializeCommands() {
		this.getCommand("hubblocks").setExecutor(mainCommand);
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
		subCommands.put("anvil", new AnvilSC());
		subCommands.put("migrate", new MigrateSC());
	}
	
	private void initializeListeners() {
		getServer().getPluginManager().registerEvents(new PlayerInteractEH(), this);
	}

	private void initializeTabcompleters() {
		this.getCommand("hubblocks").setTabCompleter(new HubBlocksTC());
	}

	private void setCommandDescriptions() {
		this.commandDescriptions.add(new CommandDescription("hubblocks", "Shows HubBlocks information", ""));
		this.commandDescriptions.add(new CommandDescription("hubblocks help", "Shows HubBlocks commands", ""));
		this.commandDescriptions.add(new CommandDescription("hubblocks reload", "Reloads HubBlocks config", "hubblocks.reload"));
		this.commandDescriptions.add(new CommandDescription("hubblocks reset", "Resets HubBlocks config", "hubblocks.reset"));
		this.commandDescriptions.add(new CommandDescription("hubblocks migrate <old backend> <new backend>", "Migrates data", "hubblocks.migrate", false));
		this.commandDescriptions.add(new CommandDescription("hubblocks anvil add", "Makes an anvil indestructible", "hubblocks.infiniteanvil"));
		this.commandDescriptions.add(new CommandDescription("hubblocks anvil remove", "Makes an anvil destructible", "hubblocks.infiniteanvil"));
	}

}
