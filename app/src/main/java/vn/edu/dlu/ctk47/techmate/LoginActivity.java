package vn.edu.dlu.ctk47.techmate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSubmit;
    TextView txtTitle, txtSwitchMode;

    // Biến để theo dõi xem đang ở chế độ nào
    String currentMode = "LOGIN";
    public static final String PREFS_NAME = "TechMatePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Enable Edge-to-Edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ View
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnLoginMain);
        txtTitle = findViewById(R.id.txtLoginWith);
        // Tìm TextView ở dưới cùng (Bạn cần đảm bảo trong XML có ID này)
        txtSwitchMode = findViewById(R.id.txtSwitchAuthMode);

        // Nhận chế độ ban đầu từ DetailFragment gửi sang
        currentMode = getIntent().getStringExtra("AUTH_MODE");
        if (currentMode == null) currentMode = "LOGIN";

        updateUIByMode();

        // 1. XỬ LÝ CHUYỂN ĐỔI QUA LẠI (Đăng nhập <-> Đăng ký)
        txtSwitchMode.setOnClickListener(v -> {
            if (currentMode.equals("LOGIN")) {
                currentMode = "REGISTER";
            } else {
                currentMode = "LOGIN";
            }
            updateUIByMode();
        });

        // 2. XỬ LÝ KHI BẤM NÚT CHÍNH (Xác nhận)
        btnSubmit.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (currentMode.equals("REGISTER")) {
                // --- THỰC HIỆN ĐĂNG KÝ ---
                editor.putString("PHONE_" + phone, password);
                editor.putBoolean("IS_LOGGED_IN", true);
                editor.apply();

                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại trang trước

            } else {
                // --- THỰC HIỆN ĐĂNG NHẬP ---
                // Tài khoản test nhanh
                if (phone.equals("0987654321") && password.equals("123456")) {
                    editor.putBoolean("IS_LOGGED_IN", true).apply();
                    Toast.makeText(this, "Admin đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                String savedPassword = sharedPreferences.getString("PHONE_" + phone, null);
                if (savedPassword != null && savedPassword.equals(password)) {
                    editor.putBoolean("IS_LOGGED_IN", true).apply();
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    // Hàm cập nhật chữ trên màn hình theo chế độ
    private void updateUIByMode() {
        if (currentMode.equals("REGISTER")) {
            txtTitle.setText("Đăng ký tài khoản");
            btnSubmit.setText("Đăng ký ngay");
            txtSwitchMode.setText("Đã có tài khoản? Đăng nhập ngay");
        } else {
            txtTitle.setText("Đăng nhập với");
            btnSubmit.setText("Đăng nhập");
            txtSwitchMode.setText("Bạn chưa có tài khoản? Đăng ký ngay");
        }
    }
}