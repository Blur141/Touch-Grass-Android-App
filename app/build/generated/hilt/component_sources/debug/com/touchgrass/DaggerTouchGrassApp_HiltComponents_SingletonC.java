package com.touchgrass;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.touchgrass.data.datastore.UserPreferencesDataStore;
import com.touchgrass.data.local.dao.AchievementDao;
import com.touchgrass.data.local.dao.ChallengeDao;
import com.touchgrass.data.local.dao.FocusSessionDao;
import com.touchgrass.data.local.dao.UsageStatDao;
import com.touchgrass.data.local.dao.UserProfileDao;
import com.touchgrass.data.local.database.AppDatabase;
import com.touchgrass.data.repository.ChallengeRepository;
import com.touchgrass.data.repository.FocusRepository;
import com.touchgrass.data.repository.GamificationRepository;
import com.touchgrass.data.repository.UsageStatsRepository;
import com.touchgrass.di.AppModule_ProvideNotificationManagerFactory;
import com.touchgrass.di.AppModule_ProvideVibratorFactory;
import com.touchgrass.di.DatabaseModule_ProvideAchievementDaoFactory;
import com.touchgrass.di.DatabaseModule_ProvideChallengeDaoFactory;
import com.touchgrass.di.DatabaseModule_ProvideDatabaseFactory;
import com.touchgrass.di.DatabaseModule_ProvideFocusSessionDaoFactory;
import com.touchgrass.di.DatabaseModule_ProvideUsageStatDaoFactory;
import com.touchgrass.di.DatabaseModule_ProvideUserProfileDaoFactory;
import com.touchgrass.service.AppBlockingService;
import com.touchgrass.service.AppBlockingService_MembersInjector;
import com.touchgrass.service.FocusSessionService;
import com.touchgrass.service.FocusSessionService_MembersInjector;
import com.touchgrass.ui.screens.blocking.BlockingViewModel;
import com.touchgrass.ui.screens.blocking.BlockingViewModel_HiltModules;
import com.touchgrass.ui.screens.blocking.BlockingViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.blocking.BlockingViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.challenges.ChallengesViewModel;
import com.touchgrass.ui.screens.challenges.ChallengesViewModel_HiltModules;
import com.touchgrass.ui.screens.challenges.ChallengesViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.challenges.ChallengesViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.focus.FocusViewModel;
import com.touchgrass.ui.screens.focus.FocusViewModel_HiltModules;
import com.touchgrass.ui.screens.focus.FocusViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.focus.FocusViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.home.HomeViewModel;
import com.touchgrass.ui.screens.home.HomeViewModel_HiltModules;
import com.touchgrass.ui.screens.home.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.home.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.onboarding.OnboardingViewModel;
import com.touchgrass.ui.screens.onboarding.OnboardingViewModel_HiltModules;
import com.touchgrass.ui.screens.onboarding.OnboardingViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.onboarding.OnboardingViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.pet.PetViewModel;
import com.touchgrass.ui.screens.pet.PetViewModel_HiltModules;
import com.touchgrass.ui.screens.pet.PetViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.pet.PetViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.settings.SettingsViewModel;
import com.touchgrass.ui.screens.settings.SettingsViewModel_HiltModules;
import com.touchgrass.ui.screens.settings.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.settings.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.ui.screens.stats.StatsViewModel;
import com.touchgrass.ui.screens.stats.StatsViewModel_HiltModules;
import com.touchgrass.ui.screens.stats.StatsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.touchgrass.ui.screens.stats.StatsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.touchgrass.utils.HapticUtils;
import com.touchgrass.utils.NotificationHelper;
import com.touchgrass.utils.PermissionUtils;
import com.touchgrass.worker.DailyResetWorker;
import com.touchgrass.worker.DailyResetWorker_AssistedFactory;
import com.touchgrass.worker.UsageStatsWorker;
import com.touchgrass.worker.UsageStatsWorker_AssistedFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerTouchGrassApp_HiltComponents_SingletonC {
  private DaggerTouchGrassApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public TouchGrassApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements TouchGrassApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements TouchGrassApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements TouchGrassApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements TouchGrassApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements TouchGrassApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements TouchGrassApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements TouchGrassApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public TouchGrassApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends TouchGrassApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends TouchGrassApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    FragmentCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends TouchGrassApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends TouchGrassApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    ActivityCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    Map keySetMapOfClassOfAndBooleanBuilder() {
      MapBuilder mapBuilder = MapBuilder.<String, Boolean>newMapBuilder(8);
      mapBuilder.put(BlockingViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BlockingViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ChallengesViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ChallengesViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(FocusViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FocusViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(OnboardingViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, OnboardingViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(PetViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, PetViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(StatsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, StatsViewModel_HiltModules.KeyModule.provide());
      return mapBuilder.build();
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(keySetMapOfClassOfAndBooleanBuilder());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesDataStore(instance, singletonCImpl.userPreferencesDataStoreProvider.get());
      MainActivity_MembersInjector.injectPermissionUtils(instance, singletonCImpl.permissionUtilsProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends TouchGrassApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    Provider<BlockingViewModel> blockingViewModelProvider;

    Provider<ChallengesViewModel> challengesViewModelProvider;

    Provider<FocusViewModel> focusViewModelProvider;

    Provider<HomeViewModel> homeViewModelProvider;

    Provider<OnboardingViewModel> onboardingViewModelProvider;

    Provider<PetViewModel> petViewModelProvider;

    Provider<SettingsViewModel> settingsViewModelProvider;

    Provider<StatsViewModel> statsViewModelProvider;

    ViewModelCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        SavedStateHandle savedStateHandleParam, ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    Map hiltViewModelMapMapOfClassOfAndProviderOfViewModelBuilder() {
      MapBuilder mapBuilder = MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(8);
      mapBuilder.put(BlockingViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (blockingViewModelProvider)));
      mapBuilder.put(ChallengesViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (challengesViewModelProvider)));
      mapBuilder.put(FocusViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (focusViewModelProvider)));
      mapBuilder.put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (homeViewModelProvider)));
      mapBuilder.put(OnboardingViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (onboardingViewModelProvider)));
      mapBuilder.put(PetViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (petViewModelProvider)));
      mapBuilder.put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (settingsViewModelProvider)));
      mapBuilder.put(StatsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (statsViewModelProvider)));
      return mapBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.blockingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.challengesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.focusViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.petViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.statsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(hiltViewModelMapMapOfClassOfAndProviderOfViewModelBuilder());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.touchgrass.ui.screens.blocking.BlockingViewModel
          return (T) new BlockingViewModel(singletonCImpl.gamificationRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.touchgrass.ui.screens.challenges.ChallengesViewModel
          return (T) new ChallengesViewModel(singletonCImpl.challengeRepositoryProvider.get(), singletonCImpl.gamificationRepositoryProvider.get(), singletonCImpl.hapticUtilsProvider.get(), singletonCImpl.userPreferencesDataStoreProvider.get());

          case 2: // com.touchgrass.ui.screens.focus.FocusViewModel
          return (T) new FocusViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.touchgrass.ui.screens.home.HomeViewModel
          return (T) new HomeViewModel(singletonCImpl.usageStatsRepositoryProvider.get(), singletonCImpl.gamificationRepositoryProvider.get(), singletonCImpl.challengeRepositoryProvider.get(), singletonCImpl.userPreferencesDataStoreProvider.get(), singletonCImpl.permissionUtilsProvider.get());

          case 4: // com.touchgrass.ui.screens.onboarding.OnboardingViewModel
          return (T) new OnboardingViewModel(singletonCImpl.userPreferencesDataStoreProvider.get());

          case 5: // com.touchgrass.ui.screens.pet.PetViewModel
          return (T) new PetViewModel(singletonCImpl.gamificationRepositoryProvider.get());

          case 6: // com.touchgrass.ui.screens.settings.SettingsViewModel
          return (T) new SettingsViewModel(singletonCImpl.userPreferencesDataStoreProvider.get(), singletonCImpl.gamificationRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.touchgrass.ui.screens.stats.StatsViewModel
          return (T) new StatsViewModel(singletonCImpl.usageStatsRepositoryProvider.get(), singletonCImpl.gamificationRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends TouchGrassApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends TouchGrassApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectAppBlockingService(AppBlockingService appBlockingService) {
      injectAppBlockingService2(appBlockingService);
    }

    @Override
    public void injectFocusSessionService(FocusSessionService focusSessionService) {
      injectFocusSessionService2(focusSessionService);
    }

    private AppBlockingService injectAppBlockingService2(AppBlockingService instance) {
      AppBlockingService_MembersInjector.injectPreferencesDataStore(instance, singletonCImpl.userPreferencesDataStoreProvider.get());
      AppBlockingService_MembersInjector.injectNotificationHelper(instance, singletonCImpl.notificationHelperProvider.get());
      return instance;
    }

    private FocusSessionService injectFocusSessionService2(FocusSessionService instance2) {
      FocusSessionService_MembersInjector.injectFocusRepository(instance2, singletonCImpl.focusRepositoryProvider.get());
      FocusSessionService_MembersInjector.injectGamificationRepository(instance2, singletonCImpl.gamificationRepositoryProvider.get());
      FocusSessionService_MembersInjector.injectNotificationHelper(instance2, singletonCImpl.notificationHelperProvider.get());
      return instance2;
    }
  }

  private static final class SingletonCImpl extends TouchGrassApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    Provider<AppDatabase> provideDatabaseProvider;

    Provider<ChallengeRepository> challengeRepositoryProvider;

    Provider<GamificationRepository> gamificationRepositoryProvider;

    Provider<UsageStatsRepository> usageStatsRepositoryProvider;

    Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider;

    Provider<NotificationManager> provideNotificationManagerProvider;

    Provider<NotificationHelper> notificationHelperProvider;

    Provider<DailyResetWorker_AssistedFactory> dailyResetWorker_AssistedFactoryProvider;

    Provider<UsageStatsWorker_AssistedFactory> usageStatsWorker_AssistedFactoryProvider;

    Provider<PermissionUtils> permissionUtilsProvider;

    Provider<Vibrator> provideVibratorProvider;

    Provider<HapticUtils> hapticUtilsProvider;

    Provider<FocusRepository> focusRepositoryProvider;

    SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    ChallengeDao challengeDao() {
      return DatabaseModule_ProvideChallengeDaoFactory.provideChallengeDao(provideDatabaseProvider.get());
    }

    UserProfileDao userProfileDao() {
      return DatabaseModule_ProvideUserProfileDaoFactory.provideUserProfileDao(provideDatabaseProvider.get());
    }

    AchievementDao achievementDao() {
      return DatabaseModule_ProvideAchievementDaoFactory.provideAchievementDao(provideDatabaseProvider.get());
    }

    UsageStatDao usageStatDao() {
      return DatabaseModule_ProvideUsageStatDaoFactory.provideUsageStatDao(provideDatabaseProvider.get());
    }

    Map mapOfStringAndProviderOfWorkerAssistedFactoryOfBuilder() {
      MapBuilder mapBuilder = MapBuilder.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>newMapBuilder(2);
      mapBuilder.put("com.touchgrass.worker.DailyResetWorker", ((Provider) (dailyResetWorker_AssistedFactoryProvider)));
      mapBuilder.put("com.touchgrass.worker.UsageStatsWorker", ((Provider) (usageStatsWorker_AssistedFactoryProvider)));
      return mapBuilder.build();
    }

    Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return mapOfStringAndProviderOfWorkerAssistedFactoryOfBuilder();
    }

    HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    FocusSessionDao focusSessionDao() {
      return DatabaseModule_ProvideFocusSessionDaoFactory.provideFocusSessionDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 2));
      this.challengeRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ChallengeRepository>(singletonCImpl, 1));
      this.gamificationRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<GamificationRepository>(singletonCImpl, 3));
      this.usageStatsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UsageStatsRepository>(singletonCImpl, 4));
      this.userPreferencesDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferencesDataStore>(singletonCImpl, 5));
      this.provideNotificationManagerProvider = DoubleCheck.provider(new SwitchingProvider<NotificationManager>(singletonCImpl, 7));
      this.notificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 6));
      this.dailyResetWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<DailyResetWorker_AssistedFactory>(singletonCImpl, 0));
      this.usageStatsWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<UsageStatsWorker_AssistedFactory>(singletonCImpl, 8));
      this.permissionUtilsProvider = DoubleCheck.provider(new SwitchingProvider<PermissionUtils>(singletonCImpl, 9));
      this.provideVibratorProvider = DoubleCheck.provider(new SwitchingProvider<Vibrator>(singletonCImpl, 11));
      this.hapticUtilsProvider = DoubleCheck.provider(new SwitchingProvider<HapticUtils>(singletonCImpl, 10));
      this.focusRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FocusRepository>(singletonCImpl, 12));
    }

    @Override
    public void injectTouchGrassApp(TouchGrassApp touchGrassApp) {
      injectTouchGrassApp2(touchGrassApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private TouchGrassApp injectTouchGrassApp2(TouchGrassApp instance) {
      TouchGrassApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      TouchGrassApp_MembersInjector.injectNotificationHelper(instance, notificationHelperProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.touchgrass.worker.DailyResetWorker_AssistedFactory
          return (T) new DailyResetWorker_AssistedFactory() {
            @Override
            public DailyResetWorker create(Context context, WorkerParameters workerParams) {
              return new DailyResetWorker(context, workerParams, singletonCImpl.challengeRepositoryProvider.get(), singletonCImpl.gamificationRepositoryProvider.get(), singletonCImpl.usageStatsRepositoryProvider.get(), singletonCImpl.userPreferencesDataStoreProvider.get(), singletonCImpl.notificationHelperProvider.get());
            }
          };

          case 1: // com.touchgrass.data.repository.ChallengeRepository
          return (T) new ChallengeRepository(singletonCImpl.challengeDao());

          case 2: // com.touchgrass.data.local.database.AppDatabase
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.touchgrass.data.repository.GamificationRepository
          return (T) new GamificationRepository(singletonCImpl.userProfileDao(), singletonCImpl.achievementDao());

          case 4: // com.touchgrass.data.repository.UsageStatsRepository
          return (T) new UsageStatsRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.usageStatDao());

          case 5: // com.touchgrass.data.datastore.UserPreferencesDataStore
          return (T) new UserPreferencesDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.touchgrass.utils.NotificationHelper
          return (T) new NotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideNotificationManagerProvider.get());

          case 7: // android.app.NotificationManager
          return (T) AppModule_ProvideNotificationManagerFactory.provideNotificationManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.touchgrass.worker.UsageStatsWorker_AssistedFactory
          return (T) new UsageStatsWorker_AssistedFactory() {
            @Override
            public UsageStatsWorker create(Context context2, WorkerParameters workerParams2) {
              return new UsageStatsWorker(context2, workerParams2, singletonCImpl.usageStatsRepositoryProvider.get(), singletonCImpl.gamificationRepositoryProvider.get(), singletonCImpl.userPreferencesDataStoreProvider.get(), singletonCImpl.notificationHelperProvider.get());
            }
          };

          case 9: // com.touchgrass.utils.PermissionUtils
          return (T) new PermissionUtils(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.touchgrass.utils.HapticUtils
          return (T) new HapticUtils(singletonCImpl.provideVibratorProvider.get());

          case 11: // android.os.Vibrator
          return (T) AppModule_ProvideVibratorFactory.provideVibrator(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.touchgrass.data.repository.FocusRepository
          return (T) new FocusRepository(singletonCImpl.focusSessionDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
