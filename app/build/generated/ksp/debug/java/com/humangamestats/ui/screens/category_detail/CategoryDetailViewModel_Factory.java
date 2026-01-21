package com.humangamestats.ui.screens.category_detail;

import androidx.lifecycle.SavedStateHandle;
import com.humangamestats.data.repository.StatCategoryRepository;
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
public final class CategoryDetailViewModel_Factory implements Factory<CategoryDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StatCategoryRepository> categoryRepositoryProvider;

  private final Provider<StatRepository> statRepositoryProvider;

  public CategoryDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatCategoryRepository> categoryRepositoryProvider,
      Provider<StatRepository> statRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.statRepositoryProvider = statRepositoryProvider;
  }

  @Override
  public CategoryDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), categoryRepositoryProvider.get(), statRepositoryProvider.get());
  }

  public static CategoryDetailViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<StatCategoryRepository> categoryRepositoryProvider,
      javax.inject.Provider<StatRepository> statRepositoryProvider) {
    return new CategoryDetailViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(categoryRepositoryProvider), Providers.asDaggerProvider(statRepositoryProvider));
  }

  public static CategoryDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StatCategoryRepository> categoryRepositoryProvider,
      Provider<StatRepository> statRepositoryProvider) {
    return new CategoryDetailViewModel_Factory(savedStateHandleProvider, categoryRepositoryProvider, statRepositoryProvider);
  }

  public static CategoryDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      StatCategoryRepository categoryRepository, StatRepository statRepository) {
    return new CategoryDetailViewModel(savedStateHandle, categoryRepository, statRepository);
  }
}
