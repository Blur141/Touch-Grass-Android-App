package com.touchgrass.di;

import com.touchgrass.data.local.dao.FocusSessionDao;
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
public final class DatabaseModule_ProvideFocusSessionDaoFactory implements Factory<FocusSessionDao> {
  private final Provider<AppDatabase> dbProvider;

  private DatabaseModule_ProvideFocusSessionDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FocusSessionDao get() {
    return provideFocusSessionDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideFocusSessionDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideFocusSessionDaoFactory(dbProvider);
  }

  public static FocusSessionDao provideFocusSessionDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFocusSessionDao(db));
  }
}
