package com.coqing.coqingtemplate.commands;

import com.coqing.coqingutils.ComponentUtils;
import com.coqing.coqingutils.ConfigUtils;
import com.coqing.coqingutils.commands.PluginCommand;
import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class TestConfigCommand implements PluginCommand {
    private final ConfigUtils config;
    private final ComponentUtils cmp;

    @Inject
    private TestConfigCommand(ConfigUtils config, ComponentUtils cmp) {
        this.config = config;
        this.cmp = cmp;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("testconfig")
                .executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        cmp.sendMessage(sender, "<prefix> <yellow>All configuration nodes:</yellow>");
        cmp.sendMessage(sender, config.getRootNode().toString());
        return 1;
    }

    @Override
    public String description() {
        return "Tests the configuration for the plugin, and prints all nodes.";
    }
}
