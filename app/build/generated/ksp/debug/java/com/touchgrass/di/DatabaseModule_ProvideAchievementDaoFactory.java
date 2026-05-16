package com.touchgrass.di;

import com.touchgrass.data.local.dao.AchievementDao;
import com.touchgrass.data.local.database.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAchievementDaoFactory implements Factory<AchievementDao> {
  private final Provider<AppDatabase> dbProvider;

  private DatabaseModule_ProvideAchievementDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AchievementDao get() {
    return provideAchievementDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAchievementDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideAchievementDaoFactory(dbProvider);
  }

  public static AchievementDao provideAchievementDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAchievementDao(db));
  }
}
