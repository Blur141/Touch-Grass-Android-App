package com.touchgrass.data.repository;

import android.content.Context;
import com.touchgrass.data.local.dao.UsageStatDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class UsageStatsRepository_Factory implements Factory<UsageStatsRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<UsageStatDao> usageStatDaoProvider;

  private UsageStatsRepository_Factory(Provider<Context> contextProvider,
      Provider<UsageStatDao> usageStatDaoProvider) {
    this.contextProvider = contextProvider;
    this.usageStatDaoProvider = usageStatDaoProvider;
  }

  @Override
  public UsageStatsRepository get() {
    return newInstance(contextProvider.get(), usageStatDaoProvider.get());
  }

  public static UsageStatsRepository_Factory create(Provider<Context> contextProvider,
      Provider<UsageStatDao> usageStatDaoProvider) {
    return new UsageStatsRepository_Factory(contextProvider, usageStatDaoProvider);
  }

  public static UsageStatsRepository newInstance(Context context, UsageStatDao usageStatDao) {
    return new UsageStatsRepository(context, usageStatDao);
  }
}
