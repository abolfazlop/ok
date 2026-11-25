package dev.autopassx;

import android.text.InputType;
import android.view.View;
import android.view.ViewStructure;
import android.widget.TextView;
import java.util.Locale;

final class AutofillHintClassifier {
    private AutofillHintClassifier() {}

    static void apply(TextView view, ViewStructure structure) {
        if (view == null || structure == null || view.getKeyListener() == null) return;

        int importance = view.getImportantForAutofill();
        if (importance == View.IMPORTANT_FOR_AUTOFILL_NO
                || importance == View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS) return;

        String[] existing = structure.getAutofillHints();
        if (existing != null && existing.length > 0) return;

        String inferred = inferHint(view);
        if (inferred != null) structure.setAutofillHints(inferred);
    }

    private static String inferHint(TextView view) {
        int inputType = view.getInputType();
        int inputClass = inputType & InputType.TYPE_MASK_CLASS;
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        String label = view.getHint() == null ? ""
                : view.getHint().toString().toLowerCase(Locale.ROOT);

        if (inputClass == InputType.TYPE_CLASS_TEXT
                && (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD
                || variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                || variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
            return View.AUTOFILL_HINT_PASSWORD;
        }
        if (inputClass == InputType.TYPE_CLASS_NUMBER
                && variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
            return View.AUTOFILL_HINT_PASSWORD;
        }
        if (inputClass == InputType.TYPE_CLASS_TEXT
                && variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
            return View.AUTOFILL_HINT_EMAIL_ADDRESS;
        }
        if (containsAny(label, "password", "passcode", "رمز عبور", "گذرواژه")) {
            return View.AUTOFILL_HINT_PASSWORD;
        }
        if (containsAny(label, "email", "e-mail", "mail address", "ایمیل")) {
            return View.AUTOFILL_HINT_EMAIL_ADDRESS;
        }
        if (containsAny(label, "username", "user name", "user id", "login",
                "account", "نام کاربری", "شناسه کاربری")) {
            return View.AUTOFILL_HINT_USERNAME;
        }
        return null;
    }

    private static boolean containsAny(String value, String... needles) {
        for (String needle : needles) if (value.contains(needle)) return true;
        return false;
    }
}
