package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

            // 🔥 KHAI BÁO TOP-LEVEL (QUAN TRỌNG)
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(
                            R.id.homeFragment,
                            R.id.cartFragment,
                            R.id.profileFragment
                    ).build();

            // 🔥 CHỈ GỌI 1 LẦN
            bottomNav.setOnItemSelectedListener(item -> {

                int id = item.getItemId();

                if (id == R.id.homeFragment) {
                    navController.popBackStack(R.id.homeFragment, false);
                    return true;
                }

                if (id == R.id.cartFragment) {
                    navController.navigate(R.id.cartFragment);
                    return true;
                }

                if (id == R.id.profileFragment) {
                    navController.navigate(R.id.profileFragment);
                    return true;
                }

                return false;
            });
        }
    }
}