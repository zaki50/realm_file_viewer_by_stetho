package com.example.realm_template;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.realm_template.prngfix.PRNGFixes;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyApplication extends Application {
    private static MyApplication self;

    public static MyApplication getInstance() {
        return self;
    }

    @VisibleForTesting
    public ApplicationComponent component;
    private RefWatcher refWatcher;

    public ApplicationComponent getComponent() {
        return component;
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        PRNGFixes.apply();

        component = DaggerApplicationComponent.create();
        refWatcher = LeakCanary.install(this);

        // assetにもっているデータを展開
        removeAllRealmFiles();
        for (String filename : getRealmFileList()) {
            copyToFilesDir(filename);
        }
    }

    private String[] getRealmFileList() {
        try {
            return getAssets().list("");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private void removeAllRealmFiles() {
        for (File f : getFilesDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        })) {
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        }
    }

    private void copyToFilesDir(String fileName) {
        try {
            final byte[] buffer = new byte[1024];
            final InputStream is;
            is = getAssets().open(fileName);
            final FileOutputStream os = new FileOutputStream(new File(getFilesDir(), fileName));
            int read;
            while (0 < (read = is.read(buffer))) {
                os.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
