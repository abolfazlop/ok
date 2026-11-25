package dev.autopassx;

import android.util.Log;
import android.view.ViewStructure;
import android.widget.TextView;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;
import java.lang.reflect.Method;

public final class AutoPassXModule extends XposedModule {
    private static final String TAG = "AutoPassX";

    @Override
    public void onModuleLoaded(XposedModuleInterface.ModuleLoadedParam param) {
        log(Log.INFO, TAG, "Loaded with LibXposed API " + getApiVersion());
    }

    @Override
    public void onPackageReady(XposedModuleInterface.PackageReadyParam param) {
        String packageName = param.getPackageName();
        if (packageName == null || packageName.equals("dev.autopassx")
                || packageName.equals("android") || packageName.equals("system")) {
            return;
        }
        try {
            Class<?> viewClass = Class.forName("android.view.View", false, param.getClassLoader());
            Method target = viewClass.getDeclaredMethod(
                    "onProvideAutofillStructure", ViewStructure.class, int.class);
            hook(target).setId("autofill-hints-" + packageName).intercept(chain -> {
                Object result = chain.proceed();
                Object receiver = chain.getThisObject();
                Object structure = chain.getArg(0);
                if (receiver instanceof TextView && structure instanceof ViewStructure) {
                    AutofillHintClassifier.apply((TextView) receiver, (ViewStructure) structure);
                }
                return result;
            });
            log(Log.INFO, TAG, "Installed Autofill hint hook for " + packageName);
        } catch (Throwable t) {
            log(Log.WARN, TAG, "Could not install Autofill hint hook for " + packageName, t);
        }
    }
}
