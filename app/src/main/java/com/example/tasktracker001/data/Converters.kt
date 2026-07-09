package com.example.tasktracker001.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converters for Room to handle complex data types.
 */
class Converters {
    /**
     * Converts a Priority enum to its string representation.
     */
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    /**
     * Converts a string back to a Priority enum.
     */
    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

    /**
     * Converts a Role enum to its string representation.
     */
    @TypeConverter
    fun fromRole(role: Role): String {
        return role.name
    }

    /**
     * Converts a string back to a Role enum.
     */
    @TypeConverter
    fun toRole(role: String): Role {
        return Role.valueOf(role)
    }

    /**
     * Converts a list of strings to a JSON string representation.
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value ?: emptyList<String>())
    }

    /**
     * Converts a JSON string back to a list of strings.
     */
    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return try {
            Gson().fromJson(value, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
