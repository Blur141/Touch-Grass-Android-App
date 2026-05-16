package com.touchgrass;

import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.utils.PermissionUtils;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<PermissionUtils> permissionUtilsProvider;

  private MainActivity_MembersInjector(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<PermissionUtils> permissionUtilsProvider) {
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.permissionUtilsProvider = permissionUtilsProvider;
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPreferencesDataStore(instance, preferencesDataStoreProvider.get());
    injectPermissionUtils(instance, permissionUtilsProvider.get());
  }

  public static MembersInjector<MainActivity> create(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<PermissionUtils> permissionUtilsProvider) {
    return new MainActivity_MembersInjector(preferencesDataStoreProvider, permissionUtilsProvider);
  }

  @InjectedFieldSignature("com.touchgrass.MainActivity.preferencesDataStore")
  public static void injectPreferencesDataStore(MainActivity instance,
      UserPreferencesDataStore preferencesDataStore) {
    instance.preferencesDataStore = preferencesDataStore;
  }

  @InjectedFieldSignature("com.touchgrass.MainActivity.permissionUtils")
  public static void injectPermissionUtils(MainActivity instance, PermissionUtils permissionUtils) {
    instance.permissionUtils = permissionUtils;
  }
}
