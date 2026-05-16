package com.touchgrass.ui.screens.pet;

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
public final class PetViewModel_Factory implements Factory<PetViewModel> {
  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private PetViewModel_Factory(Provider<GamificationRepository> gamificationRepositoryProvider) {
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
  }

  @Override
  public PetViewModel get() {
    return newInstance(gamificationRepositoryProvider.get());
  }

  public static PetViewModel_Factory create(
      Provider<GamificationRepository> gamificationRepositoryProvider) {
    return new PetViewModel_Factory(gamificationRepositoryProvider);
  }

  public static PetViewModel newInstance(GamificationRepository gamificationRepository) {
    return new PetViewModel(gamificationRepository);
  }
}
