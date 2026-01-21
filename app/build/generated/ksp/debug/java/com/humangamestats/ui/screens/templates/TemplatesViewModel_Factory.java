package com.humangamestats.ui.screens.templates;

import com.humangamestats.data.repository.DataPointTemplateRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class TemplatesViewModel_Factory implements Factory<TemplatesViewModel> {
  private final Provider<DataPointTemplateRepository> templateRepositoryProvider;

  public TemplatesViewModel_Factory(
      Provider<DataPointTemplateRepository> templateRepositoryProvider) {
    this.templateRepositoryProvider = templateRepositoryProvider;
  }

  @Override
  public TemplatesViewModel get() {
    return newInstance(templateRepositoryProvider.get());
  }

  public static TemplatesViewModel_Factory create(
      javax.inject.Provider<DataPointTemplateRepository> templateRepositoryProvider) {
    return new TemplatesViewModel_Factory(Providers.asDaggerProvider(templateRepositoryProvider));
  }

  public static TemplatesViewModel_Factory create(
      Provider<DataPointTemplateRepository> templateRepositoryProvider) {
    return new TemplatesViewModel_Factory(templateRepositoryProvider);
  }

  public static TemplatesViewModel newInstance(DataPointTemplateRepository templateRepository) {
    return new TemplatesViewModel(templateRepository);
  }
}
