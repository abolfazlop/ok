package dev.autopassx;

import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;

public final class AutoPassXModule extends XposedModule {
    @Override
    public void onModuleLoaded(XposedModuleInterface.ModuleLoadedParam param) {
        log("AutoPass X loaded in process: " + param.getProcessName());
    }

    @Override
    public void onPackageReady(XposedModuleInterface.PackageReadyParam param) {
        // Deliberately no broad hooks in the first scaffold. Hooks will be added
        // behind opt-in compatibility rules and tested against Android 16.
        log("Package ready: " + param.getPackageName());
    }
}
