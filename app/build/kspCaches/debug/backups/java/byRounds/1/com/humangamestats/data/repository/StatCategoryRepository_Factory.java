package com.humangamestats.data.repository;

import com.humangamestats.data.database.dao.StatCategoryDao;
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
public final class StatCategoryRepository_Factory implements Factory<StatCategoryRepository> {
  private final Provider<StatCategoryDao> statCategoryDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public StatCategoryRepository_Factory(Provider<StatCategoryDao> statCategoryDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.statCategoryDaoProvider = statCategoryDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public StatCategoryRepository get() {
    return newInstance(statCategoryDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static StatCategoryRepository_Factory create(
      javax.inject.Provider<StatCategoryDao> statCategoryDaoProvider,
      javax.inject.Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new StatCategoryRepository_Factory(Providers.asDaggerProvider(statCategoryDaoProvider), Providers.asDaggerProvider(ioDispatcherProvider));
  }

  public static StatCategoryRepository_Factory create(
      Provider<StatCategoryDao> statCategoryDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new StatCategoryRepository_Factory(statCategoryDaoProvider, ioDispatcherProvider);
  }

  public static StatCategoryRepository newInstance(StatCategoryDao statCategoryDao,
      CoroutineDispatcher ioDispatcher) {
    return new StatCategoryRepository(statCategoryDao, ioDispatcher);
  }
}
