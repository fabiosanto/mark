package com.fabiosanto.mark

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * @author Fabio Santo
 */
object API
{
	private val FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")

	private var client = OkHttpClient()

	private fun get(url: String, map: Map<String, String>, onSuccess: (Response?) -> Unit)
	{
		val queryString = StringBuilder()
		queryString.append(url)
		queryString.append("?")
		map.keys.forEach {
			queryString.append("$it=${map[it]}&")
		}

		val request = Request.Builder()
				.url(queryString.toString())
				.addHeader("Authorization", "Bearer ${Prefs.getString("token")}")
				.get().build()

		client.newCall(request).enqueue(object : Callback
		{
			override fun onFailure(call: Call?, e: IOException?)
			{
				Log.e("API", e?.message)
			}

			override fun onResponse(call: Call?, response: Response?)
			{
				onSuccess.invoke(response)
			}

		})
	}

	private fun post(url: String, map: Map<String, String>, onSuccess: (Response?) -> Unit, onError: (IOException?) -> Unit)
	{
		val formBody = FormBody.Builder().apply {
			map.forEach {
				add(it.key, it.value)
			}
		}.build()

		val request = Request.Builder()
				.url(url)
				.post(formBody).build()

		client.newCall(request).enqueue(object : Callback
		{
			override fun onFailure(call: Call?, e: IOException?)
			{
				onError.invoke(e)
			}

			override fun onResponse(call: Call?, response: Response?)
			{
				onSuccess.invoke(response)
			}
		})
	}

	fun login(user: String, pwd: String, callback: (String?) -> Unit)
	{
		post("https://api.safetyculture.io/auth", HashMap<String, String>().apply {
			put("username", user)
			put("password", pwd)
			put("grant_type", "password")
		}, {
			val jsonData = it?.body()?.string()
			val token = JSONObject(jsonData).getString("access_token")
			callback.invoke(token)
		}, { Log.e("API", it?.message) })
	}

	fun searchAudits(timeFrame: Pair<String, String>, templateId: String)
	{
		get("https://api.safetyculture.io/audits/search", HashMap<String, String>().apply {
//			put("modified_before", timeFrame.second)
//			put("modified_after", timeFrame.first)
//			put("template", templateId)
		}, {
			Log.e("API", it?.body()?.string())
		})
	}

	fun searchTemplates(function: (JSONArray) -> Unit)
	{
		get("https://api.safetyculture.io/templates/search", HashMap<String, String>().apply {
		}, {
			//			Log.e("API", it?.body()?.string())
			val string = it?.body()?.string()
			val jsonArray = JSONObject(string).getJSONArray("templates")
			function.invoke(jsonArray)
		})
	}
}