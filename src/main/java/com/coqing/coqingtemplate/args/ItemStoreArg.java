package com.coqing.coqingtemplate.args;

import com.coqing.coqingutils.ComponentUtils;
import com.coqing.coqingutils.itemstore.ItemStoreItem;
import com.coqing.coqingutils.itemstore.ItemStoreUtils;
import com.google.inject.Inject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class ItemStoreArg implements CustomArgumentType.Converted<@NotNull ItemStoreItem, @NotNull String> {
    private final ItemStoreUtils itemStore;
    private final DynamicCommandExceptionType NAME_DOESNT_EXIST;
    private final SimpleCommandExceptionType ERROR_WHILE_DESERIALIZING;

    @Inject
    private ItemStoreArg(ComponentUtils cmp, ItemStoreUtils itemStore) {
        this.itemStore = itemStore;
        MessageComponentSerializer msg = MessageComponentSerializer.message();
        this.NAME_DOESNT_EXIST = new DynamicCommandExceptionType(name ->
                msg.serialize(cmp.formatMessage("<prefix> <red><u><name></u> doesn't exist in the ItemStore system.</red>",
                        Placeholder.unparsed("name", name.toString())))
        );
        this.ERROR_WHILE_DESERIALIZING = new SimpleCommandExceptionType(
                msg.serialize(cmp.formatMessage("<prefix> <red>An error occured while deserializing the item. If " +
                        "you are an administrator, check the console.</red>"))
        );
    }

    @Override
    public ItemStoreItem convert(String input) throws CommandSyntaxException {
        if (!this.itemStore.containsKey(input))
            throw NAME_DOESNT_EXIST.create(input);

        try {
            return this.itemStore.get(input);
        } catch (IOException e) {
            throw ERROR_WHILE_DESERIALIZING.create();
        }
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        this.itemStore.keySet().stream()
                .filter(id -> id.startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
