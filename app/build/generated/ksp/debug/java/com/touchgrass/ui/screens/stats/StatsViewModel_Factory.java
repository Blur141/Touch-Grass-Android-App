package com.touchgrass.ui.screens.stats;

import com.touchgrass.data.repository.GamificationRepository;
import com.touchgrass.data.repository.UsageStatsRepository;
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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<UsageStatsRepository> usageStatsRepositoryProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private StatsViewModel_Factory(Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider) {
    this.usageStatsRepositoryProvider = usageStatsRepositoryProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(usageStatsRepositoryProvider.get(), gamificationRepositoryProvider.get());
  }

  public static StatsViewModel_Factory create(
      Provider<UsageStatsRepository> usageStatsRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider) {
    return new StatsViewModel_Factory(usageStatsRepositoryProvider, gamificationRepositoryProvider);
  }

  public static StatsViewModel newInstance(UsageStatsRepository usageStatsRepository,
      GamificationRepository gamificationRepository) {
    return new StatsViewModel(usageStatsRepository, gamificationRepository);
  }
}
