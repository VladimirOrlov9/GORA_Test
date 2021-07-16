package com.spbstu.goratest.presenters

import com.spbstu.goratest.data.User
import com.spbstu.goratest.models.UsersModel
import com.spbstu.goratest.views.UsersView


class UsersPresenter(private val model: UsersModel) {
    private lateinit var usersView: UsersView

    private var users: ArrayList<User>? = null

    fun attachView(view: UsersView) {
        this.usersView = view
    }

    fun loadUsers() {
        model.loadUsers(object : UsersModel.LoadUsersCallback {

            override fun onComplete(data: ArrayList<User>?) {
                if (data != null) {
                    users = data
                    usersView.updateUsersListView(data.map { it.name })
                }
            }
        })
    }

    fun goToUserCategories(pos: Int) {
        val id = users?.get(pos)?.id
        if (id != null) {
            usersView.openUserCategories(id)
        }
    }
}