package com.example.tasktracker001.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
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
public class ProjectDao_Impl(
  __db: RoomDatabase,
) : ProjectDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProject: EntityInsertAdapter<Project>

  private val __deleteAdapterOfProject: EntityDeleteOrUpdateAdapter<Project>

  private val __updateAdapterOfProject: EntityDeleteOrUpdateAdapter<Project>
  init {
    this.__db = __db
    this.__insertAdapterOfProject = object : EntityInsertAdapter<Project>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `projects` (`id`,`name`,`description`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Project) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.description)
      }
    }
    this.__deleteAdapterOfProject = object : EntityDeleteOrUpdateAdapter<Project>() {
      protected override fun createQuery(): String = "DELETE FROM `projects` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Project) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfProject = object : EntityDeleteOrUpdateAdapter<Project>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `projects` SET `id` = ?,`name` = ?,`description` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Project) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.description)
        statement.bindLong(4, entity.id.toLong())
      }
    }
  }

  public override suspend fun insert(project: Project): Unit = performSuspending(__db, false, true)
      { _connection ->
    __insertAdapterOfProject.insert(_connection, project)
  }

  public override suspend fun delete(project: Project): Unit = performSuspending(__db, false, true)
      { _connection ->
    __deleteAdapterOfProject.handle(_connection, project)
  }

  public override suspend fun update(project: Project): Unit = performSuspending(__db, false, true)
      { _connection ->
    __updateAdapterOfProject.handle(_connection, project)
  }

  public override fun getAllProjects(): Flow<List<Project>> {
    val _sql: String = "SELECT * FROM projects"
    return createFlow(__db, false, arrayOf("projects")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: MutableList<Project> = mutableListOf()
        while (_stmt.step()) {
          val _item: Project
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          _item = Project(_tmpId,_tmpName,_tmpDescription)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getProjectById(projectId: Int): Project? {
    val _sql: String = "SELECT * FROM projects WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, projectId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: Project?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          _result = Project(_tmpId,_tmpName,_tmpDescription)
        } else {
          _result = null
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
