package com.fabiosanto.mark

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.new_report_bottomsheet.*
import org.json.JSONArray
import kotlin.reflect.KFunction1


/**
 * @author Fabio Santo
 */
class NewReportBottomSheet : BottomSheetDialogFragment()
{
	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		val view = inflater?.inflate(R.layout.new_report_bottomsheet, container, false)

		return view
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
	{
		val layoutManager = LinearLayoutManager(activity).apply { orientation = LinearLayoutManager.VERTICAL; }
		rview.layoutManager = layoutManager
		rview.adapter = Adapter(JSONArray(), (activity as MainActivity)::onTemplateClicked)
		progress.visibility = View.VISIBLE

		API.searchTemplates({
			activity.runOnUiThread {
				progress.visibility = View.GONE
				rview.adapter = Adapter(it, (activity as MainActivity)::onTemplateClicked)
			}
		})
		super.onViewCreated(view, savedInstanceState)
	}

	class Adapter(val data: JSONArray, private val onTemplateClicked: (templateId: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
	{
		override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
		{
			return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false), ::onClick)
		}

		fun onClick(position: Int)
		{
			onTemplateClicked.invoke(data.getJSONObject(position).getString("template_id"))
		}

		override fun getItemCount() = data.length()

		override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
		{
			(holder as ViewHolder).bind(data.getJSONObject(position).getString("name"))
		}

		inner class ViewHolder(itemView: View, val onClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener
		{
			override fun onClick(p0: View?)
			{
				onClick.invoke(adapterPosition)
			}

			fun bind(string: String)
			{
				itemView.text.text = string
				itemView.setOnClickListener(this)
			}
		}
	}
}
