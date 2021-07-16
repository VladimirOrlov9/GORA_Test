package com.spbstu.goratest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.spbstu.goratest.R
import com.spbstu.goratest.models.UsersModel
import com.spbstu.goratest.presenters.UsersPresenter
import com.spbstu.goratest.views.UsersView

class UsersFragment : Fragment(), UsersView {

    private lateinit var listView: ListView

    private var presenter: UsersPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        init(view)

        return view
    }

    override fun onStart() {
        super.onStart()

        presenter?.loadUsers()
    }

    private fun init(view: View) {
        (activity as AppCompatActivity).supportActionBar?.title = "Users"

        listView = view.findViewById(R.id.users_list)
        listView.setOnItemClickListener { _, _, position, _ ->
            presenter?.goToUserCategories(position)
        }

        val model = UsersModel()
        presenter = UsersPresenter(model)
        presenter?.attachView(this)
    }

    override fun updateUsersListView(list: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list
        )
        listView.adapter = adapter
    }

    override fun openUserCategories(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        Navigation.findNavController(requireView()).navigate(R.id.categoriesFragment, bundle)
    }
}