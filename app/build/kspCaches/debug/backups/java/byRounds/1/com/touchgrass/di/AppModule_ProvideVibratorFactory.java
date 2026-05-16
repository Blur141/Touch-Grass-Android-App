package com.touchgrass.di;

import android.content.Context;
import android.os.Vibrator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideVibratorFactory implements Factory<Vibrator> {
  private final Provider<Context> contextProvider;

  private AppModule_ProvideVibratorFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public Vibrator get() {
    return provideVibrator(contextProvider.get());
  }

  public static AppModule_ProvideVibratorFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideVibratorFactory(contextProvider);
  }

  public static Vibrator provideVibrator(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideVibrator(context));
  }
}
