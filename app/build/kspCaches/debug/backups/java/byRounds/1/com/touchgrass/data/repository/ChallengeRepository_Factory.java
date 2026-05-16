package com.touchgrass.data.repository;

import com.touchgrass.data.local.dao.ChallengeDao;
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
public final class ChallengeRepository_Factory implements Factory<ChallengeRepository> {
  private final Provider<ChallengeDao> challengeDaoProvider;

  private ChallengeRepository_Factory(Provider<ChallengeDao> challengeDaoProvider) {
    this.challengeDaoProvider = challengeDaoProvider;
  }

  @Override
  public ChallengeRepository get() {
    return newInstance(challengeDaoProvider.get());
  }

  public static ChallengeRepository_Factory create(Provider<ChallengeDao> challengeDaoProvider) {
    return new ChallengeRepository_Factory(challengeDaoProvider);
  }

  public static ChallengeRepository newInstance(ChallengeDao challengeDao) {
    return new ChallengeRepository(challengeDao);
  }
}
