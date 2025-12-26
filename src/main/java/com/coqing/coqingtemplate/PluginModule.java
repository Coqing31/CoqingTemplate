package com.coqing.coqingtemplate;

import com.coqing.coqingutils.InjectorModule;
import com.coqing.coqingutils.Utils;
import com.google.inject.AbstractModule;

public class PluginModule extends InjectorModule {
    private final Utils utils;
    private final CoqingTemplate plugin;

    public PluginModule(Utils utils, CoqingTemplate plugin) {
        super(utils);
        this.utils = utils;
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(Utils.class).toInstance(this.utils);
        bind(CoqingTemplate.class).toInstance(this.plugin);
    }

}
