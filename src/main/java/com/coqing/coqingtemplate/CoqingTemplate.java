package com.coqing.coqingtemplate;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import com.coqing.coqingutils.ConfigUtils;
import com.coqing.coqingutils.Utils;
import com.coqing.coqingutils.commands.CommandUtils;

@SuppressWarnings("UnstableApiUsage")
public final class CoqingTemplate extends JavaPlugin {
    private static CoqingTemplate instance;
    @Getter
    private static Utils utils;
    @Getter
    private static Injector injector;

    public static CoqingTemplate get() {
        return instance;
    }

    private void initUtils() {
        utils = Utils.createBuilder()
                .plugin(this)
                .debug(true)
                .build();

        injector = Guice.createInjector(new PluginModule(utils, this));
        utils.setPluginInjector(injector);

        // Init config
        injector.getInstance(ConfigUtils.class).load(this.getDataPath().resolve("config.yml"));

        // Init commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            //noinspection DataFlowIssue
            utils.getUtil(CommandUtils.class).registerCommands(commands.registrar(),
                    "com.coqing.coqingtemplate.commands");
        });
    }

    @Override
    public void onEnable() {
        instance = this;
        initUtils();
        // TODO: Insert other logic here...
    }

    @Override
    public void onDisable() {
        utils = null;
        injector = null;
        instance = null;
    }
}
