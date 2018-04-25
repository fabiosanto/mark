package com.fabiosanto.mark

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView

/**
 * @author Fabio Santo
 */
class ReportFragment : Fragment()
{
	lateinit var uuid: String
	lateinit var dataFilters: MutableMap<String, *>

	override fun onCreate(savedInstanceState: Bundle?)
	{
		uuid = arguments.getString("id")
		dataFilters = Prefs.getReportsFilters(uuid)

		API.searchAudits(Pair("2014-04-01T00:00:00.000Z", "2018-04-01T00:00:00.000Z"), dataFilters["template_id"] as String)
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		return FrameLayout(activity as Context?).apply {
			addView(TextView(activity).apply {
				text = dataFilters["template_id"] as String
				textSize = 40.toFloat()
				setTextColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
			})
		}
	}
}