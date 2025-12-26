package com.coqing.coqingtemplate.commands;

import com.coqing.coqingtemplate.CoqingTemplate;
import com.coqing.coqingtemplate.args.ItemStoreArg;
import com.coqing.coqingutils.ComponentUtils;
import com.coqing.coqingutils.commands.CommandUtils;
import com.coqing.coqingutils.commands.PluginCommand;
import com.coqing.coqingutils.itemstore.ItemStoreItem;
import com.coqing.coqingutils.itemstore.ItemStoreUtils;
import com.google.inject.Inject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public class ItemStoreCommand implements PluginCommand {
    private final CommandUtils command;
    private final ComponentUtils cmp;
    private final ItemStoreUtils itemStore;
    private final ItemStoreArg isArg;
    private final CoqingTemplate plugin;

    @Inject
    private ItemStoreCommand(CommandUtils command, ComponentUtils cmp, ItemStoreUtils itemStore, ItemStoreArg isArg,
                             CoqingTemplate plugin) {
        this.command = command;
        this.cmp = cmp;
        this.itemStore = itemStore;
        this.isArg = isArg;
        this.plugin = plugin;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("is")
                .requires(req ->
                        req.getExecutor() instanceof Player &&
                                req.getExecutor().hasPermission("coqingtemplate.itemstore"))
                .then(Commands.literal("add")
                        .then(Commands.argument("id", StringArgumentType.string()).executes(this::executeAdd))
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("id", isArg).executes(this::executeRemove))
                )
                .then(Commands.literal("get")
                        .then(Commands.argument("id", isArg).executes(this::executeGet))
                );
    }

    private int executeAdd(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) command.getExecutorOrSender(ctx);
        String id = StringArgumentType.getString(ctx, "id");
        if (this.itemStore.containsKey(id)) {
            cmp.sendMessage(sender, "<prefix> <red>Item with ID <u><id></u> already exists.</red>",
                    Placeholder.unparsed("id", id));
            return 0;
        }

        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            cmp.sendMessage(sender, "<prefix> <red>You must hold an item.</red>");
            return 0;
        }

        try {
            this.itemStore.set(id, item);
            this.itemStore.compact();
            cmp.sendMessage(sender, "<prefix> <green>Successfully added item with ID <u><id></u> to item " +
                    "store.</green>",
                    Placeholder.unparsed("id", id));
        } catch (IOException e) {
            cmp.sendMessage(sender, "<prefix> <red>An error occurred while adding the item to the item store. " +
                    "Check console for details.</red>");

            this.plugin.getSLF4JLogger().error("An error occurred while {} tried to add item with ID {}:",
                    sender.getName(), id, e);
            return 0;
        }

        return 1;
    }

    private int executeRemove(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) command.getExecutorOrSender(ctx);
        ItemStoreItem item = ctx.getArgument("id", ItemStoreItem.class);

        try {
            this.itemStore.remove(item.getId());
            this.itemStore.compact();
            cmp.sendMessage(sender, "<prefix> <green>Successfully removed item with ID <u><id></u> from item " +
                            "store.</green>",
                    Placeholder.unparsed("id", item.getId()));
        } catch (IOException e) {
            cmp.sendMessage(sender, "<prefix> <red>An error occurred while removing the item from the item store. " +
                    "Check console for details.</red>");

            this.plugin.getSLF4JLogger().error("An error occurred while {} tried to remove item with ID {}:",
                    sender.getName(), item.getId(), e);
            return 0;
        }

        return 1;
    }

    private int executeGet(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) command.getExecutorOrSender(ctx);
        ItemStoreItem item = ctx.getArgument("id", ItemStoreItem.class);

        ItemStack stack = item.getItem();
        sender.getInventory().addItem(stack);
        cmp.sendMessage(sender, "<prefix> <green>Successfully gave item with ID <u><id></u> from item " +
                        "store.</green>",
                Placeholder.unparsed("id", item.getId()));

        return 1;
    }
    @Override
    public String description() {
        return "";
    }
}
