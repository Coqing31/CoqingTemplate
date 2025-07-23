package com.coqing.coqingtemplate;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import com.coqing.coqingutils.ConfigUtils;
import com.coqing.coqingutils.Utils;
import com.coqing.coqingutils.commands.CommandUtils;
import com.coqing.coqingutils.plugin.CoqingUtils;

public class CoqingTemplate extends JavaPlugin {
    private static CoqingTemplate instance;
    private static Utils utils;
    private static Injector injector;

    public Utils getUtils() {
        return utils;
    }

    public Injector getGuiceInjector() {
        return injector;
    }

    public static CoqingTemplate get() {
        return instance;
    }

    private void initUtils() {
        utils = Utils.createBuilder()
                .plugin(this)
                .debug(true)
                .packetEvents(CoqingUtils.getPacketEventsAPI())
                .build();

        injector = Guice.createInjector(utils.getGuiceModule());
        utils.setPluginInjector(injector);

        // Init config
        injector.getInstance(ConfigUtils.class).load(this.getDataPath().resolve("config.yml"));

        // Init commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
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
