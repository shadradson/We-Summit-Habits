package com.humangamestats.ui.screens.stat;

import androidx.lifecycle.SavedStateHandle;
import com.humangamestats.data.repository.DataPointTemplateRepository;
import com.humangamestats.data.repository.StatRepository;
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
public final class StatFormViewModel_Factory implements Factory<StatFormViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StatRepository> statRepositoryProvider;

  private final Provider<DataPointTemplateRepository> templateRepositoryProvider;

  public StatFormViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<DataPointTemplateRepository> templateRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.statRepositoryProvider = statRepositoryProvider;
    this.templateRepositoryProvider = templateRepositoryProvider;
  }

  @Override
  public StatFormViewModel get() {
    return newInstance(savedStateHandleProvider.get(), statRepositoryProvider.get(), templateRepositoryProvider.get());
  }

  public static StatFormViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<StatRepository> statRepositoryProvider,
      javax.inject.Provider<DataPointTemplateRepository> templateRepositoryProvider) {
    return new StatFormViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(statRepositoryProvider), Providers.asDaggerProvider(templateRepositoryProvider));
  }

  public static StatFormViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<DataPointTemplateRepository> templateRepositoryProvider) {
    return new StatFormViewModel_Factory(savedStateHandleProvider, statRepositoryProvider, templateRepositoryProvider);
  }

  public static StatFormViewModel newInstance(SavedStateHandle savedStateHandle,
      StatRepository statRepository, DataPointTemplateRepository templateRepository) {
    return new StatFormViewModel(savedStateHandle, statRepository, templateRepository);
  }
}
