package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 🔥 BẮT BUỘC
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // KHÔNG setContentView

        new Handler().postDelayed(() -> {

            startActivity(new android.content.Intent(this, MainActivity.class));
            finish();

        }, 1500); // 🔥 1.5 giây
    }
}