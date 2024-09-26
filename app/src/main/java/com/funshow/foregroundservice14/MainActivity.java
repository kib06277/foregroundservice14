package com.funshow.foregroundservice14;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean allPermissionsGranted = true;
    List<String> permissionsNeeded = new ArrayList<>();
    Button login;
    //14 版以下
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,  // 拍照與錄影
            Manifest.permission.READ_EXTERNAL_STORAGE,  // 存取相簿媒體檔案
            Manifest.permission.RECORD_AUDIO  // 錄音
    };

    //14 版以上權限
    private static final String[] REQUIRED_PERMISSIONS_to14 = {
            Manifest.permission.CAMERA, // 拍照與錄影
            Manifest.permission.READ_MEDIA_IMAGES, //圖片
            Manifest.permission.READ_MEDIA_VIDEO, // 影片
            Manifest.permission.READ_MEDIA_AUDIO, // 讀取語音
            Manifest.permission.RECORD_AUDIO, // 錄音
            Manifest.permission.FOREGROUND_SERVICE_CAMERA , //相機
            Manifest.permission.FOREGROUND_SERVICE_MICROPHONE , //麥克風
            Manifest.permission.FOREGROUND_SERVICE , //前景服務
            Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION , //前景服務媒體投影
            Manifest.permission.POST_NOTIFICATIONS //通知
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            login = findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAndRequestPermissions();
                }
            });
        } catch (Exception e) {
            Log.i("FunshowError" , "LoginActivity connectWebSocket e = " + e);
        }
    }

    private void checkAndRequestPermissions() {
        Log.i("APPLifeCycle" , "MainActivity checkAndRequestPermissions");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 使用 Android 13 以上的新 API 或行為
                if (permissionsNeeded.size() == 0) {
                    for (String permission : REQUIRED_PERMISSIONS_to14) {
                        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                            permissionsNeeded.add(permission);
                        }
                    }
                }
            } else {
                // 使用舊版本的 API 或行為
                if (permissionsNeeded.size() == 0) {
                    for (String permission : REQUIRED_PERMISSIONS) {
                        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                            permissionsNeeded.add(permission);
                        }
                    }
                }
            }

            if (!permissionsNeeded.isEmpty()) {
                showPermissionExplanationDialog(permissionsNeeded);
            } else {
                // 所有權限都已獲得，可以執行需要這些權限的操作
                startMyForegroundService();
            }
        } catch (Exception e) {
            Log.i("FunshowError" , "LoginActivity checkAndRequestPermissions e = " + e);
        }
    }

    private void showPermissionExplanationDialog(final List<String> permissionsNeeded) {
        Log.i("APPLifeCycle" , "MainActivity showPermissionExplanationDialog");
        StringBuilder message = new StringBuilder("我們需要以下權限來提供完整的功能：");
        Log.d("ActivityLog", "permissionsNeeded.size():" + permissionsNeeded.size());
        for (String permission : permissionsNeeded) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("ActivityLog", "permission:" + permission);
                // 使用 Android 13 以上的新 API 或行為
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        message.append("相機權限：用於拍照和錄影\n");
                        break;
                    case Manifest.permission.READ_MEDIA_IMAGES:
                        message.append("媒體存取權限：用於存取圖片檔案\n");
                        break;
                    case Manifest.permission.READ_MEDIA_VIDEO:
                        message.append("媒體存取權限：用於存取影片檔案\n");
                        break;
                    case Manifest.permission.READ_MEDIA_AUDIO:
                        message.append("媒體存取權限：用於存取語音檔案\n");
                        break;
                    case Manifest.permission.RECORD_AUDIO:
                        message.append("錄音權限：用於錄音功能\n");
                        break;
                    case Manifest.permission.FOREGROUND_SERVICE_CAMERA:
                        message.append("錄音權限：用於手機相機功能\n");
                        break;
                    case Manifest.permission.FOREGROUND_SERVICE_MICROPHONE:
                        message.append("錄音權限：用於麥克風功能\n");
                        break;
                    case Manifest.permission.FOREGROUND_SERVICE:
                        message.append("錄音權限：用於前景服務功能\n");
                        break;
                    case Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION:
                        message.append("錄音權限：用於前景服務媒體投影\n");
                        break;
                }
            } else {
                // 使用舊版本的 API 或行為
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        message.append("相機權限：用於拍照和錄影\n");
                        break;
                    case Manifest.permission.READ_EXTERNAL_STORAGE:
                        message.append("存儲權限：用於存取相簿媒體檔案\n");
                        break;
                    case Manifest.permission.RECORD_AUDIO:
                        message.append("錄音權限：用於錄音功能\n");
                        break;
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("權限請求")
                .setMessage(message.toString())
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // 使用 Android 13 以上 的新 API 或行為
                            ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS_to14, PERMISSION_REQUEST_CODE);
                        } else {
                            // 使用舊版本的 API 或行為
                            ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleDeniedPermissions();
                    }
                })
                .create()
                .show();
    }

    private void startMyForegroundService() {
        try {
            Log.i("APPLifeCycle" , "MainActivity startMyForegroundService");
            Intent serviceIntent = new Intent(this, FBHelperService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0 以上必须使用 startForegroundService 来启动前台服务
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        } catch (Exception e) {
            Log.i("FunshowError" , "LoginActivity startMyForegroundService e = " + e);
        }
    }

    private void handleDeniedPermissions() {
        Log.i("APPLifeCycle" , "MainActivity handleDeniedPermissions");
        // 處理被拒絕的權限
        Toast.makeText(this, "部分權限被拒絕，應用程式可能無法正常運行", Toast.LENGTH_LONG).show();
        new AlertDialog.Builder(this)
                .setTitle("權限被拒絕")
                .setMessage("請前往設置手動授予權限，否則應用程式可能無法正常運行。")
                .setPositiveButton("前往設置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , String[] permissions , int[] grantResults) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 使用 Android 13 以上的新 API 或行為
                if (requestCode == PERMISSION_REQUEST_CODE) {
                    for(int i = 0 ; i < grantResults.length ; i++) {
                        Log.i("ActivityLog" , "grantResults[" + i + "] = " + grantResults[i]);
                    }

                    //三種權限皆同意
                    if( (grantResults[0] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[1] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[2] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[3] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[4] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[5] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[6] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[7] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[8] == PackageManager.PERMISSION_GRANTED) &
                            isNotificationPermissionGranted(this) ) {
                        allPermissionsGranted = true;
                    } else {
                        allPermissionsGranted = false;
                    }

                    if (allPermissionsGranted) {
                        // 所有權限都已獲得，可以執行需要這些權限的操作
                        startMyForegroundService();
                    } else {
                        // 有些權限被拒絕
                        handleDeniedPermissions();
                    }
                }
            } else {
                // 使用舊版本的 API 或行為
                if (requestCode == PERMISSION_REQUEST_CODE) {
                    //三種權限皆同意
                    if( (grantResults[0] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[1] == PackageManager.PERMISSION_GRANTED) &
                            (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                        allPermissionsGranted = true;
                    } else {
                        allPermissionsGranted = false;
                    }

                    if (allPermissionsGranted) {
                        // 所有權限都已獲得，可以執行需要這些權限的操作
                        startMyForegroundService();
                    } else {
                        // 有些權限被拒絕
                        handleDeniedPermissions();
                    }
                }
            }
        } catch (Exception e) {
            Log.i("FunshowError" , "LoginActivity onRequestPermissionsResult e = " + e);
        }
    }

    //判斷是否開啟通知
    public boolean isNotificationPermissionGranted(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}