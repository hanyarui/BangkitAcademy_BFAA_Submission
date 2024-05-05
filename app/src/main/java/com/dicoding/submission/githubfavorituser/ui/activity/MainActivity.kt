package com.dicoding.submission.githubfavorituser.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission.githubfavorituser.R
import com.dicoding.submission.githubfavorituser.adapter.MainAdapter
import com.dicoding.submission.githubfavorituser.databinding.ActivityMainBinding
import com.dicoding.submission.githubfavorituser.local.model.UserSearch
import com.dicoding.submission.githubfavorituser.local.theme.SettingPreference
import com.dicoding.submission.githubfavorituser.util.Loading
import com.dicoding.submission.githubfavorituser.viewmodel.MainViewModel
import com.dicoding.submission.githubfavorituser.viewmodel.SettingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private val listSearchUsers = ArrayList<UserSearch>()
    private val load = Loading()

    private val SettingviewModel by viewModels<SettingViewModel> {
        SettingViewModel.Setting(SettingPreference(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSearchView()
        getThemeSetting()

        viewModel.listGithubUser.observe(this) { listUserSearch ->
            setUserData(listUserSearch)
        }

        viewModel.isLoading.observe(this) {
            load.showLoading(it, binding.progressBar)
        }

        viewModel.totalCount.observe(this) {
            showText(it)
        }

        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUser.layoutManager = layoutManager

        val btnFavorite: FloatingActionButton = findViewById(R.id.btnFavorite)
        btnFavorite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(applicationContext, FavoritActivity::class.java)
                startActivity(intent)
            }
        })

    }

    private fun getThemeSetting() {
        SettingviewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            val tvNotice = binding.tvNotice // Directly access tvNotice using binding
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.rvUser.visibility = View.VISIBLE
                    tvNotice.visibility = View.GONE
                    viewModel.searchGithubUser(it)
                    setUserData(listSearchUsers)
                }
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setUserData(listSearchUsers: List<UserSearch>) {
        val listUser = ArrayList<UserSearch>()
        for (user in listSearchUsers) {
            listUser.clear()
            listUser.addAll(listSearchUsers)
        }
        val adapter = MainAdapter(listUser)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserSearch) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: UserSearch) {
        val moveWithParcelableIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveWithParcelableIntent.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(moveWithParcelableIntent)
    }

    private fun showText(totalCount: Int) {
        with(binding) {
            if (totalCount == 0) {
                tvNotice.visibility = View.VISIBLE
                tvNotice.text = resources.getString(R.string.user_not_found)
            } else {
                tvNotice.visibility = View.INVISIBLE
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.rvUser.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}