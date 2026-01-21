package com.humangamestats.ui.screens.settings;

import com.humangamestats.data.datastore.SettingsDataStore;
import com.humangamestats.data.repository.StatCategoryRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<StatCategoryRepository> categoryRepositoryProvider;

  private final Provider<StatRepository> statRepositoryProvider;

  private final Provider<StatRecordRepository> recordRepositoryProvider;

  public SettingsViewModel_Factory(Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<StatCategoryRepository> categoryRepositoryProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<StatRecordRepository> recordRepositoryProvider) {
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.statRepositoryProvider = statRepositoryProvider;
    this.recordRepositoryProvider = recordRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsDataStoreProvider.get(), categoryRepositoryProvider.get(), statRepositoryProvider.get(), recordRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      javax.inject.Provider<SettingsDataStore> settingsDataStoreProvider,
      javax.inject.Provider<StatCategoryRepository> categoryRepositoryProvider,
      javax.inject.Provider<StatRepository> statRepositoryProvider,
      javax.inject.Provider<StatRecordRepository> recordRepositoryProvider) {
    return new SettingsViewModel_Factory(Providers.asDaggerProvider(settingsDataStoreProvider), Providers.asDaggerProvider(categoryRepositoryProvider), Providers.asDaggerProvider(statRepositoryProvider), Providers.asDaggerProvider(recordRepositoryProvider));
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<StatCategoryRepository> categoryRepositoryProvider,
      Provider<StatRepository> statRepositoryProvider,
      Provider<StatRecordRepository> recordRepositoryProvider) {
    return new SettingsViewModel_Factory(settingsDataStoreProvider, categoryRepositoryProvider, statRepositoryProvider, recordRepositoryProvider);
  }

  public static SettingsViewModel newInstance(SettingsDataStore settingsDataStore,
      StatCategoryRepository categoryRepository, StatRepository statRepository,
      StatRecordRepository recordRepository) {
    return new SettingsViewModel(settingsDataStore, categoryRepository, statRepository, recordRepository);
  }
}
