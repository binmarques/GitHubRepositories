package br.com.binmarques.githubrepositories.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

/**
 * Created By Daniel Marques on 24/07/2018
 */

public class ActivityUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager manager, @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void updateAndroidSecurityProvider(@NonNull Activity activity) {
        try {
            ProviderInstaller.installIfNeeded(activity);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .showErrorDialogFragment(e.getConnectionStatusCode(), activity,
                            null, 0, null);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (currentNetworkInfo != null && currentNetworkInfo.isAvailable()) {
                return currentNetworkInfo.isConnected();
            }
        }

        return false;
    }
}
