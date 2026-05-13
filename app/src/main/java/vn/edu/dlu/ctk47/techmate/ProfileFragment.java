package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.dlu.ctk47.techmate.firebase.AuthRepository;

public class ProfileFragment extends Fragment {

    private LinearLayout layoutUnlogged, layoutLogged, btnLogout, btnAccountSettings;
    private ImageView imgAvatar;
    private TextView txtUserName, txtUserEmail;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        layoutUnlogged = view.findViewById(R.id.layoutUnlogged);
        layoutLogged = view.findViewById(R.id.layoutLogged);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAccountSettings = view.findViewById(R.id.btnAccountSettings);

        // Chuyển sang màn hình Đăng nhập
        if (layoutUnlogged != null) {
            layoutUnlogged.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
            });
        }

        // Chuyển sang màn hình Cài đặt tài khoản
        if (btnAccountSettings != null) {
            btnAccountSettings.setOnClickListener(v -> {
                if (AuthRepository.INSTANCE.getCurrentUser() != null) {
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_settingsFragment);
                } else {
                    Toast.makeText(getContext(), "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Đăng xuất
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                AuthRepository.INSTANCE.logout();
                updateUI();
                Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            });
        }

        updateUI();
    }

    private void updateUI() {
        FirebaseUser currentUser = AuthRepository.INSTANCE.getCurrentUser();

        if (currentUser != null) {
            if (layoutUnlogged != null) layoutUnlogged.setVisibility(View.GONE);
            if (layoutLogged != null) layoutLogged.setVisibility(View.VISIBLE);
            if (btnLogout != null) btnLogout.setVisibility(View.VISIBLE);

            AuthRepository.INSTANCE.getUserProfile(currentUser.getUid(), user -> {
                if (user != null && isAdded()) {
                    if (txtUserName != null) txtUserName.setText(user.getName());
                    if (txtUserEmail != null) txtUserEmail.setText(user.getEmail());

                    if (imgAvatar != null) {
                        Glide.with(this)
                                .load(user.getAvatar())
                                .placeholder(R.drawable.logo)
                                .circleCrop()
                                .into(imgAvatar);
                    }
                }
                return null;
            });
        } else {
            if (layoutUnlogged != null) layoutUnlogged.setVisibility(View.VISIBLE);
            if (layoutLogged != null) layoutLogged.setVisibility(View.GONE);
            if (btnLogout != null) btnLogout.setVisibility(View.GONE);
        }
    }
}
