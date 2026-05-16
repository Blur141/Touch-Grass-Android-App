package com.touchgrass.service;

import com.touchgrass.data.datastore.UserPreferencesDataStore;
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
public final class AppBlockingService_MembersInjector implements MembersInjector<AppBlockingService> {
  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private AppBlockingService_MembersInjector(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(AppBlockingService instance) {
    injectPreferencesDataStore(instance, preferencesDataStoreProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<AppBlockingService> create(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new AppBlockingService_MembersInjector(preferencesDataStoreProvider, notificationHelperProvider);
  }

  @InjectedFieldSignature("com.touchgrass.service.AppBlockingService.preferencesDataStore")
  public static void injectPreferencesDataStore(AppBlockingService instance,
      UserPreferencesDataStore preferencesDataStore) {
    instance.preferencesDataStore = preferencesDataStore;
  }

  @InjectedFieldSignature("com.touchgrass.service.AppBlockingService.notificationHelper")
  public static void injectNotificationHelper(AppBlockingService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
