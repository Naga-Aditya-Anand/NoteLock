package com.example.notessecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    LinearLayout MainLayout;

    TextView main, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainLayout=findViewById(R.id.main_layout);

        main = findViewById(R.id.main);
        description = findViewById(R.id.description);

        BiometricManager biometricManager= BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),"Device doesn't support Biometric",Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),"Biometric Not Working",Toast.LENGTH_SHORT).show();

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(),"Biometric is not enrolled",Toast.LENGTH_SHORT).show();
        }

        Executor executor= ContextCompat.getMainExecutor(this);

        biometricPrompt=new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Biometric Success",Toast.LENGTH_SHORT).show();
                MainLayout.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main.setText("N");
                    }
                },400);
                // After that we will be appending the next
                // letter after some time interval
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main.append("O");
                    }
                },800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main.append("T");
                    }
                },1200);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main.append("E");
                    }
                },1600);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.setText("L");
                    }
                },1800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("O");
                    }
                },2000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("C");
                    }
                },2200);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("K");
                    }
                },2400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append(" ");
                    }
                },2600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("Y");
                    }
                },2800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("O");
                    }
                },3000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("U");
                    }
                },3200);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("R");
                    }
                },3400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append(" ");
                    }
                },3600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("N");
                    }
                },3800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("O");
                    }
                },4000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("T");
                    }
                },4200);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        description.append("E");
                    }
                },4400);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, Sign_In.class);
                        startActivity(intent);
                        finish();
                    }

            },4600);
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("NOTELOCK")
                .setDescription("Use your Fingerprint").setDeviceCredentialAllowed(true).build();

        biometricPrompt.authenticate(promptInfo);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}