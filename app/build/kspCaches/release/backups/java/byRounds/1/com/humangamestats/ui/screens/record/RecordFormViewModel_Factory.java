package com.humangamestats.ui.screens.record;

import androidx.lifecycle.SavedStateHandle;
import com.humangamestats.data.datastore.SettingsDataStore;
import com.humangamestats.data.repository.StatRecordRepository;
import com.humangamestats.data.repository.StatRepository;
import com.humangamestats.data.service.LocationService;
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
public final class RecordFormViewModel_Factory implements Factory<RecordFormViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StatRepository> statRepositoryProvider;

  private final Provider<StatRecordRepository> recordRepositoryProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public RecordFormViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<StatRecordRepository> recordRepositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.statRepositoryProvider = statRepositoryProvider;
    this.recordRepositoryProvider = recordRepositoryProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public RecordFormViewModel get() {
    return newInstance(savedStateHandleProvider.get(), statRepositoryProvider.get(), recordRepositoryProvider.get(), locationServiceProvider.get(), settingsDataStoreProvider.get());
  }

  public static RecordFormViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<StatRepository> statRepositoryProvider,
      javax.inject.Provider<StatRecordRepository> recordRepositoryProvider,
      javax.inject.Provider<LocationService> locationServiceProvider,
      javax.inject.Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new RecordFormViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(statRepositoryProvider), Providers.asDaggerProvider(recordRepositoryProvider), Providers.asDaggerProvider(locationServiceProvider), Providers.asDaggerProvider(settingsDataStoreProvider));
  }

  public static RecordFormViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<StatRecordRepository> recordRepositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new RecordFormViewModel_Factory(savedStateHandleProvider, statRepositoryProvider, recordRepositoryProvider, locationServiceProvider, settingsDataStoreProvider);
  }

  public static RecordFormViewModel newInstance(SavedStateHandle savedStateHandle,
      StatRepository statRepository, StatRecordRepository recordRepository,
      LocationService locationService, SettingsDataStore settingsDataStore) {
    return new RecordFormViewModel(savedStateHandle, statRepository, recordRepository, locationService, settingsDataStore);
  }
}
