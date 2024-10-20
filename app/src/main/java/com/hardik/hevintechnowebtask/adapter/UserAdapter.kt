package com.hardik.hevintechnowebtask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hardik.hevintechnowebtask.R
import com.hardik.hevintechnowebtask.databinding.ItemUserPreviewBinding
import com.hardik.hevintechnowebtask.domain.model.UserModel

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {
    val TAG = UserAdapter::class.java.simpleName
    inner class UserViewHolder(val binding: ItemUserPreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(
            oldItem: UserModel,
            newItem: UserModel
                                    ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserModel,
            newItem: UserModel
                                       ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this@UserAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
                                          )
                             )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(200).start()
        holder.itemView.apply {
            holder.binding.tvTitle.text = user.firstName +" "+user.lastName
            holder.binding.tvEmail.text = user.email
            Glide
                .with(this)
                .load(user.image)
//                .load("https://100k-faces.glitch.me/random-image")
//                .load("https://ozgrozer.github.io/100k-faces/0/6/006722.jpg")
//                .centerCrop()
//                .apply(RequestOptions.circleCropTransform())
//                .apply(RequestOptions().transform(RoundedCorners(20)))
                .fitCenter()
                .placeholder(ContextCompat.getDrawable(this.context, R.drawable.ic_launcher_foreground))
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.ivUser)
            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        UserModel(
                            id = user.id ?: 1,
                            email=user.email,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            image = user.image,
                            )
                      )
                }
            }
        }
    }

    private var onItemClickListener: ((UserModel) -> Unit)? = null
    fun setOnItemClickListener(onItemClickListener: ((UserModel) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

    private var originalList: List<UserModel> = emptyList()

    // Function to set the original list
    fun setOriginalList(list: List<UserModel>) {
        originalList = list
        differ.submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    originalList
                } else {
                    originalList.filter { user ->
                        user.firstName.contains(constraint, true) ||
                                user.lastName.contains(constraint,true) ||
                                user.email.contains(constraint, true)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                differ.submitList(results?.values as List<UserModel>?)
            }
        }
    }
}