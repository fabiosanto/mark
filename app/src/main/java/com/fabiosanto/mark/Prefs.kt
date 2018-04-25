package com.fabiosanto.mark

import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import android.preference.PreferenceManager
import java.util.*
import org.json.JSONArray


/**
 * @author Fabio Santo
 */
object Prefs
{
	private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MarkApplication.app)!!

	fun saveString(key: String, value: String)
	{
		defaultSharedPreferences.edit().putString(key, value).apply()
	}

	fun getString(key: String): String
	{
		return defaultSharedPreferences.getString(key, "")
	}

	fun addReportPage(filters: HashMap<String, String>)
	{
		val uuid = UUID.randomUUID()
		val jsonArray = JSONArray(defaultSharedPreferences.getString("reports", "[]"))
		jsonArray.put(uuid)
		defaultSharedPreferences.edit()?.putString("reports", jsonArray.toString())?.apply()

		val reportsPrefs = MarkApplication.app.getSharedPreferences(uuid.toString(), Context.MODE_PRIVATE)

		with(reportsPrefs?.edit()) {
			filters.keys.forEach {
				this?.putString(it, filters[it])
			}
			this?.apply()
		}
	}

	fun removeReport(index: Int)
	{
		val jsonArray = JSONArray(defaultSharedPreferences.getString("reports", "[]"))
		jsonArray.remove(index)
		defaultSharedPreferences.edit()?.putString("reports", jsonArray.toString())?.apply()
	}

	fun getReports() = JSONArray(defaultSharedPreferences.getString("reports", "[]"))

	fun getReportsFilters(uuid: String) = MarkApplication.app.getSharedPreferences(uuid, Context.MODE_PRIVATE).all
}