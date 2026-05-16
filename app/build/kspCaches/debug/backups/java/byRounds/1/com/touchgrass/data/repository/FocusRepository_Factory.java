package com.touchgrass.data.repository;

import com.touchgrass.data.local.dao.FocusSessionDao;
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
public final class FocusRepository_Factory implements Factory<FocusRepository> {
  private final Provider<FocusSessionDao> focusSessionDaoProvider;

  private FocusRepository_Factory(Provider<FocusSessionDao> focusSessionDaoProvider) {
    this.focusSessionDaoProvider = focusSessionDaoProvider;
  }

  @Override
  public FocusRepository get() {
    return newInstance(focusSessionDaoProvider.get());
  }

  public static FocusRepository_Factory create(Provider<FocusSessionDao> focusSessionDaoProvider) {
    return new FocusRepository_Factory(focusSessionDaoProvider);
  }

  public static FocusRepository newInstance(FocusSessionDao focusSessionDao) {
    return new FocusRepository(focusSessionDao);
  }
}
