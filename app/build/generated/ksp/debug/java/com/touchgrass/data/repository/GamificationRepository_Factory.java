package com.touchgrass.data.repository;

import com.touchgrass.data.local.dao.AchievementDao;
import com.touchgrass.data.local.dao.UserProfileDao;
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
public final class GamificationRepository_Factory implements Factory<GamificationRepository> {
  private final Provider<UserProfileDao> userProfileDaoProvider;

  private final Provider<AchievementDao> achievementDaoProvider;

  private GamificationRepository_Factory(Provider<UserProfileDao> userProfileDaoProvider,
      Provider<AchievementDao> achievementDaoProvider) {
    this.userProfileDaoProvider = userProfileDaoProvider;
    this.achievementDaoProvider = achievementDaoProvider;
  }

  @Override
  public GamificationRepository get() {
    return newInstance(userProfileDaoProvider.get(), achievementDaoProvider.get());
  }

  public static GamificationRepository_Factory create(
      Provider<UserProfileDao> userProfileDaoProvider,
      Provider<AchievementDao> achievementDaoProvider) {
    return new GamificationRepository_Factory(userProfileDaoProvider, achievementDaoProvider);
  }

  public static GamificationRepository newInstance(UserProfileDao userProfileDao,
      AchievementDao achievementDao) {
    return new GamificationRepository(userProfileDao, achievementDao);
  }
}
