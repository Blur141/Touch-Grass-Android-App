package com.touchgrass.ui.screens.home;

import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.data.repository.ChallengeRepository;
import com.touchgrass.data.repository.GamificationRepository;
import com.touchgrass.data.repository.UsageStatsRepository;
import com.touchgrass.utils.PermissionUtils;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<UsageStatsRepository> usageStatsRepositoryProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<ChallengeRepository> challengeRepositoryProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<PermissionUtils> permissionUtilsProvider;

  private HomeViewModel_Factory(Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<PermissionUtils> permissionUtilsProvider) {
    this.usageStatsRepositoryProvider = usageStatsRepositoryProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.challengeRepositoryProvider = challengeRepositoryProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.permissionUtilsProvider = permissionUtilsProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(usageStatsRepositoryProvider.get(), gamificationRepositoryProvider.get(), challengeRepositoryProvider.get(), preferencesDataStoreProvider.get(), permissionUtilsProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<PermissionUtils> permissionUtilsProvider) {
    return new HomeViewModel_Factory(usageStatsRepositoryProvider, gamificationRepositoryProvider, challengeRepositoryProvider, preferencesDataStoreProvider, permissionUtilsProvider);
  }

  public static HomeViewModel newInstance(UsageStatsRepository usageStatsRepository,
      GamificationRepository gamificationRepository, ChallengeRepository challengeRepository,
      UserPreferencesDataStore preferencesDataStore, PermissionUtils permissionUtils) {
    return new HomeViewModel(usageStatsRepository, gamificationRepository, challengeRepository, preferencesDataStore, permissionUtils);
  }
}
