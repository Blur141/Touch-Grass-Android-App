package com.touchgrass.utils;

import android.content.Context;
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
public final class PermissionUtils_Factory implements Factory<PermissionUtils> {
  private final Provider<Context> contextProvider;

  private PermissionUtils_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PermissionUtils get() {
    return newInstance(contextProvider.get());
  }

  public static PermissionUtils_Factory create(Provider<Context> contextProvider) {
    return new PermissionUtils_Factory(contextProvider);
  }

  public static PermissionUtils newInstance(Context context) {
    return new PermissionUtils(context);
  }
}
