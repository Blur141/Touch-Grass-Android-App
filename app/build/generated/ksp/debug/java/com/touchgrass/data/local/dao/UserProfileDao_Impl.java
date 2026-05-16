package com.touchgrass.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.touchgrass.data.local.entity.UserProfileEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserProfileDao_Impl implements UserProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProfileEntity> __insertionAdapterOfUserProfileEntity;

  private final SharedSQLiteStatement __preparedStmtOfAddXp;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStreak;

  private final SharedSQLiteStatement __preparedStmtOfIncrementChallengesAndXp;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePetStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTouchGrassScore;

  private final SharedSQLiteStatement __preparedStmtOfUpdateName;

  private final SharedSQLiteStatement __preparedStmtOfAddFocusMinutes;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastActiveDate;

  public UserProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProfileEntity = new EntityInsertionAdapter<UserProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_profile` (`id`,`name`,`xp`,`level`,`streak`,`longestStreak`,`totalChallengesCompleted`,`totalFocusMinutes`,`dailyGoalMinutes`,`joinDate`,`lastActiveDate`,`touchGrassScore`,`petHealthPoints`,`petLevel`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getXp());
        statement.bindLong(4, entity.getLevel());
        statement.bindLong(5, entity.getStreak());
        statement.bindLong(6, entity.getLongestStreak());
        statement.bindLong(7, entity.getTotalChallengesCompleted());
        statement.bindLong(8, entity.getTotalFocusMinutes());
        statement.bindLong(9, entity.getDailyGoalMinutes());
        statement.bindLong(10, entity.getJoinDate());
        statement.bindLong(11, entity.getLastActiveDate());
        statement.bindLong(12, entity.getTouchGrassScore());
        statement.bindLong(13, entity.getPetHealthPoints());
        statement.bindLong(14, entity.getPetLevel());
      }
    };
    this.__preparedStmtOfAddXp = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET xp = xp + ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStreak = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET streak = ?, longestStreak = MAX(longestStreak, ?) WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementChallengesAndXp = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET totalChallengesCompleted = totalChallengesCompleted + 1, xp = xp + ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePetStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET petHealthPoints = ?, petLevel = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTouchGrassScore = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET touchGrassScore = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateName = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET name = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfAddFocusMinutes = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET totalFocusMinutes = totalFocusMinutes + ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastActiveDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET lastActiveDate = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object upsertProfile(final UserProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addXp(final int amount, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddXp.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, amount);
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
          __preparedStmtOfAddXp.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStreak(final int streak, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStreak.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, streak);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, streak);
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
          __preparedStmtOfUpdateStreak.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementChallengesAndXp(final int xp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementChallengesAndXp.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, xp);
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
          __preparedStmtOfIncrementChallengesAndXp.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePetStatus(final int health, final int level,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePetStatus.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, health);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, level);
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
          __preparedStmtOfUpdatePetStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTouchGrassScore(final int score,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTouchGrassScore.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, score);
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
          __preparedStmtOfUpdateTouchGrassScore.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateName(final String name, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateName.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, name);
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
          __preparedStmtOfUpdateName.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object addFocusMinutes(final int minutes, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddFocusMinutes.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, minutes);
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
          __preparedStmtOfAddFocusMinutes.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastActiveDate(final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastActiveDate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
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
          __preparedStmtOfUpdateLastActiveDate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserProfileEntity> observeProfile() {
    final String _sql = "SELECT * FROM user_profile WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_profile"}, new Callable<UserProfileEntity>() {
      @Override
      @Nullable
      public UserProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfTotalChallengesCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChallengesCompleted");
          final int _cursorIndexOfTotalFocusMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFocusMinutes");
          final int _cursorIndexOfDailyGoalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyGoalMinutes");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfLastActiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastActiveDate");
          final int _cursorIndexOfTouchGrassScore = CursorUtil.getColumnIndexOrThrow(_cursor, "touchGrassScore");
          final int _cursorIndexOfPetHealthPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "petHealthPoints");
          final int _cursorIndexOfPetLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "petLevel");
          final UserProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpStreak;
            _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final int _tmpTotalChallengesCompleted;
            _tmpTotalChallengesCompleted = _cursor.getInt(_cursorIndexOfTotalChallengesCompleted);
            final int _tmpTotalFocusMinutes;
            _tmpTotalFocusMinutes = _cursor.getInt(_cursorIndexOfTotalFocusMinutes);
            final int _tmpDailyGoalMinutes;
            _tmpDailyGoalMinutes = _cursor.getInt(_cursorIndexOfDailyGoalMinutes);
            final long _tmpJoinDate;
            _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            final long _tmpLastActiveDate;
            _tmpLastActiveDate = _cursor.getLong(_cursorIndexOfLastActiveDate);
            final int _tmpTouchGrassScore;
            _tmpTouchGrassScore = _cursor.getInt(_cursorIndexOfTouchGrassScore);
            final int _tmpPetHealthPoints;
            _tmpPetHealthPoints = _cursor.getInt(_cursorIndexOfPetHealthPoints);
            final int _tmpPetLevel;
            _tmpPetLevel = _cursor.getInt(_cursorIndexOfPetLevel);
            _result = new UserProfileEntity(_tmpId,_tmpName,_tmpXp,_tmpLevel,_tmpStreak,_tmpLongestStreak,_tmpTotalChallengesCompleted,_tmpTotalFocusMinutes,_tmpDailyGoalMinutes,_tmpJoinDate,_tmpLastActiveDate,_tmpTouchGrassScore,_tmpPetHealthPoints,_tmpPetLevel);
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
  public Object getProfile(final Continuation<? super UserProfileEntity> $completion) {
    final String _sql = "SELECT * FROM user_profile WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserProfileEntity>() {
      @Override
      @Nullable
      public UserProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfTotalChallengesCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChallengesCompleted");
          final int _cursorIndexOfTotalFocusMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFocusMinutes");
          final int _cursorIndexOfDailyGoalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyGoalMinutes");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfLastActiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastActiveDate");
          final int _cursorIndexOfTouchGrassScore = CursorUtil.getColumnIndexOrThrow(_cursor, "touchGrassScore");
          final int _cursorIndexOfPetHealthPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "petHealthPoints");
          final int _cursorIndexOfPetLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "petLevel");
          final UserProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpStreak;
            _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final int _tmpTotalChallengesCompleted;
            _tmpTotalChallengesCompleted = _cursor.getInt(_cursorIndexOfTotalChallengesCompleted);
            final int _tmpTotalFocusMinutes;
            _tmpTotalFocusMinutes = _cursor.getInt(_cursorIndexOfTotalFocusMinutes);
            final int _tmpDailyGoalMinutes;
            _tmpDailyGoalMinutes = _cursor.getInt(_cursorIndexOfDailyGoalMinutes);
            final long _tmpJoinDate;
            _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            final long _tmpLastActiveDate;
            _tmpLastActiveDate = _cursor.getLong(_cursorIndexOfLastActiveDate);
            final int _tmpTouchGrassScore;
            _tmpTouchGrassScore = _cursor.getInt(_cursorIndexOfTouchGrassScore);
            final int _tmpPetHealthPoints;
            _tmpPetHealthPoints = _cursor.getInt(_cursorIndexOfPetHealthPoints);
            final int _tmpPetLevel;
            _tmpPetLevel = _cursor.getInt(_cursorIndexOfPetLevel);
            _result = new UserProfileEntity(_tmpId,_tmpName,_tmpXp,_tmpLevel,_tmpStreak,_tmpLongestStreak,_tmpTotalChallengesCompleted,_tmpTotalFocusMinutes,_tmpDailyGoalMinutes,_tmpJoinDate,_tmpLastActiveDate,_tmpTouchGrassScore,_tmpPetHealthPoints,_tmpPetLevel);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
