package com.touchgrass.ui.screens.settings;

import android.content.Context;
import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.data.repository.GamificationRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<Context> contextProvider;

  private SettingsViewModel_Factory(Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<Context> contextProvider) {
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(preferencesDataStoreProvider.get(), gamificationRepositoryProvider.get(), contextProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<Context> contextProvider) {
    return new SettingsViewModel_Factory(preferencesDataStoreProvider, gamificationRepositoryProvider, contextProvider);
  }

  public static SettingsViewModel newInstance(UserPreferencesDataStore preferencesDataStore,
      GamificationRepository gamificationRepository, Context context) {
    return new SettingsViewModel(preferencesDataStore, gamificationRepository, context);
  }
}
