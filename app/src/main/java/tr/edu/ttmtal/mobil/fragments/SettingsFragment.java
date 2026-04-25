package tr.edu.ttmtal.mobil.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import tr.edu.ttmtal.mobil.LoginActivity;
import tr.edu.ttmtal.mobil.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("ttmtal_prefs", 0);
        View layoutLogin = view.findViewById(R.id.layoutLogin);
        View layoutLogout = view.findViewById(R.id.layoutLogout);

        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
            layoutLogin.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.VISIBLE);
        } else {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutLogout.setVisibility(View.GONE);
        }

        layoutLogin.setOnClickListener(v ->
            startActivity(new Intent(requireContext(), LoginActivity.class))
        );

        Switch switchNotif = view.findViewById(R.id.switchNotifications);
        switchNotif.setChecked(prefs.getBoolean("notifications", true));
        switchNotif.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("notifications", isChecked).apply();
            Toast.makeText(requireContext(), isChecked ? "Bildirimler açıldı" : "Bildirimler kapatıldı", Toast.LENGTH_SHORT).show();
        });

        Switch switchDark = view.findViewById(R.id.switchDarkMode);
        switchDark.setChecked(prefs.getBoolean("dark_mode", false));
        switchDark.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            if (getActivity() != null) getActivity().recreate();
        });

        view.findViewById(R.id.layoutPrivacy).setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://turktelekomatl.meb.k12.tr")));
        });

        view.findViewById(R.id.layoutRate).setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + requireContext().getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(requireContext(), "Mağaza bulunamadı.", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.layoutLogout).setOnClickListener(v -> {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireContext(), tr.edu.ttmtal.mobil.WelcomeActivity.class));
            requireActivity().finishAffinity();
        });
    }
}
