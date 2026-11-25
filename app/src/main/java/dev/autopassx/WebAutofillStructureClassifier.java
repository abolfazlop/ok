package dev.autopassx;

import android.util.Pair;
import android.view.View;
import android.view.ViewStructure;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class WebAutofillStructureClassifier {
    private static final int MAX_DEPTH = 12;
    private static final int MAX_NODES = 400;

    private WebAutofillStructureClassifier() {}

    static void apply(ViewStructure root) {
        if (root == null) return;
        int[] visited = new int[] { 0 };
        visit(root, 0, visited);
    }

    private static void visit(ViewStructure node, int depth, int[] visited) {
        if (node == null || depth > MAX_DEPTH || visited[0]++ >= MAX_NODES) return;

        try {
            inferHtmlField(node);
            int childCount = Math.min(node.getChildCount(), MAX_NODES - visited[0]);
            for (int i = 0; i < childCount; i++) {
                visit(node.getChild(i), depth + 1, visited);
            }
        } catch (Throwable ignored) {
            // A malformed or vendor-specific virtual node must never break the host app.
        }
    }

    private static void inferHtmlField(ViewStructure node) {
        ViewStructure.HtmlInfo html = node.getHtmlInfo();
        if (html == null) return;

        String tag = html.getTag();
        if (tag == null || !(tag.equalsIgnoreCase("input")
                || tag.equalsIgnoreCase("textarea"))) return;

        List<Pair<String, String>> attributes = html.getAttributes();
        if (attributes == null || attributes.isEmpty()) return;

        Map<String, String> values = new HashMap<>();
        for (Pair<String, String> pair : attributes) {
            if (pair != null && pair.first != null && pair.second != null) {
                values.put(pair.first.toLowerCase(Locale.ROOT),
                        pair.second.toLowerCase(Locale.ROOT));
            }
        }

        String autocomplete = values.getOrDefault("autocomplete", "");
        if (autocomplete.contains("off")) return;

        String type = values.getOrDefault("type", "");
        String name = values.getOrDefault("name", "");
        String id = values.getOrDefault("id", "");
        String placeholder = values.getOrDefault("placeholder", "");
        String metadata = name + " " + id + " " + placeholder;

        String autofillHint = null;
        if (autocomplete.contains("current-password") || type.equals("password")
                || containsAny(metadata, "password", "passwd", "passcode")) {
            autofillHint = View.AUTOFILL_HINT_PASSWORD;
        } else if (autocomplete.contains("new-password")) {
            autofillHint = View.AUTOFILL_HINT_NEW_PASSWORD;
        } else if (autocomplete.contains("username")
                || containsAny(metadata, "username", "user_name", "login")) {
            autofillHint = View.AUTOFILL_HINT_USERNAME;
        } else if (autocomplete.contains("email") || type.equals("email")
                || containsAny(metadata, "email", "e-mail")) {
            autofillHint = View.AUTOFILL_HINT_EMAIL_ADDRESS;
        }

        if (autofillHint != null) {
            node.setAutofillHints(new String[] { autofillHint });
        }
    }

    private static boolean containsAny(String value, String... needles) {
        for (String needle : needles) if (value.contains(needle)) return true;
        return false;
    }
}
