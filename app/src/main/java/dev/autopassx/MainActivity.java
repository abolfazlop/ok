package dev.autopassx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(28), dp(24), dp(24));
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("AutoPass X");
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView status = new TextView(this);
        status.setText("نسخهٔ آزمایشی اولیه\n\nاین برنامه هنوز قابلیت تزریق و اصلاح فرم‌ها را فعال نکرده است. ابتدا Bitwarden را به‌عنوان سرویس Autofill اندروید انتخاب کنید و سپس ماژول را در LSPosed فعال کنید.\n\nقفل خزانه و تأیید بیومتریک Bitwarden دست‌نخورده باقی می‌ماند.");
        status.setTextSize(16);
        status.setPadding(0, dp(24), 0, dp(24));
        root.addView(status, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button autofill = new Button(this);
        autofill.setText("تنظیم سرویس Autofill");
        autofill.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE));
            } catch (Exception ignored) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        root.addView(autofill, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(root);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
