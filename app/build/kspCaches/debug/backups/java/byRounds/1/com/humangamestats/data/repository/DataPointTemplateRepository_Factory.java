package com.humangamestats.data.repository;

import com.humangamestats.data.database.dao.DataPointTemplateDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DataPointTemplateRepository_Factory implements Factory<DataPointTemplateRepository> {
  private final Provider<DataPointTemplateDao> templateDaoProvider;

  public DataPointTemplateRepository_Factory(Provider<DataPointTemplateDao> templateDaoProvider) {
    this.templateDaoProvider = templateDaoProvider;
  }

  @Override
  public DataPointTemplateRepository get() {
    return newInstance(templateDaoProvider.get());
  }

  public static DataPointTemplateRepository_Factory create(
      javax.inject.Provider<DataPointTemplateDao> templateDaoProvider) {
    return new DataPointTemplateRepository_Factory(Providers.asDaggerProvider(templateDaoProvider));
  }

  public static DataPointTemplateRepository_Factory create(
      Provider<DataPointTemplateDao> templateDaoProvider) {
    return new DataPointTemplateRepository_Factory(templateDaoProvider);
  }

  public static DataPointTemplateRepository newInstance(DataPointTemplateDao templateDao) {
    return new DataPointTemplateRepository(templateDao);
  }
}
