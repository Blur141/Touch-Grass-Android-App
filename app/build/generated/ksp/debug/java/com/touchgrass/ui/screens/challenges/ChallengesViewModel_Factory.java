package com.touchgrass.ui.screens.challenges;

import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.data.repository.ChallengeRepository;
import com.touchgrass.data.repository.GamificationRepository;
import com.touchgrass.utils.HapticUtils;
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
public final class ChallengesViewModel_Factory implements Factory<ChallengesViewModel> {
  private final Provider<ChallengeRepository> challengeRepositoryProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<HapticUtils> hapticUtilsProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private ChallengesViewModel_Factory(Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<HapticUtils> hapticUtilsProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    this.challengeRepositoryProvider = challengeRepositoryProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.hapticUtilsProvider = hapticUtilsProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
  }

  @Override
  public ChallengesViewModel get() {
    return newInstance(challengeRepositoryProvider.get(), gamificationRepositoryProvider.get(), hapticUtilsProvider.get(), preferencesDataStoreProvider.get());
  }

  public static ChallengesViewModel_Factory create(
      Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<HapticUtils> hapticUtilsProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    return new ChallengesViewModel_Factory(challengeRepositoryProvider, gamificationRepositoryProvider, hapticUtilsProvider, preferencesDataStoreProvider);
  }

  public static ChallengesViewModel newInstance(ChallengeRepository challengeRepository,
      GamificationRepository gamificationRepository, HapticUtils hapticUtils,
      UserPreferencesDataStore preferencesDataStore) {
    return new ChallengesViewModel(challengeRepository, gamificationRepository, hapticUtils, preferencesDataStore);
  }
}
