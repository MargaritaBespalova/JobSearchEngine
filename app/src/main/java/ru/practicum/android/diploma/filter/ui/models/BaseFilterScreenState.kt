package ru.practicum.android.diploma.filter.ui.models

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBaseBinding
import ru.practicum.android.diploma.filter.domain.models.SelectedFilter


sealed interface BaseFilterScreenState {

    fun render(binding: FragmentFilterBaseBinding)

    object Empty : BaseFilterScreenState {
        override fun render(binding: FragmentFilterBaseBinding) {
            with(binding) {
                chooseBaseFilterBtn.visibility = View.GONE
                bottomContainerToApply.visibility = View.GONE

            }

        }
    }

    data class Content(private val selectedFilter: SelectedFilter) : BaseFilterScreenState {

        @SuppressLint("SetTextI18n")
        override fun render(binding: FragmentFilterBaseBinding) {
            if (selectedFilter.industry?.name.isNullOrEmpty()) {
                AppCompatResources.getDrawable(binding.root.context, R.drawable.leading_icon)
                binding.departmentText.setText(selectedFilter.industry?.name ?: "")

            } else {
                binding.departmentContainer.endIconDrawable =
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.close_btn)
                binding.departmentText.setText(selectedFilter.industry?.name ?: "")
                binding.bottomContainerToApply.visibility = View.VISIBLE
            }
            binding.workPlaceText.setText(
                (selectedFilter.country?.name ?: "") +
                        "${
                            if (!selectedFilter.region?.name.isNullOrEmpty()) ", "
                            else {
                                ""
                            }
                        }${selectedFilter.region?.name ?: ""}"
            )
        }
    }


}