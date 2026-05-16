package com.touchgrass.ui.screens.onboarding;

import com.touchgrass.data.datastore.UserPreferencesDataStore;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private OnboardingViewModel_Factory(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(preferencesDataStoreProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    return new OnboardingViewModel_Factory(preferencesDataStoreProvider);
  }

  public static OnboardingViewModel newInstance(UserPreferencesDataStore preferencesDataStore) {
    return new OnboardingViewModel(preferencesDataStore);
  }
}
