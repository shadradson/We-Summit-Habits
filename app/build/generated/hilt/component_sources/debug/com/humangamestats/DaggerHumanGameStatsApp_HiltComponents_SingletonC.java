package com.humangamestats;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.humangamestats.data.database.AppDatabase;
import com.humangamestats.data.database.dao.DataPointTemplateDao;
import com.humangamestats.data.database.dao.StatCategoryDao;
import com.humangamestats.data.database.dao.StatDao;
import com.humangamestats.data.database.dao.StatRecordDao;
import com.humangamestats.data.datastore.SettingsDataStore;
import com.humangamestats.data.repository.DataPointTemplateRepository;
import com.humangamestats.data.repository.StatCategoryRepository;
import com.humangamestats.data.repository.StatRecordRepository;
import com.humangamestats.data.repository.StatRepository;
import com.humangamestats.data.service.LocationService;
import com.humangamestats.di.AppModule_ProvideFusedLocationProviderClientFactory;
import com.humangamestats.di.AppModule_ProvideIoDispatcherFactory;
import com.humangamestats.di.DatabaseModule_ProvideAppDatabaseFactory;
import com.humangamestats.di.DatabaseModule_ProvideDataPointTemplateDaoFactory;
import com.humangamestats.di.DatabaseModule_ProvideStatCategoryDaoFactory;
import com.humangamestats.di.DatabaseModule_ProvideStatDaoFactory;
import com.humangamestats.di.DatabaseModule_ProvideStatRecordDaoFactory;
import com.humangamestats.ui.screens.categories.CategoriesViewModel;
import com.humangamestats.ui.screens.categories.CategoriesViewModel_HiltModules;
import com.humangamestats.ui.screens.categories.CategoriesViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.categories.CategoriesViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.humangamestats.ui.screens.category_detail.CategoryDetailViewModel;
import com.humangamestats.ui.screens.category_detail.CategoryDetailViewModel_HiltModules;
import com.humangamestats.ui.screens.category_detail.CategoryDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.category_detail.CategoryDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.humangamestats.ui.screens.record.RecordFormViewModel;
import com.humangamestats.ui.screens.record.RecordFormViewModel_HiltModules;
import com.humangamestats.ui.screens.record.RecordFormViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.record.RecordFormViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.humangamestats.ui.screens.settings.SettingsViewModel;
import com.humangamestats.ui.screens.settings.SettingsViewModel_HiltModules;
import com.humangamestats.ui.screens.settings.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.settings.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.humangamestats.ui.screens.stat.StatDetailViewModel;
import com.humangamestats.ui.screens.stat.StatDetailViewModel_HiltModules;
import com.humangamestats.ui.screens.stat.StatDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.stat.StatDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.humangamestats.ui.screens.stat.StatFormViewModel;
import com.humangamestats.ui.screens.stat.StatFormViewModel_HiltModules;
import com.humangamestats.ui.screens.stat.StatFormViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.stat.StatFormViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.humangamestats.ui.screens.templates.TemplatesViewModel;
import com.humangamestats.ui.screens.templates.TemplatesViewModel_HiltModules;
import com.humangamestats.ui.screens.templates.TemplatesViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.humangamestats.ui.screens.templates.TemplatesViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerHumanGameStatsApp_HiltComponents_SingletonC {
  private DaggerHumanGameStatsApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public HumanGameStatsApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements HumanGameStatsApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements HumanGameStatsApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements HumanGameStatsApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements HumanGameStatsApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements HumanGameStatsApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements HumanGameStatsApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements HumanGameStatsApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public HumanGameStatsApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends HumanGameStatsApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends HumanGameStatsApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends HumanGameStatsApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends HumanGameStatsApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(7).put(CategoriesViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CategoriesViewModel_HiltModules.KeyModule.provide()).put(CategoryDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CategoryDetailViewModel_HiltModules.KeyModule.provide()).put(RecordFormViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, RecordFormViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).put(StatDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, StatDetailViewModel_HiltModules.KeyModule.provide()).put(StatFormViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, StatFormViewModel_HiltModules.KeyModule.provide()).put(TemplatesViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, TemplatesViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends HumanGameStatsApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CategoriesViewModel> categoriesViewModelProvider;

    private Provider<CategoryDetailViewModel> categoryDetailViewModelProvider;

    private Provider<RecordFormViewModel> recordFormViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StatDetailViewModel> statDetailViewModelProvider;

    private Provider<StatFormViewModel> statFormViewModelProvider;

    private Provider<TemplatesViewModel> templatesViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.categoriesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.categoryDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.recordFormViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.statDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.statFormViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.templatesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(7).put(CategoriesViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) categoriesViewModelProvider)).put(CategoryDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) categoryDetailViewModelProvider)).put(RecordFormViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) recordFormViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).put(StatDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) statDetailViewModelProvider)).put(StatFormViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) statFormViewModelProvider)).put(TemplatesViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) templatesViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.humangamestats.ui.screens.categories.CategoriesViewModel 
          return (T) new CategoriesViewModel(singletonCImpl.statCategoryRepositoryProvider.get());

          case 1: // com.humangamestats.ui.screens.category_detail.CategoryDetailViewModel 
          return (T) new CategoryDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.statCategoryRepositoryProvider.get(), singletonCImpl.statRepositoryProvider.get());

          case 2: // com.humangamestats.ui.screens.record.RecordFormViewModel 
          return (T) new RecordFormViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.statRepositoryProvider.get(), singletonCImpl.statRecordRepositoryProvider.get(), singletonCImpl.locationServiceProvider.get(), singletonCImpl.settingsDataStoreProvider.get());

          case 3: // com.humangamestats.ui.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.statCategoryRepositoryProvider.get(), singletonCImpl.statRepositoryProvider.get(), singletonCImpl.statRecordRepositoryProvider.get());

          case 4: // com.humangamestats.ui.screens.stat.StatDetailViewModel 
          return (T) new StatDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.statRepositoryProvider.get(), singletonCImpl.statRecordRepositoryProvider.get());

          case 5: // com.humangamestats.ui.screens.stat.StatFormViewModel 
          return (T) new StatFormViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.statRepositoryProvider.get(), singletonCImpl.dataPointTemplateRepositoryProvider.get());

          case 6: // com.humangamestats.ui.screens.templates.TemplatesViewModel 
          return (T) new TemplatesViewModel(singletonCImpl.dataPointTemplateRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends HumanGameStatsApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends HumanGameStatsApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends HumanGameStatsApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<StatCategoryDao> provideStatCategoryDaoProvider;

    private Provider<StatCategoryRepository> statCategoryRepositoryProvider;

    private Provider<StatDao> provideStatDaoProvider;

    private Provider<StatRecordDao> provideStatRecordDaoProvider;

    private Provider<StatRepository> statRepositoryProvider;

    private Provider<StatRecordRepository> statRecordRepositoryProvider;

    private Provider<FusedLocationProviderClient> provideFusedLocationProviderClientProvider;

    private Provider<LocationService> locationServiceProvider;

    private Provider<SettingsDataStore> settingsDataStoreProvider;

    private Provider<DataPointTemplateDao> provideDataPointTemplateDaoProvider;

    private Provider<DataPointTemplateRepository> dataPointTemplateRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 2));
      this.provideStatCategoryDaoProvider = DoubleCheck.provider(new SwitchingProvider<StatCategoryDao>(singletonCImpl, 1));
      this.statCategoryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StatCategoryRepository>(singletonCImpl, 0));
      this.provideStatDaoProvider = DoubleCheck.provider(new SwitchingProvider<StatDao>(singletonCImpl, 4));
      this.provideStatRecordDaoProvider = DoubleCheck.provider(new SwitchingProvider<StatRecordDao>(singletonCImpl, 5));
      this.statRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StatRepository>(singletonCImpl, 3));
      this.statRecordRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StatRecordRepository>(singletonCImpl, 6));
      this.provideFusedLocationProviderClientProvider = DoubleCheck.provider(new SwitchingProvider<FusedLocationProviderClient>(singletonCImpl, 8));
      this.locationServiceProvider = DoubleCheck.provider(new SwitchingProvider<LocationService>(singletonCImpl, 7));
      this.settingsDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<SettingsDataStore>(singletonCImpl, 9));
      this.provideDataPointTemplateDaoProvider = DoubleCheck.provider(new SwitchingProvider<DataPointTemplateDao>(singletonCImpl, 11));
      this.dataPointTemplateRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<DataPointTemplateRepository>(singletonCImpl, 10));
    }

    @Override
    public void injectHumanGameStatsApp(HumanGameStatsApp humanGameStatsApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.humangamestats.data.repository.StatCategoryRepository 
          return (T) new StatCategoryRepository(singletonCImpl.provideStatCategoryDaoProvider.get(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 1: // com.humangamestats.data.database.dao.StatCategoryDao 
          return (T) DatabaseModule_ProvideStatCategoryDaoFactory.provideStatCategoryDao(singletonCImpl.provideAppDatabaseProvider.get());

          case 2: // com.humangamestats.data.database.AppDatabase 
          return (T) DatabaseModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.humangamestats.data.repository.StatRepository 
          return (T) new StatRepository(singletonCImpl.provideStatDaoProvider.get(), singletonCImpl.provideStatRecordDaoProvider.get(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 4: // com.humangamestats.data.database.dao.StatDao 
          return (T) DatabaseModule_ProvideStatDaoFactory.provideStatDao(singletonCImpl.provideAppDatabaseProvider.get());

          case 5: // com.humangamestats.data.database.dao.StatRecordDao 
          return (T) DatabaseModule_ProvideStatRecordDaoFactory.provideStatRecordDao(singletonCImpl.provideAppDatabaseProvider.get());

          case 6: // com.humangamestats.data.repository.StatRecordRepository 
          return (T) new StatRecordRepository(singletonCImpl.provideStatRecordDaoProvider.get(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 7: // com.humangamestats.data.service.LocationService 
          return (T) new LocationService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideFusedLocationProviderClientProvider.get());

          case 8: // com.google.android.gms.location.FusedLocationProviderClient 
          return (T) AppModule_ProvideFusedLocationProviderClientFactory.provideFusedLocationProviderClient(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.humangamestats.data.datastore.SettingsDataStore 
          return (T) new SettingsDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.humangamestats.data.repository.DataPointTemplateRepository 
          return (T) new DataPointTemplateRepository(singletonCImpl.provideDataPointTemplateDaoProvider.get());

          case 11: // com.humangamestats.data.database.dao.DataPointTemplateDao 
          return (T) DatabaseModule_ProvideDataPointTemplateDaoFactory.provideDataPointTemplateDao(singletonCImpl.provideAppDatabaseProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
