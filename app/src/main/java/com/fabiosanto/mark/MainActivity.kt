package com.fabiosanto.mark

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import com.fabiosanto.mark.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import android.support.v4.view.PagerAdapter


class MainActivity : AppCompatActivity()
{
	private lateinit var mainPagerAdapter: MainPagerAdapter
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if (Prefs.getString("token").isNullOrEmpty())
			startActivity(Intent(this, LoginActivity::class.java))

		mainPagerAdapter = MainPagerAdapter(supportFragmentManager, Prefs.getReports())
		pager.adapter = mainPagerAdapter

		fab.setOnClickListener {
			NewReportBottomSheet().show(supportFragmentManager, "")
		}
	}

	fun onTemplateClicked(templateId: String)
	{
		Prefs.addReportPage(hashMapOf(Pair("template_id", templateId)))
		mainPagerAdapter.reportsIds = Prefs.getReports()
		mainPagerAdapter.notifyDataSetChanged()
		pager.setCurrentItem(pager.currentItem + 1, true)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean
	{
		menu?.add(0, 0, 0, "Remove")
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean
	{
		mainPagerAdapter.removeCurrent(pager.currentItem)
		return super.onOptionsItemSelected(item)
	}

	class MainPagerAdapter(fm: FragmentManager, var reportsIds: JSONArray) : FragmentStatePagerAdapter(fm)
	{
		override fun getItem(position: Int): Fragment
		{
			return ReportFragment().apply {
				arguments = Bundle().apply {
					putString("id", reportsIds.getString(position))
				}
			}
		}

		override fun getItemPosition(`object`: Any?): Int
		{
			// refresh all fragments when data set changed
			return PagerAdapter.POSITION_NONE
		}

		override fun getCount(): Int
		{
			return reportsIds.length()
		}

		fun removeCurrent(currentItem: Int)
		{
			Prefs.removeReport(currentItem)
			reportsIds.remove(currentItem)
			notifyDataSetChanged()
		}
	}
}
