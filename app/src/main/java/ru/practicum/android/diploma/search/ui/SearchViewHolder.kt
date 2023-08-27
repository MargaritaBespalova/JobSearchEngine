package ru.practicum.android.diploma.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemDescriptionBinding
import ru.practicum.android.diploma.search.domain.Vacancy

class SearchViewHolder(
    private val binding: ItemDescriptionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) {
        binding.title.text = vacancy.title
        binding.company.text = vacancy.company
        binding.value.text = vacancy.salary
        Glide.with(itemView.context)
            .load(vacancy.iconUri)
            .placeholder(R.drawable.ic_placeholder_company)
            .transform(CenterCrop(),RoundedCorners(R.dimen.size_12dp))
            .into(binding.image)
    }
}