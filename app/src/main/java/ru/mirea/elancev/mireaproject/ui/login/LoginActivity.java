package ru.mirea.elancev.mireaproject.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.elancev.mireaproject.MainActivity;
import ru.mirea.elancev.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    // Пакетное имя для программы удаленного доступа (AnyDesk)
    private static final String[] TARGET_APP_PACKAGE = {
            "com.anydesk.anydeskandroid",
            "com.microsoft.rdc.androidx",
            "com.teamviewer.quicksupport.market",
            "com.google.chromeremotedesktop",
            "com.sand.airdroid",
            "com.sand.airmirror",
            "com.realvnc.viewer.android",
            "com.microsoft.appmanager"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверяем наличие программы удаленного доступа
        if (isTargetAppInstalled(TARGET_APP_PACKAGE)) {
            showTargetAppWarning();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private boolean validateForm() {
        return true;
    }

    private boolean isTargetAppInstalled(String[] targetAppPackages) {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0);

        for (ApplicationInfo appInfo : installedApps) {
            String packageName = appInfo.packageName;
            for (String targetAppPackage : targetAppPackages) {
                if (packageName.equals(targetAppPackage)) {
                    return true;
                }
            }
        }

        return false;
    }


    private void showTargetAppWarning() {
        new AlertDialog.Builder(this)
                .setTitle("Предупреждение")
                .setMessage("Обнаружена программа удаленного доступа (AnyDesk). Вы должны удалить ее, чтобы продолжить использование этого приложения.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрываем приложение
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private boolean createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return false;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("SUCCESS");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return true;
    }

    private boolean signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return false;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("SUCCESS");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return true;
    }

    public void clickLog(View v) {
        String email = binding.username.getText().toString();
        String password = binding.password.getText().toString();

        if (signIn(email, password) || createAccount(email, password)) {
            // Действия после успешной авторизации
        }
    }
}