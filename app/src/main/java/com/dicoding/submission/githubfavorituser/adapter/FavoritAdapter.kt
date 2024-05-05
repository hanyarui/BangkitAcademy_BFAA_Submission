package com.dicoding.submission.githubfavorituser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submission.githubfavorituser.databinding.ItemRowBinding
import com.dicoding.submission.githubfavorituser.local.model.UserSearch

class FavoritAdapter(private val listUser: List<UserSearch>) : RecyclerView.Adapter<FavoritAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        with(holder.binding) {
            Glide.with(root.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(imgUserAvatar)
            tvName.text = user.login
            tvUrl.text = user.htmlUrl
            root.setOnClickListener { onItemClickCallback.onItemClicked(listUser[position]) }
        }
    }

    override fun getItemCount(): Int = listUser.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserSearch)
    }
}