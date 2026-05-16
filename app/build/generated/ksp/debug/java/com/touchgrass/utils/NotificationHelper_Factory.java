package com.touchgrass.utils;

import android.app.NotificationManager;
import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NotificationHelper_Factory implements Factory<NotificationHelper> {
  private final Provider<Context> contextProvider;

  private final Provider<NotificationManager> notificationManagerProvider;

  private NotificationHelper_Factory(Provider<Context> contextProvider,
      Provider<NotificationManager> notificationManagerProvider) {
    this.contextProvider = contextProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  @Override
  public NotificationHelper get() {
    return newInstance(contextProvider.get(), notificationManagerProvider.get());
  }

  public static NotificationHelper_Factory create(Provider<Context> contextProvider,
      Provider<NotificationManager> notificationManagerProvider) {
    return new NotificationHelper_Factory(contextProvider, notificationManagerProvider);
  }

  public static NotificationHelper newInstance(Context context,
      NotificationManager notificationManager) {
    return new NotificationHelper(context, notificationManager);
  }
}
