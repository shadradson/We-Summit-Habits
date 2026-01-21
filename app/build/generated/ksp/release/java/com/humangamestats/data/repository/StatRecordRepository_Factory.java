package com.humangamestats.data.repository;

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
public final class StatRecordRepository_Factory implements Factory<StatRecordRepository> {
  private final Provider<StatRecordDao> statRecordDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public StatRecordRepository_Factory(Provider<StatRecordDao> statRecordDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.statRecordDaoProvider = statRecordDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public StatRecordRepository get() {
    return newInstance(statRecordDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static StatRecordRepository_Factory create(
      javax.inject.Provider<StatRecordDao> statRecordDaoProvider,
      javax.inject.Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new StatRecordRepository_Factory(Providers.asDaggerProvider(statRecordDaoProvider), Providers.asDaggerProvider(ioDispatcherProvider));
  }

  public static StatRecordRepository_Factory create(Provider<StatRecordDao> statRecordDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new StatRecordRepository_Factory(statRecordDaoProvider, ioDispatcherProvider);
  }

  public static StatRecordRepository newInstance(StatRecordDao statRecordDao,
      CoroutineDispatcher ioDispatcher) {
    return new StatRecordRepository(statRecordDao, ioDispatcher);
  }
}
