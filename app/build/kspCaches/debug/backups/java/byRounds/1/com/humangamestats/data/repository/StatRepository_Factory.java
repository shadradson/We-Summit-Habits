package com.humangamestats.data.repository;

import com.humangamestats.data.database.dao.StatDao;
import com.humangamestats.data.database.dao.StatRecordDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.humangamestats.di.IoDispatcher")
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
public final class StatRepository_Factory implements Factory<StatRepository> {
  private final Provider<StatDao> statDaoProvider;

  private final Provider<StatRecordDao> statRecordDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public StatRepository_Factory(Provider<StatDao> statDaoProvider,
      Provider<StatRecordDao> statRecordDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.statDaoProvider = statDaoProvider;
    this.statRecordDaoProvider = statRecordDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public StatRepository get() {
    return newInstance(statDaoProvider.get(), statRecordDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static StatRepository_Factory create(javax.inject.Provider<StatDao> statDaoProvider,
      javax.inject.Provider<StatRecordDao> statRecordDaoProvider,
      javax.inject.Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new StatRepository_Factory(Providers.asDaggerProvider(statDaoProvider), Providers.asDaggerProvider(statRecordDaoProvider), Providers.asDaggerProvider(ioDispatcherProvider));
  }

  public static StatRepository_Factory create(Provider<StatDao> statDaoProvider,
      Provider<StatRecordDao> statRecordDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new StatRepository_Factory(statDaoProvider, statRecordDaoProvider, ioDispatcherProvider);
  }

  public static StatRepository newInstance(StatDao statDao, StatRecordDao statRecordDao,
      CoroutineDispatcher ioDispatcher) {
    return new StatRepository(statDao, statRecordDao, ioDispatcher);
  }
}
