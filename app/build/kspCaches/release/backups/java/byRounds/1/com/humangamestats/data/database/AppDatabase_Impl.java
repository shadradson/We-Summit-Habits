package com.humangamestats.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.humangamestats.data.database.dao.DataPointTemplateDao;
import com.humangamestats.data.database.dao.DataPointTemplateDao_Impl;
import com.humangamestats.data.database.dao.StatCategoryDao;
import com.humangamestats.data.database.dao.StatCategoryDao_Impl;
import com.humangamestats.data.database.dao.StatDao;
import com.humangamestats.data.database.dao.StatDao_Impl;
import com.humangamestats.data.database.dao.StatRecordDao;
import com.humangamestats.data.database.dao.StatRecordDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile StatCategoryDao _statCategoryDao;

  private volatile StatDao _statDao;

  private volatile StatRecordDao _statRecordDao;

  private volatile DataPointTemplateDao _dataPointTemplateDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `stat_categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `icon` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `stats` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `data_points_json` TEXT NOT NULL, `template_id` INTEGER, `stat_type` TEXT NOT NULL DEFAULT 'NUMBER', `type_label` TEXT NOT NULL DEFAULT 'Value', `sort_order` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`category_id`) REFERENCES `stat_categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_stats_category_id` ON `stats` (`category_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_stats_template_id` ON `stats` (`template_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `stat_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `stat_id` INTEGER NOT NULL, `values_json` TEXT NOT NULL, `value` TEXT NOT NULL DEFAULT '', `notes` TEXT, `latitude` REAL, `longitude` REAL, `location_name` TEXT, `recorded_at` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`stat_id`) REFERENCES `stats`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_stat_records_stat_id` ON `stat_records` (`stat_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_stat_records_recorded_at` ON `stat_records` (`recorded_at`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `data_point_templates` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `data_points_json` TEXT NOT NULL, `is_system` INTEGER NOT NULL, `sort_order` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '796fcf07dbfaaef637af410de5082fab')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `stat_categories`");
        db.execSQL("DROP TABLE IF EXISTS `stats`");
        db.execSQL("DROP TABLE IF EXISTS `stat_records`");
        db.execSQL("DROP TABLE IF EXISTS `data_point_templates`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStatCategories = new HashMap<String, TableInfo.Column>(6);
        _columnsStatCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatCategories.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatCategories.put("icon", new TableInfo.Column("icon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatCategories.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatCategories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatCategories.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStatCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStatCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStatCategories = new TableInfo("stat_categories", _columnsStatCategories, _foreignKeysStatCategories, _indicesStatCategories);
        final TableInfo _existingStatCategories = TableInfo.read(db, "stat_categories");
        if (!_infoStatCategories.equals(_existingStatCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "stat_categories(com.humangamestats.data.database.entity.StatCategoryEntity).\n"
                  + " Expected:\n" + _infoStatCategories + "\n"
                  + " Found:\n" + _existingStatCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsStats = new HashMap<String, TableInfo.Column>(10);
        _columnsStats.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("data_points_json", new TableInfo.Column("data_points_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("template_id", new TableInfo.Column("template_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("stat_type", new TableInfo.Column("stat_type", "TEXT", true, 0, "'NUMBER'", TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("type_label", new TableInfo.Column("type_label", "TEXT", true, 0, "'Value'", TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("sort_order", new TableInfo.Column("sort_order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStats.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStats = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysStats.add(new TableInfo.ForeignKey("stat_categories", "CASCADE", "NO ACTION", Arrays.asList("category_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesStats = new HashSet<TableInfo.Index>(2);
        _indicesStats.add(new TableInfo.Index("index_stats_category_id", false, Arrays.asList("category_id"), Arrays.asList("ASC")));
        _indicesStats.add(new TableInfo.Index("index_stats_template_id", false, Arrays.asList("template_id"), Arrays.asList("ASC")));
        final TableInfo _infoStats = new TableInfo("stats", _columnsStats, _foreignKeysStats, _indicesStats);
        final TableInfo _existingStats = TableInfo.read(db, "stats");
        if (!_infoStats.equals(_existingStats)) {
          return new RoomOpenHelper.ValidationResult(false, "stats(com.humangamestats.data.database.entity.StatEntity).\n"
                  + " Expected:\n" + _infoStats + "\n"
                  + " Found:\n" + _existingStats);
        }
        final HashMap<String, TableInfo.Column> _columnsStatRecords = new HashMap<String, TableInfo.Column>(11);
        _columnsStatRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("stat_id", new TableInfo.Column("stat_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("values_json", new TableInfo.Column("values_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("value", new TableInfo.Column("value", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("location_name", new TableInfo.Column("location_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("recorded_at", new TableInfo.Column("recorded_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatRecords.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStatRecords = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysStatRecords.add(new TableInfo.ForeignKey("stats", "CASCADE", "NO ACTION", Arrays.asList("stat_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesStatRecords = new HashSet<TableInfo.Index>(2);
        _indicesStatRecords.add(new TableInfo.Index("index_stat_records_stat_id", false, Arrays.asList("stat_id"), Arrays.asList("ASC")));
        _indicesStatRecords.add(new TableInfo.Index("index_stat_records_recorded_at", false, Arrays.asList("recorded_at"), Arrays.asList("ASC")));
        final TableInfo _infoStatRecords = new TableInfo("stat_records", _columnsStatRecords, _foreignKeysStatRecords, _indicesStatRecords);
        final TableInfo _existingStatRecords = TableInfo.read(db, "stat_records");
        if (!_infoStatRecords.equals(_existingStatRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "stat_records(com.humangamestats.data.database.entity.StatRecordEntity).\n"
                  + " Expected:\n" + _infoStatRecords + "\n"
                  + " Found:\n" + _existingStatRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsDataPointTemplates = new HashMap<String, TableInfo.Column>(8);
        _columnsDataPointTemplates.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("data_points_json", new TableInfo.Column("data_points_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("is_system", new TableInfo.Column("is_system", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("sort_order", new TableInfo.Column("sort_order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataPointTemplates.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDataPointTemplates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDataPointTemplates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDataPointTemplates = new TableInfo("data_point_templates", _columnsDataPointTemplates, _foreignKeysDataPointTemplates, _indicesDataPointTemplates);
        final TableInfo _existingDataPointTemplates = TableInfo.read(db, "data_point_templates");
        if (!_infoDataPointTemplates.equals(_existingDataPointTemplates)) {
          return new RoomOpenHelper.ValidationResult(false, "data_point_templates(com.humangamestats.data.database.entity.DataPointTemplateEntity).\n"
                  + " Expected:\n" + _infoDataPointTemplates + "\n"
                  + " Found:\n" + _existingDataPointTemplates);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "796fcf07dbfaaef637af410de5082fab", "db23bc79fefc1868c0a56246067bb3f6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "stat_categories","stats","stat_records","data_point_templates");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `stat_categories`");
      _db.execSQL("DELETE FROM `stats`");
      _db.execSQL("DELETE FROM `stat_records`");
      _db.execSQL("DELETE FROM `data_point_templates`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(StatCategoryDao.class, StatCategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StatDao.class, StatDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StatRecordDao.class, StatRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DataPointTemplateDao.class, DataPointTemplateDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public StatCategoryDao statCategoryDao() {
    if (_statCategoryDao != null) {
      return _statCategoryDao;
    } else {
      synchronized(this) {
        if(_statCategoryDao == null) {
          _statCategoryDao = new StatCategoryDao_Impl(this);
        }
        return _statCategoryDao;
      }
    }
  }

  @Override
  public StatDao statDao() {
    if (_statDao != null) {
      return _statDao;
    } else {
      synchronized(this) {
        if(_statDao == null) {
          _statDao = new StatDao_Impl(this);
        }
        return _statDao;
      }
    }
  }

  @Override
  public StatRecordDao statRecordDao() {
    if (_statRecordDao != null) {
      return _statRecordDao;
    } else {
      synchronized(this) {
        if(_statRecordDao == null) {
          _statRecordDao = new StatRecordDao_Impl(this);
        }
        return _statRecordDao;
      }
    }
  }

  @Override
  public DataPointTemplateDao dataPointTemplateDao() {
    if (_dataPointTemplateDao != null) {
      return _dataPointTemplateDao;
    } else {
      synchronized(this) {
        if(_dataPointTemplateDao == null) {
          _dataPointTemplateDao = new DataPointTemplateDao_Impl(this);
        }
        return _dataPointTemplateDao;
      }
    }
  }
}
