package com.touchgrass.ui.screens.focus;

import android.content.Context;
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
public final class FocusViewModel_Factory implements Factory<FocusViewModel> {
  private final Provider<Context> contextProvider;

  private FocusViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FocusViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static FocusViewModel_Factory create(Provider<Context> contextProvider) {
    return new FocusViewModel_Factory(contextProvider);
  }

  public static FocusViewModel newInstance(Context context) {
    return new FocusViewModel(context);
  }
}
