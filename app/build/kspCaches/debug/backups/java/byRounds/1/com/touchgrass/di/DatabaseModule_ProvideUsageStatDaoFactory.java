package com.touchgrass.di;

import com.touchgrass.data.local.dao.UsageStatDao;
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
public final class DatabaseModule_ProvideUsageStatDaoFactory implements Factory<UsageStatDao> {
  private final Provider<AppDatabase> dbProvider;

  private DatabaseModule_ProvideUsageStatDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public UsageStatDao get() {
    return provideUsageStatDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideUsageStatDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideUsageStatDaoFactory(dbProvider);
  }

  public static UsageStatDao provideUsageStatDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideUsageStatDao(db));
  }
}
