package com.fabiosanto.mark.login

import com.fabiosanto.mark.API
import com.fabiosanto.mark.Prefs

/**
 * @author Fabio Santo
 */
class LoginContract
{
	interface Model
	{
		fun saveToken(token: String)
	}

	interface View
	{
		fun closeLogin()
		fun loginError(errorMessage: String)
	}
}

class LoginModel : LoginContract.Model
{
	override fun saveToken(token: String)
	{
		Prefs.saveString("token", token)
	}
}

class LoginPresenter(private val model: LoginContract.Model = LoginModel(), val view: LoginContract.View)
{
	fun login(username: String, password: String)
	{
		API.login(username, password, {
			it?.let {
				model.saveToken(it)
				view.closeLogin()
			}
		})
	}
}
