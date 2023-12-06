package com.stardust.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import androidx.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Stardust on 2017/4/5.
 */

public class DeveloperUtils {

    private static final String PACKAGE_NAME = "org.autojs.autojs";
    private static final String LOG_TAG = "DeveloperUtils";
    private static final ExecutorService sExecutor = UnderuseExecutors.getExecutor();
    private static final String SALT = "let\nlife\nbe\nbeautiful\nlike\nsummer\nflowers\nand\ndeath\nlike\nautumn\nleaves\n.";

    public static boolean isSelfPackage(@Nullable String runningPackage) {
        return PACKAGE_NAME.equals(runningPackage);
    }

    private static PackageInfo getPackageInfo(Context context, String packageName, int flags) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static String selfPackage() {
        return PACKAGE_NAME;
    }

}
