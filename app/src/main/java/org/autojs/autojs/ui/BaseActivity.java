package org.autojs.autojs.ui;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.stardust.app.GlobalAppContext;

import org.autojs.autojs.Pref;
import org.autojs.autoxjs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Stardust on 2017/1/23.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected static final int PERMISSION_REQUEST_CODE = 11186;
    private boolean mShouldApplyDayNightModeForOptionsMenu = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void applyDayNightMode() {
        GlobalAppContext.post(() -> {
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    protected boolean checkPermission(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] requestPermissions = getRequestPermissions(permissions);
            if (requestPermissions.length > 0) {
                requestPermissions(requestPermissions, PERMISSION_REQUEST_CODE);
                return false;
            }
            return true;
        } else {
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, PERMISSION_GRANTED);
            onRequestPermissionsResult(PERMISSION_REQUEST_CODE, permissions, grantResults);
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private String[] getRequestPermissions(String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) == PERMISSION_DENIED) {
                list.add(permission);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void setToolbarAsBack(String title) {
        setToolbarAsBack(this, R.id.toolbar, title);
    }

    public static void setToolbarAsBack(final AppCompatActivity activity, int id, String title) {
        Toolbar toolbar = activity.findViewById(id);
        toolbar.setTitle(title);
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            toolbar.setNavigationOnClickListener(v -> activity.finish());
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mShouldApplyDayNightModeForOptionsMenu) {
            for (int i = 0; i < menu.size(); i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(ContextCompat.getColor(this, R.color.toolbar), PorterDuff.Mode.SRC_ATOP);
                }
            }
            mShouldApplyDayNightModeForOptionsMenu = false;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}
