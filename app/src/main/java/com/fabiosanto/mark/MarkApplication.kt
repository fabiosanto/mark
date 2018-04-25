package com.fabiosanto.mark

import android.app.Application


/**
 * @author Fabio Santo
 */
class MarkApplication : Application()
{
	companion object
	{
		lateinit var app: MarkApplication
	}

	override fun onCreate()
	{
		super.onCreate()
		app = this
	}
}