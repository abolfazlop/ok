package dev.autopassx;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public final class MainActivity extends Activity {
    private static final String BITWARDEN_PACKAGE = "com.x8bit.bitwarden";

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
        status.setText("نسخهٔ آزمایشی\n\nاین ماژول به فیلدهای استاندارد اندروید کمک می‌کند تا Bitwarden آن‌ها را بهتر تشخیص دهد. برای اثرگذاری، AutoPass X را در LSPosed فعال و برنامه‌های موردنظر را در Scope انتخاب کن.\n\nBitwarden باید سرویس Autofill پیش‌فرض اندروید باشد. اندروید هنگام درخواست، سرویس Bitwarden را حتی اگر رابط برنامه بسته باشد اجرا می‌کند؛ اگر خزانه قفل باشد، خود Bitwarden احراز هویت می‌خواهد.\n\nرمزها و محتوای واردشده توسط این ماژول خوانده یا ذخیره نمی‌شوند.");
        status.setTextSize(16);
        status.setPadding(0, dp(24), 0, dp(24));
        root.addView(status, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button autofill = new Button(this);
        autofill.setText("تنظیم سرویس Autofill");
        autofill.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE);
                intent.setData(Uri.parse("package:" + BITWARDEN_PACKAGE));
                startActivity(intent);
            } catch (Exception ignored) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        root.addView(autofill, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button openBitwarden = new Button(this);
        openBitwarden.setText("باز کردن Bitwarden / بازکردن قفل خزانه");
        openBitwarden.setOnClickListener(v -> {
            Intent launch = getPackageManager().getLaunchIntentForPackage(BITWARDEN_PACKAGE);
            if (launch != null) {
                startActivity(launch);
            } else {
                Toast.makeText(this, "Bitwarden نصب نیست یا بستهٔ آن پیدا نشد.", Toast.LENGTH_LONG).show();
            }
        });
        root.addView(openBitwarden, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(root);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
