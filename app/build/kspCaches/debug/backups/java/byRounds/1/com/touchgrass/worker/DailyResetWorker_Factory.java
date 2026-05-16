package com.touchgrass.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.data.repository.ChallengeRepository;
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
public final class DailyResetWorker_Factory {
  private final Provider<ChallengeRepository> challengeRepositoryProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<UsageStatsRepository> usageStatsRepositoryProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private DailyResetWorker_Factory(Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.challengeRepositoryProvider = challengeRepositoryProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.usageStatsRepositoryProvider = usageStatsRepositoryProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public DailyResetWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, challengeRepositoryProvider.get(), gamificationRepositoryProvider.get(), usageStatsRepositoryProvider.get(), preferencesDataStoreProvider.get(), notificationHelperProvider.get());
  }

  public static DailyResetWorker_Factory create(
      Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new DailyResetWorker_Factory(challengeRepositoryProvider, gamificationRepositoryProvider, usageStatsRepositoryProvider, preferencesDataStoreProvider, notificationHelperProvider);
  }

  public static DailyResetWorker newInstance(Context context, WorkerParameters workerParams,
      ChallengeRepository challengeRepository, GamificationRepository gamificationRepository,
      UsageStatsRepository usageStatsRepository, UserPreferencesDataStore preferencesDataStore,
      NotificationHelper notificationHelper) {
    return new DailyResetWorker(context, workerParams, challengeRepository, gamificationRepository, usageStatsRepository, preferencesDataStore, notificationHelper);
  }
}
