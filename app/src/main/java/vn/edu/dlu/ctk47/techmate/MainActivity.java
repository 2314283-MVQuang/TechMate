package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 🔥 Splash init
        SplashScreen splash = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔥 Giữ splash 1.5s (chuẩn, không block UI)
        final long startTime = System.currentTimeMillis();
        splash.setKeepOnScreenCondition(() ->
                System.currentTimeMillis() - startTime < 2000
        );

        // 🔥 Navigation setup
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) return;

        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            int current = navController.getCurrentDestination().getId();

            // 🔥 Tránh navigate trùng → crash
            if (current == id) return true;

            try {
                navController.navigate(id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        });
    }
}