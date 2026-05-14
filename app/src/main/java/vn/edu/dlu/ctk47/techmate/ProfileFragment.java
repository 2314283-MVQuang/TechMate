package vn.edu.dlu.ctk47.techmate;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwitchCompat switchDarkMode = view.findViewById(R.id.switchDarkMode);
        SwitchCompat switchNotif = view.findViewById(R.id.switchNotif);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        // 1. Xử lý Chế độ tối (Dark Mode)
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // 2. Xử lý Nút Thông báo
        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String msg = isChecked ? "Đã bật thông báo" : "Đã tắt thông báo";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // 3. Xử lý Nút Đăng Xuất
        btnLogout.setOnClickListener(v -> {
            // Xóa trạng thái đăng nhập
            SharedPreferences prefs = requireActivity().getSharedPreferences("TechMatePrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("IS_LOGGED_IN", false);
            editor.apply();

            Toast.makeText(getContext(), "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển hướng người dùng về lại Trang Chủ
            NavController nav = Navigation.findNavController(v);
            nav.popBackStack(R.id.homeFragment, false);
        });
    }
}
