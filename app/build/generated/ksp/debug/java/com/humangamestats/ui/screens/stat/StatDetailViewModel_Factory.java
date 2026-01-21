package com.humangamestats.ui.screens.stat;

import androidx.lifecycle.SavedStateHandle;
import com.humangamestats.data.repository.StatRecordRepository;
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
public final class StatDetailViewModel_Factory implements Factory<StatDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StatRepository> statRepositoryProvider;

  private final Provider<StatRecordRepository> recordRepositoryProvider;

  public StatDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<StatRecordRepository> recordRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.statRepositoryProvider = statRepositoryProvider;
    this.recordRepositoryProvider = recordRepositoryProvider;
  }

  @Override
  public StatDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), statRepositoryProvider.get(), recordRepositoryProvider.get());
  }

  public static StatDetailViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<StatRepository> statRepositoryProvider,
      javax.inject.Provider<StatRecordRepository> recordRepositoryProvider) {
    return new StatDetailViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(statRepositoryProvider), Providers.asDaggerProvider(recordRepositoryProvider));
  }

  public static StatDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<StatRecordRepository> recordRepositoryProvider) {
    return new StatDetailViewModel_Factory(savedStateHandleProvider, statRepositoryProvider, recordRepositoryProvider);
  }

  public static StatDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      StatRepository statRepository, StatRecordRepository recordRepository) {
    return new StatDetailViewModel(savedStateHandle, statRepository, recordRepository);
  }
}
