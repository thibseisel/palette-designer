package com.github.thibseisel.palettedesigner.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
abstract class AppModule {

    @Binds @Singleton
    abstract Context bindsApplicationContext(@NonNull Application app);

    @Provides @Singleton
    static SharedPreferences provideSharedPreferences(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}