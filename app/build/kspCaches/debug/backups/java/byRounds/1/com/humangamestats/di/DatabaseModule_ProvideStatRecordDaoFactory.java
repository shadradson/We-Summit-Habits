package com.humangamestats.di;

import com.humangamestats.data.database.AppDatabase;
import com.humangamestats.data.database.dao.StatRecordDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DatabaseModule_ProvideStatRecordDaoFactory implements Factory<StatRecordDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideStatRecordDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public StatRecordDao get() {
    return provideStatRecordDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideStatRecordDaoFactory create(
      javax.inject.Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideStatRecordDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideStatRecordDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideStatRecordDaoFactory(databaseProvider);
  }

  public static StatRecordDao provideStatRecordDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStatRecordDao(database));
  }
}
