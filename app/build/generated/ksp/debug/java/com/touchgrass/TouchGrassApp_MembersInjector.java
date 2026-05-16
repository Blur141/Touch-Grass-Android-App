package com.touchgrass;

import androidx.hilt.work.HiltWorkerFactory;
import com.touchgrass.utils.NotificationHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class TouchGrassApp_MembersInjector implements MembersInjector<TouchGrassApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private TouchGrassApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(TouchGrassApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<TouchGrassApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new TouchGrassApp_MembersInjector(workerFactoryProvider, notificationHelperProvider);
  }

  @InjectedFieldSignature("com.touchgrass.TouchGrassApp.workerFactory")
  public static void injectWorkerFactory(TouchGrassApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.touchgrass.TouchGrassApp.notificationHelper")
  public static void injectNotificationHelper(TouchGrassApp instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
