package com.coqing.coqingtemplate.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import com.coqing.coqingutils.ComponentUtils;
import com.coqing.coqingutils.commands.CommandUtils;
import com.coqing.coqingutils.commands.PluginCommand;

public class SomeCommand implements PluginCommand {
    private final CommandUtils command;
    private final ComponentUtils component;

    @Inject
    private SomeCommand(CommandUtils command, ComponentUtils component) {
        this.command = command;
        this.component = component;
    }

    @Override
    public String description() {
        return "Some command probably.";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("somecommand").executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> ctx) {
        component.sendMessage(
                command.getExecutorOrSender(ctx),
                "<prefix> <green>SomeCommand should work!</green>");
        return 1;
    }
}
