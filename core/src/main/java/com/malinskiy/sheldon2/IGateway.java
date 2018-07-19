package com.malinskiy.sheldon2;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface IGateway {

    @Nonnull Observable<Boolean> observeBoolean(@Nonnull String key, @Nonnull Boolean defaultValue);

    @Nonnull Observable<Float> observeFloat(@Nonnull String key, @Nonnull Float defaultValue);

    @Nonnull Observable<Integer> observeInteger(@Nonnull String key, @Nonnull Integer defaultValue);

    @Nonnull Observable<Long> observeLong(@Nonnull String key, @Nonnull Long defaultValue);

    @Nonnull Observable<String> observeString(@Nonnull String key, @Nonnull String defaultValue);

    @Nonnull Boolean getBoolean(@Nonnull String key, @Nonnull Boolean defaultValue);

    @Nonnull Float getFloat(@Nonnull String key, @Nonnull Float defaultValue);

    @Nonnull Integer getInteger(@Nonnull String key, @Nonnull Integer defaultValue);

    @Nonnull Long getLong(@Nonnull String key, @Nonnull Long defaultValue);

    @Nonnull String getString(@Nonnull String key, @Nonnull String defaultValue);

    void putBoolean(@Nonnull String key, @Nonnull Boolean value);

    void putFloat(@Nonnull String key, @Nonnull Float value);

    void putInteger(@Nonnull String key, @Nonnull Integer value);

    void putLong(@Nonnull String key, @Nonnull Long value);

    void putString(@Nonnull String key, @Nonnull String value);

    @Deprecated
    @Nonnull Observable<Boolean> containsAsObservable(@Nonnull String key);

    boolean contains(@Nonnull String key);

    Completable putBooleanSync(@Nonnull String key, @Nonnull Boolean value);

    Completable putFloatSync(@Nonnull String key, @Nonnull Float value);

    Completable putIntegerSync(@Nonnull String key, @Nonnull Integer value);

    Completable putLongSync(@Nonnull String key, @Nonnull Long value);

    Completable putStringSync(@Nonnull String key, @Nonnull String value);

    void remove(@Nonnull String key);

    void clear();
}
