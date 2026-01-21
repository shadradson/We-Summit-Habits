package com.humangamestats.di;

import com.humangamestats.data.database.AppDatabase;
import com.humangamestats.data.database.dao.DataPointTemplateDao;
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
public final class DatabaseModule_ProvideDataPointTemplateDaoFactory implements Factory<DataPointTemplateDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideDataPointTemplateDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DataPointTemplateDao get() {
    return provideDataPointTemplateDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDataPointTemplateDaoFactory create(
      javax.inject.Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDataPointTemplateDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideDataPointTemplateDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDataPointTemplateDaoFactory(databaseProvider);
  }

  public static DataPointTemplateDao provideDataPointTemplateDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDataPointTemplateDao(database));
  }
}
