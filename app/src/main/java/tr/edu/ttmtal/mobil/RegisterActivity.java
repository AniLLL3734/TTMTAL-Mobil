package tr.edu.ttmtal.mobil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etName = findViewById(R.id.etRegName);
        EditText etEmail = findViewById(R.id.etRegEmail);
        EditText etPass = findViewById(R.id.etRegPassword);
        EditText etPassConfirm = findViewById(R.id.etRegPasswordConfirm);
        Button btnRegister = findViewById(R.id.btnRegisterSubmit);
        TextView tvGoLogin = findViewById(R.id.tvGoLogin);

        com.google.firebase.auth.FirebaseAuth mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        com.google.firebase.database.DatabaseReference mDatabase = com.google.firebase.database.FirebaseDatabase.getInstance("https://ttmtal-mobil-95160-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String passConfirm = etPassConfirm.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(passConfirm)) {
                Toast.makeText(this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.length() < 6) {
                Toast.makeText(this, "Şifre en az 6 karakter olmalıdır.", Toast.LENGTH_SHORT).show();
                return;
            }

            btnRegister.setEnabled(false);
            btnRegister.setText("Kayıt Yapılıyor...");

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            java.util.HashMap<String, Object> userMap = new java.util.HashMap<>();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("uid", uid);
                            userMap.put("createdAt", com.google.firebase.database.ServerValue.TIMESTAMP);

                            mDatabase.child("users").child(uid).setValue(userMap)
                                    .addOnCompleteListener(dbTask -> {
                                        Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    });
                        } else {
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Hesap Oluştur");
                            Toast.makeText(this, "Hata: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Google Sign In Setup
        com.google.android.gms.auth.api.signin.GoogleSignInOptions gso = new com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btnGoogleRegister).setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 9001);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                com.google.android.gms.auth.api.signin.GoogleSignInAccount account = task.getResult(com.google.android.gms.common.api.ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (com.google.android.gms.common.api.ApiException e) {
                Toast.makeText(this, "Google Kaydı Başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        com.google.firebase.auth.AuthCredential credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null);
        com.google.firebase.auth.FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        com.google.firebase.auth.FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            java.util.HashMap<String, Object> userMap = new java.util.HashMap<>();
                            userMap.put("name", user.getDisplayName());
                            userMap.put("email", user.getEmail());
                            userMap.put("uid", user.getUid());
                            userMap.put("createdAt", com.google.firebase.database.ServerValue.TIMESTAMP);

                            com.google.firebase.database.FirebaseDatabase.getInstance("https://ttmtal-mobil-95160-default-rtdb.europe-west1.firebasedatabase.app")
                                    .getReference("users").child(user.getUid()).updateChildren(userMap);
                        }
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Kimlik Doğrulama Başarısız.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
