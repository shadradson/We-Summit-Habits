package com.humangamestats.ui.screens.categories;

import com.humangamestats.data.repository.StatCategoryRepository;
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
public final class CategoriesViewModel_Factory implements Factory<CategoriesViewModel> {
  private final Provider<StatCategoryRepository> categoryRepositoryProvider;

  public CategoriesViewModel_Factory(Provider<StatCategoryRepository> categoryRepositoryProvider) {
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public CategoriesViewModel get() {
    return newInstance(categoryRepositoryProvider.get());
  }

  public static CategoriesViewModel_Factory create(
      javax.inject.Provider<StatCategoryRepository> categoryRepositoryProvider) {
    return new CategoriesViewModel_Factory(Providers.asDaggerProvider(categoryRepositoryProvider));
  }

  public static CategoriesViewModel_Factory create(
      Provider<StatCategoryRepository> categoryRepositoryProvider) {
    return new CategoriesViewModel_Factory(categoryRepositoryProvider);
  }

  public static CategoriesViewModel newInstance(StatCategoryRepository categoryRepository) {
    return new CategoriesViewModel(categoryRepository);
  }
}
