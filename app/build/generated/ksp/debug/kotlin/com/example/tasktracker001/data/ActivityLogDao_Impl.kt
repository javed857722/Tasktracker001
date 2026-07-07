package com.example.tasktracker001.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ActivityLogDao_Impl(
  __db: RoomDatabase,
) : ActivityLogDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfActivityLog: EntityInsertAdapter<ActivityLog>
  init {
    this.__db = __db
    this.__insertAdapterOfActivityLog = object : EntityInsertAdapter<ActivityLog>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `activity_logs` (`id`,`taskId`,`userId`,`username`,`action`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ActivityLog) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.taskId.toLong())
        statement.bindLong(3, entity.userId.toLong())
        statement.bindText(4, entity.username)
        statement.bindText(5, entity.action)
        statement.bindLong(6, entity.timestamp)
      }
    }
  }

  public override suspend fun insert(activityLog: ActivityLog): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfActivityLog.insert(_connection, activityLog)
  }

  public override fun getActivityLogsForTask(taskId: Int): Flow<List<ActivityLog>> {
    val _sql: String = "SELECT * FROM activity_logs WHERE taskId = ? ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, taskId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _cursorIndexOfAction: Int = getColumnIndexOrThrow(_stmt, "action")
        val _cursorIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<ActivityLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLog
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpTaskId: Int
          _tmpTaskId = _stmt.getLong(_cursorIndexOfTaskId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_cursorIndexOfUsername)
          val _tmpAction: String
          _tmpAction = _stmt.getText(_cursorIndexOfAction)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_cursorIndexOfTimestamp)
          _item = ActivityLog(_tmpId,_tmpTaskId,_tmpUserId,_tmpUsername,_tmpAction,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
