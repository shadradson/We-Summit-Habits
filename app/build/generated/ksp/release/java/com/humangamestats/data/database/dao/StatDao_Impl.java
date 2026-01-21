package com.humangamestats.data.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.humangamestats.data.database.entity.StatEntity;
import com.humangamestats.model.StatType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StatDao_Impl implements StatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StatEntity> __insertionAdapterOfStatEntity;

  private final EntityDeletionOrUpdateAdapter<StatEntity> __deletionAdapterOfStatEntity;

  private final EntityDeletionOrUpdateAdapter<StatEntity> __updateAdapterOfStatEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStatById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStatsByCategory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllStats;

  public StatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStatEntity = new EntityInsertionAdapter<StatEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `stats` (`id`,`category_id`,`name`,`data_points_json`,`template_id`,`stat_type`,`type_label`,`sort_order`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StatEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getDataPointsJson());
        if (entity.getTemplateId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getTemplateId());
        }
        statement.bindString(6, __StatType_enumToString(entity.getStatType()));
        statement.bindString(7, entity.getTypeLabel());
        statement.bindLong(8, entity.getSortOrder());
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfStatEntity = new EntityDeletionOrUpdateAdapter<StatEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `stats` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StatEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfStatEntity = new EntityDeletionOrUpdateAdapter<StatEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `stats` SET `id` = ?,`category_id` = ?,`name` = ?,`data_points_json` = ?,`template_id` = ?,`stat_type` = ?,`type_label` = ?,`sort_order` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StatEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getDataPointsJson());
        if (entity.getTemplateId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getTemplateId());
        }
        statement.bindString(6, __StatType_enumToString(entity.getStatType()));
        statement.bindString(7, entity.getTypeLabel());
        statement.bindLong(8, entity.getSortOrder());
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteStatById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM stats WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteStatsByCategory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM stats WHERE category_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllStats = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM stats";
        return _query;
      }
    };
  }

  @Override
  public Object insertStat(final StatEntity stat, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfStatEntity.insertAndReturnId(stat);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertStats(final List<StatEntity> stats,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfStatEntity.insertAndReturnIdsList(stats);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStat(final StatEntity stat, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfStatEntity.handle(stat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStat(final StatEntity stat, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStatEntity.handle(stat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStatById(final long statId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStatById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, statId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteStatById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStatsByCategory(final long categoryId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStatsByCategory.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, categoryId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteStatsByCategory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllStats(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllStats.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllStats.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StatEntity>> getStatsByCategory(final long categoryId) {
    final String _sql = "SELECT * FROM stats WHERE category_id = ? ORDER BY sort_order ASC, created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stats"}, new Callable<List<StatEntity>>() {
      @Override
      @NonNull
      public List<StatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<StatEntity> _result = new ArrayList<StatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllStats(final Continuation<? super List<StatEntity>> $completion) {
    final String _sql = "SELECT * FROM stats ORDER BY category_id ASC, sort_order ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StatEntity>>() {
      @Override
      @NonNull
      public List<StatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<StatEntity> _result = new ArrayList<StatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StatEntity>> getAllStatsFlow() {
    final String _sql = "SELECT * FROM stats ORDER BY category_id ASC, sort_order ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stats"}, new Callable<List<StatEntity>>() {
      @Override
      @NonNull
      public List<StatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<StatEntity> _result = new ArrayList<StatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStatById(final long statId, final Continuation<? super StatEntity> $completion) {
    final String _sql = "SELECT * FROM stats WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, statId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StatEntity>() {
      @Override
      @Nullable
      public StatEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final StatEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<StatEntity> getStatByIdFlow(final long statId) {
    final String _sql = "SELECT * FROM stats WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, statId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stats"}, new Callable<StatEntity>() {
      @Override
      @Nullable
      public StatEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final StatEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStatCountByCategory(final long categoryId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM stats WHERE category_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalStatCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM stats";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMaxSortOrderInCategory(final long categoryId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(MAX(sort_order), 0) FROM stats WHERE category_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StatEntity>> searchStatsInCategory(final long categoryId, final String query) {
    final String _sql = "SELECT * FROM stats WHERE category_id = ? AND name LIKE '%' || ? || '%' ORDER BY sort_order ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stats"}, new Callable<List<StatEntity>>() {
      @Override
      @NonNull
      public List<StatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<StatEntity> _result = new ArrayList<StatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<StatEntity>> getStatsByCategorySortedByRecentRecord(final long categoryId) {
    final String _sql = "\n"
            + "        SELECT s.* FROM stats s\n"
            + "        LEFT JOIN (\n"
            + "            SELECT stat_id, MAX(recorded_at) as latestRecord\n"
            + "            FROM stat_records\n"
            + "            GROUP BY stat_id\n"
            + "        ) r ON s.id = r.stat_id\n"
            + "        WHERE s.category_id = ?\n"
            + "        ORDER BY CASE WHEN r.latestRecord IS NULL THEN 1 ELSE 0 END, r.latestRecord DESC, s.created_at DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stats",
        "stat_records"}, new Callable<List<StatEntity>>() {
      @Override
      @NonNull
      public List<StatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<StatEntity> _result = new ArrayList<StatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<StatEntity>> getStatsByCategorySortedAlphabetically(final long categoryId) {
    final String _sql = "SELECT * FROM stats WHERE category_id = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stats"}, new Callable<List<StatEntity>>() {
      @Override
      @NonNull
      public List<StatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDataPointsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "data_points_json");
          final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
          final int _cursorIndexOfStatType = CursorUtil.getColumnIndexOrThrow(_cursor, "stat_type");
          final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sort_order");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<StatEntity> _result = new ArrayList<StatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDataPointsJson;
            _tmpDataPointsJson = _cursor.getString(_cursorIndexOfDataPointsJson);
            final Long _tmpTemplateId;
            if (_cursor.isNull(_cursorIndexOfTemplateId)) {
              _tmpTemplateId = null;
            } else {
              _tmpTemplateId = _cursor.getLong(_cursorIndexOfTemplateId);
            }
            final StatType _tmpStatType;
            _tmpStatType = __StatType_stringToEnum(_cursor.getString(_cursorIndexOfStatType));
            final String _tmpTypeLabel;
            _tmpTypeLabel = _cursor.getString(_cursorIndexOfTypeLabel);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StatEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpDataPointsJson,_tmpTemplateId,_tmpStatType,_tmpTypeLabel,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __StatType_enumToString(@NonNull final StatType _value) {
    switch (_value) {
      case NUMBER: return "NUMBER";
      case DURATION: return "DURATION";
      case RATING: return "RATING";
      case CHECKBOX: return "CHECKBOX";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private StatType __StatType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "NUMBER": return StatType.NUMBER;
      case "DURATION": return StatType.DURATION;
      case "RATING": return StatType.RATING;
      case "CHECKBOX": return StatType.CHECKBOX;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
