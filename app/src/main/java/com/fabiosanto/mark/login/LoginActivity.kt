package com.fabiosanto.mark.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fabiosanto.mark.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginContract.View
{
	override fun closeLogin()
	{
		finish()
	}

	override fun loginError(errorMessage: String)
	{
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		val loginPresenter = LoginPresenter(view = this)

		login.setOnClickListener {
			loginPresenter.login(username = email.text.toString(), password = password.text.toString())
		}
	}
}
