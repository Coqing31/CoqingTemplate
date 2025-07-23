package com.coqing.coqingtemplate.commands.somecommand;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import com.coqing.coqingutils.ComponentUtils;
import com.coqing.coqingutils.commands.CommandUtils;
import com.coqing.coqingutils.commands.PluginCommand;

public class SomeCommandSomeSub implements PluginCommand {
    private final CommandUtils command;
    private final ComponentUtils component;

    @Inject
    private SomeCommandSomeSub(CommandUtils command, ComponentUtils component) {
        this.command = command;
        this.component = component;
    }

    @Override
    public String description() {
        return "Some sub command for some command.";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("somesub").executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> ctx) {
        component.sendMessage(
                command.getExecutorOrSender(ctx),
                "<prefix> <green>Ran /somecommand somesub.</green>");
        return 1;
    }
}
