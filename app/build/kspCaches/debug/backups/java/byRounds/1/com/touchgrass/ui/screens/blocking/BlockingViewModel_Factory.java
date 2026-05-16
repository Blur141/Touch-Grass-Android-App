package com.touchgrass.ui.screens.blocking;

import androidx.lifecycle.SavedStateHandle;
import com.touchgrass.data.repository.GamificationRepository;
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
public final class BlockingViewModel_Factory implements Factory<BlockingViewModel> {
  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private BlockingViewModel_Factory(Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public BlockingViewModel get() {
    return newInstance(gamificationRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static BlockingViewModel_Factory create(
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new BlockingViewModel_Factory(gamificationRepositoryProvider, savedStateHandleProvider);
  }

  public static BlockingViewModel newInstance(GamificationRepository gamificationRepository,
      SavedStateHandle savedStateHandle) {
    return new BlockingViewModel(gamificationRepository, savedStateHandle);
  }
}
