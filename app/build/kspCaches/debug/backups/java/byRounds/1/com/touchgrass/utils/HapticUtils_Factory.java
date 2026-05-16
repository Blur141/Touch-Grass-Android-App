package com.touchgrass.utils;

import android.os.Vibrator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class HapticUtils_Factory implements Factory<HapticUtils> {
  private final Provider<Vibrator> vibratorProvider;

  private HapticUtils_Factory(Provider<Vibrator> vibratorProvider) {
    this.vibratorProvider = vibratorProvider;
  }

  @Override
  public HapticUtils get() {
    return newInstance(vibratorProvider.get());
  }

  public static HapticUtils_Factory create(Provider<Vibrator> vibratorProvider) {
    return new HapticUtils_Factory(vibratorProvider);
  }

  public static HapticUtils newInstance(Vibrator vibrator) {
    return new HapticUtils(vibrator);
  }
}
