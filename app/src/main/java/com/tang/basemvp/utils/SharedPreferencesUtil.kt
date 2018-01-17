package com.tang.basemvp.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Base64
import java.io.*

/**
 * Created by lfh on 2016/8/13.
 */
class SharedPreferencesUtil private constructor() {
    lateinit var context: Context
    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    val all: Map<String, *>
        get() = this.prefs.all


    fun getBoolean(key: String, defaultVal: Boolean): Boolean {
        return this.prefs.getBoolean(key, defaultVal)
    }

    fun getBoolean(key: String): Boolean {
        return this.prefs.getBoolean(key, false)
    }


    fun getString(key: String, defaultVal: String): String? {
        return this.prefs.getString(key, defaultVal)
    }

    fun getString(key: String): String? {
        return this.prefs.getString(key, null)
    }

    fun getInt(key: String, defaultVal: Int): Int {
        return this.prefs.getInt(key, defaultVal)
    }

    fun getInt(key: String): Int {
        return this.prefs.getInt(key, 0)
    }


    fun getFloat(key: String, defaultVal: Float): Float {
        return this.prefs.getFloat(key, defaultVal)
    }

    fun getFloat(key: String): Float {
        return this.prefs.getFloat(key, 0f)
    }

    fun getLong(key: String, defaultVal: Long): Long {
        return this.prefs.getLong(key, defaultVal)
    }

    fun getLong(key: String): Long {
        return this.prefs.getLong(key, 0L)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String, defaultVal: Set<String>): Set<String>? {
        return this.prefs.getStringSet(key, defaultVal)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String): Set<String>? {
        return this.prefs.getStringSet(key, null)
    }

    fun exists(key: String): Boolean {
        return prefs.contains(key)
    }


    fun putString(key: String, value: String): SharedPreferencesUtil {
        editor.putString(key, value)
        editor.commit()
        return this
    }

    fun putInt(key: String, value: Int): SharedPreferencesUtil {
        editor.putInt(key, value)
        editor.commit()
        return this
    }

    fun putFloat(key: String, value: Float): SharedPreferencesUtil {
        editor.putFloat(key, value)
        editor.commit()
        return this
    }

    fun putLong(key: String, value: Long): SharedPreferencesUtil {
        editor.putLong(key, value)
        editor.commit()
        return this
    }

    fun putBoolean(key: String, value: Boolean): SharedPreferencesUtil {
        editor.putBoolean(key, value)
        editor.commit()
        return this
    }

    fun commit() {
        editor.commit()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(key: String, value: Set<String>): SharedPreferencesUtil {
        editor.putStringSet(key, value)
        editor.commit()
        return this
    }

    fun putObject(key: String, `object`: Any) {
        val baos = ByteArrayOutputStream()
        var out: ObjectOutputStream? = null
        try {
            out = ObjectOutputStream(baos)
            out.writeObject(`object`)
            val objectVal = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
            editor.putString(key, objectVal)
            editor.commit()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                baos?.close()
                if (out != null) {
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        if (prefs.contains(key)) {
            val objectVal = prefs.getString(key, null)
            val buffer = Base64.decode(objectVal, Base64.DEFAULT)
            val bais = ByteArrayInputStream(buffer)
            var ois: ObjectInputStream? = null
            try {
                ois = ObjectInputStream(bais)
                return ois.readObject() as T
            } catch (e: StreamCorruptedException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } finally {
                try {
                    bais?.close()
                    if (ois != null) {
                        ois.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    fun remove(key: String): SharedPreferencesUtil {
        editor.remove(key)
        editor.commit()
        return this
    }

    fun removeAll(): SharedPreferencesUtil {
        editor.clear()
        editor.commit()
        return this
    }

    companion object {

        @get:Synchronized
        var instance: SharedPreferencesUtil? = null
        fun init(context: Context, prefsname: String, mode: Int) {
            instance = SharedPreferencesUtil()
            instance!!.context = context
            instance!!.prefs = instance!!.context.getSharedPreferences(prefsname, mode)
            instance!!.editor = instance!!.prefs.edit()
        }
    }
}
