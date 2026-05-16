package com.touchgrass.service;

import com.touchgrass.data.repository.FocusRepository;
import com.touchgrass.data.repository.GamificationRepository;
import com.touchgrass.utils.NotificationHelper;
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
public final class FocusSessionService_MembersInjector implements MembersInjector<FocusSessionService> {
  private final Provider<FocusRepository> focusRepositoryProvider;

  private final Provider<GamificationRepository> gamificationRepositoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private FocusSessionService_MembersInjector(Provider<FocusRepository> focusRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.focusRepositoryProvider = focusRepositoryProvider;
    this.gamificationRepositoryProvider = gamificationRepositoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(FocusSessionService instance) {
    injectFocusRepository(instance, focusRepositoryProvider.get());
    injectGamificationRepository(instance, gamificationRepositoryProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<FocusSessionService> create(
      Provider<FocusRepository> focusRepositoryProvider,
      Provider<GamificationRepository> gamificationRepositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new FocusSessionService_MembersInjector(focusRepositoryProvider, gamificationRepositoryProvider, notificationHelperProvider);
  }

  @InjectedFieldSignature("com.touchgrass.service.FocusSessionService.focusRepository")
  public static void injectFocusRepository(FocusSessionService instance,
      FocusRepository focusRepository) {
    instance.focusRepository = focusRepository;
  }

  @InjectedFieldSignature("com.touchgrass.service.FocusSessionService.gamificationRepository")
  public static void injectGamificationRepository(FocusSessionService instance,
      GamificationRepository gamificationRepository) {
    instance.gamificationRepository = gamificationRepository;
  }

  @InjectedFieldSignature("com.touchgrass.service.FocusSessionService.notificationHelper")
  public static void injectNotificationHelper(FocusSessionService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
