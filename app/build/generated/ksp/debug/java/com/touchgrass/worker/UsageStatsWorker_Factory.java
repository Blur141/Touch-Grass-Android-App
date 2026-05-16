package com.touchgrass.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.data.repository.GamificationRepository;
import com.touchgrass.data.repository.UsageStatsRepository;
import com.touchgrass.utils.NotificationHelper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class UsageStatsWorker_Factory {
  private final Provider<UsageStatsRepository> usageStatsRepositoryProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private UsageStatsWorker_Factory(Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.usageStatsRepositoryProvider = usageStatsRepositoryProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public UsageStatsWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, usageStatsRepositoryProvider.get(), gamificationRepositoryProvider.get(), preferencesDataStoreProvider.get(), notificationHelperProvider.get());
  }

  public static UsageStatsWorker_Factory create(
      Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new UsageStatsWorker_Factory(usageStatsRepositoryProvider, gamificationRepositoryProvider, preferencesDataStoreProvider, notificationHelperProvider);
  }

  public static UsageStatsWorker newInstance(Context context, WorkerParameters workerParams,
      UsageStatsRepository usageStatsRepository, GamificationRepository gamificationRepository,
      UserPreferencesDataStore preferencesDataStore, NotificationHelper notificationHelper) {
    return new UsageStatsWorker(context, workerParams, usageStatsRepository, gamificationRepository, preferencesDataStore, notificationHelper);
  }
}
