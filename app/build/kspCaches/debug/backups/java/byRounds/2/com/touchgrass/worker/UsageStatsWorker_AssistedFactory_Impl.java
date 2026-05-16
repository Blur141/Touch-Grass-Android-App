package com.touchgrass.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class UsageStatsWorker_AssistedFactory_Impl implements UsageStatsWorker_AssistedFactory {
  private final UsageStatsWorker_Factory delegateFactory;

  UsageStatsWorker_AssistedFactory_Impl(UsageStatsWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public UsageStatsWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<UsageStatsWorker_AssistedFactory> create(
      UsageStatsWorker_Factory delegateFactory) {
    return InstanceFactory.create(new UsageStatsWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<UsageStatsWorker_AssistedFactory> createFactoryProvider(
      UsageStatsWorker_Factory delegateFactory) {
    return InstanceFactory.create(new UsageStatsWorker_AssistedFactory_Impl(delegateFactory));
  }
}
