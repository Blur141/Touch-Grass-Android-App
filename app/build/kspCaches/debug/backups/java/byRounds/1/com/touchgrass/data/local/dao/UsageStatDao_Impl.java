package com.touchgrass.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.touchgrass.data.local.entity.AppUsageEntity;
import com.touchgrass.data.local.entity.DailyUsageSummaryEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class UsageStatDao_Impl implements UsageStatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailyUsageSummaryEntity> __insertionAdapterOfDailyUsageSummaryEntity;

  private final EntityInsertionAdapter<AppUsageEntity> __insertionAdapterOfAppUsageEntity;

  public UsageStatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailyUsageSummaryEntity = new EntityInsertionAdapter<DailyUsageSummaryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_usage_summary` (`dateEpochDay`,`totalScreenTimeMs`,`unlockCount`,`longestSessionMs`,`dailyGoalMs`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyUsageSummaryEntity entity) {
        statement.bindLong(1, entity.getDateEpochDay());
        statement.bindLong(2, entity.getTotalScreenTimeMs());
        statement.bindLong(3, entity.getUnlockCount());
        statement.bindLong(4, entity.getLongestSessionMs());
        statement.bindLong(5, entity.getDailyGoalMs());
      }
    };
    this.__insertionAdapterOfAppUsageEntity = new EntityInsertionAdapter<AppUsageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_usage` (`packageName`,`appName`,`totalTimeMs`,`launchCount`,`lastUsed`,`dateEpochDay`,`category`,`isBlocked`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppUsageEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        statement.bindLong(3, entity.getTotalTimeMs());
        statement.bindLong(4, entity.getLaunchCount());
        statement.bindLong(5, entity.getLastUsed());
        statement.bindLong(6, entity.getDateEpochDay());
        statement.bindString(7, entity.getCategory());
        final int _tmp = entity.isBlocked() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
  }

  @Override
  public Object upsertDailySummary(final DailyUsageSummaryEntity summary,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyUsageSummaryEntity.insert(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAppUsages(final List<AppUsageEntity> usages,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppUsageEntity.insert(usages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<DailyUsageSummaryEntity> observeDailySummary(final long dateEpochDay) {
    final String _sql = "SELECT * FROM daily_usage_summary WHERE dateEpochDay = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dateEpochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_usage_summary"}, new Callable<DailyUsageSummaryEntity>() {
      @Override
      @Nullable
      public DailyUsageSummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfTotalScreenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScreenTimeMs");
          final int _cursorIndexOfUnlockCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockCount");
          final int _cursorIndexOfLongestSessionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMs");
          final int _cursorIndexOfDailyGoalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyGoalMs");
          final DailyUsageSummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final long _tmpTotalScreenTimeMs;
            _tmpTotalScreenTimeMs = _cursor.getLong(_cursorIndexOfTotalScreenTimeMs);
            final int _tmpUnlockCount;
            _tmpUnlockCount = _cursor.getInt(_cursorIndexOfUnlockCount);
            final long _tmpLongestSessionMs;
            _tmpLongestSessionMs = _cursor.getLong(_cursorIndexOfLongestSessionMs);
            final long _tmpDailyGoalMs;
            _tmpDailyGoalMs = _cursor.getLong(_cursorIndexOfDailyGoalMs);
            _result = new DailyUsageSummaryEntity(_tmpDateEpochDay,_tmpTotalScreenTimeMs,_tmpUnlockCount,_tmpLongestSessionMs,_tmpDailyGoalMs);
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
  public Flow<List<DailyUsageSummaryEntity>> observeRecentSummaries(final int limit) {
    final String _sql = "SELECT * FROM daily_usage_summary ORDER BY dateEpochDay DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_usage_summary"}, new Callable<List<DailyUsageSummaryEntity>>() {
      @Override
      @NonNull
      public List<DailyUsageSummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfTotalScreenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScreenTimeMs");
          final int _cursorIndexOfUnlockCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockCount");
          final int _cursorIndexOfLongestSessionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMs");
          final int _cursorIndexOfDailyGoalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyGoalMs");
          final List<DailyUsageSummaryEntity> _result = new ArrayList<DailyUsageSummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyUsageSummaryEntity _item;
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final long _tmpTotalScreenTimeMs;
            _tmpTotalScreenTimeMs = _cursor.getLong(_cursorIndexOfTotalScreenTimeMs);
            final int _tmpUnlockCount;
            _tmpUnlockCount = _cursor.getInt(_cursorIndexOfUnlockCount);
            final long _tmpLongestSessionMs;
            _tmpLongestSessionMs = _cursor.getLong(_cursorIndexOfLongestSessionMs);
            final long _tmpDailyGoalMs;
            _tmpDailyGoalMs = _cursor.getLong(_cursorIndexOfDailyGoalMs);
            _item = new DailyUsageSummaryEntity(_tmpDateEpochDay,_tmpTotalScreenTimeMs,_tmpUnlockCount,_tmpLongestSessionMs,_tmpDailyGoalMs);
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
  public Flow<List<AppUsageEntity>> observeAppUsageForDay(final long dateEpochDay) {
    final String _sql = "SELECT * FROM app_usage WHERE dateEpochDay = ? ORDER BY totalTimeMs DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dateEpochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_usage"}, new Callable<List<AppUsageEntity>>() {
      @Override
      @NonNull
      public List<AppUsageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfTotalTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeMs");
          final int _cursorIndexOfLaunchCount = CursorUtil.getColumnIndexOrThrow(_cursor, "launchCount");
          final int _cursorIndexOfLastUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsed");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsBlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlocked");
          final List<AppUsageEntity> _result = new ArrayList<AppUsageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppUsageEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpTotalTimeMs;
            _tmpTotalTimeMs = _cursor.getLong(_cursorIndexOfTotalTimeMs);
            final int _tmpLaunchCount;
            _tmpLaunchCount = _cursor.getInt(_cursorIndexOfLaunchCount);
            final long _tmpLastUsed;
            _tmpLastUsed = _cursor.getLong(_cursorIndexOfLastUsed);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final boolean _tmpIsBlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBlocked);
            _tmpIsBlocked = _tmp != 0;
            _item = new AppUsageEntity(_tmpPackageName,_tmpAppName,_tmpTotalTimeMs,_tmpLaunchCount,_tmpLastUsed,_tmpDateEpochDay,_tmpCategory,_tmpIsBlocked);
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
  public Object getAppUsageForDay(final long dateEpochDay,
      final Continuation<? super List<AppUsageEntity>> $completion) {
    final String _sql = "SELECT * FROM app_usage WHERE dateEpochDay = ? ORDER BY totalTimeMs DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dateEpochDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AppUsageEntity>>() {
      @Override
      @NonNull
      public List<AppUsageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfTotalTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeMs");
          final int _cursorIndexOfLaunchCount = CursorUtil.getColumnIndexOrThrow(_cursor, "launchCount");
          final int _cursorIndexOfLastUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUsed");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsBlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlocked");
          final List<AppUsageEntity> _result = new ArrayList<AppUsageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppUsageEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpTotalTimeMs;
            _tmpTotalTimeMs = _cursor.getLong(_cursorIndexOfTotalTimeMs);
            final int _tmpLaunchCount;
            _tmpLaunchCount = _cursor.getInt(_cursorIndexOfLaunchCount);
            final long _tmpLastUsed;
            _tmpLastUsed = _cursor.getLong(_cursorIndexOfLastUsed);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final boolean _tmpIsBlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBlocked);
            _tmpIsBlocked = _tmp != 0;
            _item = new AppUsageEntity(_tmpPackageName,_tmpAppName,_tmpTotalTimeMs,_tmpLaunchCount,_tmpLastUsed,_tmpDateEpochDay,_tmpCategory,_tmpIsBlocked);
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
  public Object getTotalScreenTimeInRange(final long startDay, final long endDay,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(totalScreenTimeMs) FROM daily_usage_summary WHERE dateEpochDay >= ? AND dateEpochDay <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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
  public Flow<List<DailyUsageSummaryEntity>> observeSummariesSince(final long startDay) {
    final String _sql = "SELECT * FROM daily_usage_summary WHERE dateEpochDay >= ? ORDER BY dateEpochDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_usage_summary"}, new Callable<List<DailyUsageSummaryEntity>>() {
      @Override
      @NonNull
      public List<DailyUsageSummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfTotalScreenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScreenTimeMs");
          final int _cursorIndexOfUnlockCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockCount");
          final int _cursorIndexOfLongestSessionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMs");
          final int _cursorIndexOfDailyGoalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyGoalMs");
          final List<DailyUsageSummaryEntity> _result = new ArrayList<DailyUsageSummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyUsageSummaryEntity _item;
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final long _tmpTotalScreenTimeMs;
            _tmpTotalScreenTimeMs = _cursor.getLong(_cursorIndexOfTotalScreenTimeMs);
            final int _tmpUnlockCount;
            _tmpUnlockCount = _cursor.getInt(_cursorIndexOfUnlockCount);
            final long _tmpLongestSessionMs;
            _tmpLongestSessionMs = _cursor.getLong(_cursorIndexOfLongestSessionMs);
            final long _tmpDailyGoalMs;
            _tmpDailyGoalMs = _cursor.getLong(_cursorIndexOfDailyGoalMs);
            _item = new DailyUsageSummaryEntity(_tmpDateEpochDay,_tmpTotalScreenTimeMs,_tmpUnlockCount,_tmpLongestSessionMs,_tmpDailyGoalMs);
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
}
