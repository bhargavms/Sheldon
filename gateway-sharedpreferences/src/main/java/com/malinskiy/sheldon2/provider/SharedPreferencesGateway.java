package com.malinskiy.sheldon2.provider;

import com.malinskiy.sheldon2.IGateway;

import android.content.SharedPreferences;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


public class SharedPreferencesGateway implements IGateway {

    @Nonnull private final Observable<String> keyChangesObservable;
    @Nonnull private final SharedPreferences preferences;
    @Nullable private SharedPreferences.OnSharedPreferenceChangeListener listener;

    public SharedPreferencesGateway(@Nonnull final SharedPreferences preferences) {
        this.preferences = preferences;

        keyChangesObservable = Observable.using(
                new Callable<SharedPreferences>() {
                    @Override public SharedPreferences call() throws Exception {
                        return preferences;
                    }
                },
                new Function<SharedPreferences, ObservableSource<String>>() {
                    @Override public ObservableSource<String> apply(@NonNull final SharedPreferences sharedPreferences) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override public void subscribe(final ObservableEmitter<String> e) throws Exception {
                                listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                                    @Override
                                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                                        e.onNext(key);
                                    }
                                };
                                sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
                            }
                        });
                    }
                },
                new Consumer<SharedPreferences>() {
                    @Override public void accept(@NonNull SharedPreferences sharedPreferences) throws Exception {
                        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
                        listener = null;
                    }
                }).publish().refCount();
    }

    @Nonnull @Override
    public Observable<Boolean> observeBoolean(@Nonnull final String key,
                                              @Nonnull final Boolean defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Predicate<String>() {
                    @Override public boolean test(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Function<String, Boolean>() {
                    @Override public Boolean apply(String s) {
                        return preferences.getBoolean(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<Float> observeFloat(@Nonnull final String key,
                                                             @Nonnull final Float defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Predicate<String>() {
                    @Override public boolean test(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Function<String, Float>() {
                    @Override public Float apply(String s) {
                        return preferences.getFloat(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<Integer> observeInteger(@Nonnull final String key,
                                                                 @Nonnull final Integer defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Predicate<String>() {
                    @Override public boolean test(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Function<String, Integer>() {
                    @Override public Integer apply(String s) {
                        return preferences.getInt(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<Long> observeLong(@Nonnull final String key,
                                                           @Nonnull final Long defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Predicate<String>() {
                    @Override public boolean test(@NonNull String s) throws Exception {
                        return s.equals(key);
                    }
                })
                .map(new Function<String, Long>() {
                    @Override public Long apply(String s) {
                        return preferences.getLong(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<String> observeString(@Nonnull final String key,
                                                               @Nonnull final String defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Predicate<String>() {
                    @Override public boolean test(@NonNull String s) throws Exception {
                        return s.equals(key);
                    }
                })
                .map(new Function<String, String>() {
                    @Override public String apply(@NonNull String s) throws Exception {
                        return preferences.getString(s, defaultValue);
                    }
                });
    }

    @Nonnull
    @Override
    public Boolean getBoolean(@Nonnull String key, @Nonnull Boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    @Nonnull
    @Override
    public Float getFloat(@Nonnull String key, @Nonnull Float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    @Nonnull
    @Override
    public Integer getInteger(@Nonnull String key, @Nonnull Integer defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Nonnull
    @Override
    public Long getLong(@Nonnull String key, @Nonnull Long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    @Nonnull
    @Override
    public String getString(@Nonnull String key, @Nonnull String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    @Override public void putBoolean(@Nonnull String key, @Nonnull Boolean value) {
        preferences.edit()
                   .putBoolean(key, value)
                   .apply();
    }

    @Override public void putFloat(@Nonnull String key, @Nonnull Float value) {
        preferences.edit()
                   .putFloat(key, value)
                   .apply();
    }

    @Override public void putInteger(@Nonnull String key, @Nonnull Integer value) {
        preferences.edit()
                   .putInt(key, value)
                   .apply();
    }

    @Override public void putLong(@Nonnull String key, @Nonnull Long value) {
        preferences.edit()
                   .putLong(key, value)
                   .apply();
    }

    @Override public void putString(@Nonnull String key, @Nonnull String value) {
        preferences.edit()
                   .putString(key, value)
                   .apply();
    }

    /**
     *
     * @deprecated Don't need to call .contains in separate thread (and thus increase complexity
     * of the program) .contains is quick operation, only the initial getSharedPrefernces call is
     * slow and needs to be off loaded from the UI thread.
     *
     */
    @Deprecated
    @Nonnull @Override public Observable<Boolean> containsAsObservable(@Nonnull final String key) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override public Boolean call() {
                return preferences.contains(key);
            }
        });
    }

    @Nonnull @Override public boolean contains(@Nonnull final String key) {
        return preferences.contains(key);
    }

    @Override public void remove(@Nonnull String key) {
        preferences.edit()
                   .remove(key)
                   .apply();
    }

    @Override
    public void clear() {
        preferences.edit()
                .clear()
                .apply();
    }
}
