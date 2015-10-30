/*
 * Copyright 2015 Makoto Yamazaki <makoto1975@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.realm_file_viewer;

import android.app.Application;

import com.example.realm_file_viewer.prngfix.PRNGFixes;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication self;

    public static MyApplication getInstance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        PRNGFixes.apply();

        removeAllRealmFiles();
        // copy files from assets folder to /data/data/<applicationId>/files/
        for (String realmFilename : getRealmFileList()) {
            copyToFilesDir(realmFilename);
        }

        initStetho();
    }

    private List<String> getRealmFileList() {
        try {
            final String[] fileList = getAssets().list("");
            final ArrayList<String> realmFileList = new ArrayList<>();

            for (String file : fileList) {
                if (file.endsWith(".realm")) {
                    realmFileList.add(file);
                }
            }
            return realmFileList;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
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

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                                .withMetaTables()
                                .withDescendingOrder()
                                .withLimit(1000)
                                .build())
                        .build());
    }
}
