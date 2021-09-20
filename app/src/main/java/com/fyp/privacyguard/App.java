package com.fyp.privacyguard;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.fyp.privacyguard.data.Repository;
import com.fyp.privacyguard.receiver.BootCompletedIntentReceiver;
import com.fyp.privacyguard.service.PhoneLogService;
import com.fyp.privacyguard.shared.RepositoryViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import com.fyp.privacyguard.AppConst;
import com.fyp.privacyguard.BuildConfig;
import com.fyp.privacyguard.data.AppItem;
import com.fyp.privacyguard.data.DataManager;
import com.fyp.privacyguard.db.DbHistoryExecutor;
import com.fyp.privacyguard.db.DbIgnoreExecutor;
import com.fyp.privacyguard.service.AppService;
import com.fyp.privacyguard.util.CrashHandler;
import com.fyp.privacyguard.util.PreferenceManager;

public class App extends Application {

    public RepositoryViewModelFactory vmRepositoryFactory;
    public Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        // App Usage Handling
        PreferenceManager.init(this);
        getApplicationContext().startService(new Intent(getApplicationContext(), AppService.class));
        DbIgnoreExecutor.init(getApplicationContext());
        DbHistoryExecutor.init(getApplicationContext());
        DataManager.init();
        addDefaultIgnoreAppsToDB();
        if (AppConst.CRASH_TO_FILE) CrashHandler.getInstance().init();

        // Core shared repositories
        repository = new Repository();
        vmRepositoryFactory = new RepositoryViewModelFactory(this, repository);

        // Log Service
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(new BootCompletedIntentReceiver(), filter);

        startService(new Intent(this, PhoneLogService.class));
    }

    private void addDefaultIgnoreAppsToDB() {
        new Thread(() -> {
            List<String> mDefaults = new ArrayList<>();
            mDefaults.add("com.android.settings");
            mDefaults.add(BuildConfig.APPLICATION_ID);
            for (String packageName : mDefaults) {
                AppItem item = new AppItem();
                item.mPackageName = packageName;
                item.mEventTime = System.currentTimeMillis();
                DbIgnoreExecutor.getInstance().insertItem(item);
            }
        }).start();
    }
}
