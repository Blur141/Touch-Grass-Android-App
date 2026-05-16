package com.touchgrass.di;

import com.touchgrass.data.local.dao.ChallengeDao;
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
public final class DatabaseModule_ProvideChallengeDaoFactory implements Factory<ChallengeDao> {
  private final Provider<AppDatabase> dbProvider;

  private DatabaseModule_ProvideChallengeDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ChallengeDao get() {
    return provideChallengeDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideChallengeDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideChallengeDaoFactory(dbProvider);
  }

  public static ChallengeDao provideChallengeDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideChallengeDao(db));
  }
}
