package ru.practicum.android.diploma.details.ui

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.details.domain.models.VacancyFullInfo
import ru.practicum.android.diploma.root.RootActivity
import ru.practicum.android.diploma.util.setImage
import ru.practicum.android.diploma.util.thisName
import ru.practicum.android.diploma.util.viewBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val binding by viewBinding<FragmentDetailsBinding>()
    private val viewModel: DetailsViewModel by viewModels { (activity as RootActivity).viewModelFactory }
    private val args by navArgs<DetailsFragmentArgs>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.log(thisName, "onViewCreated()")
        viewModel.getVacancyByID(args.vacancy.id)
        collector()
        pressSimilarVacanciesButton()
        initListeners()
        sendVacancy()
        writeEmail()
        makeCall()
    }

    private fun initListeners() {
        binding.lottieHeart.setOnClickListener {
            viewModel.handleAddToFavsButton(args.vacancy)
        }
        binding.vacancyToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun collector() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                viewModel.log(thisName, "uiState.collect { state -> ${state.thisName}")
                when (state) {
                    is DetailsScreenState.Content -> showContent(state.vacancy)
                    is DetailsScreenState.Empty -> showEmpty()
                    is DetailsScreenState.Error -> showError(state.message)
                    is DetailsScreenState.Loading -> showLoading()
                    is DetailsScreenState.Offline -> showOffline(state.message)
                    is DetailsScreenState.PlayHeartAnimation -> showPlayHeartAnimation(state.isInFavs)
                }
            }
        }
    }
    
    private fun showContent(vacancy: VacancyFullInfo) {
        with(binding) {
            scrollView.visibility = View.VISIBLE
            placeHolder.visibility = View.GONE
            iwAnim.visibility = View.GONE
            hideContactsIfEmpty(vacancy)
            showKeySkills(vacancy)
            val tvSchedule = vacancy.employment + ". " + vacancy.schedule
            val formattedDescription =
                HtmlCompat.fromHtml(vacancy.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            
            if (vacancy.logo.isNotEmpty()) imageView.imageTintList = null
            
            tvContactsPhone.text = if (vacancy.contactPhones.isEmpty()) {
                binding.root.context.getString(R.string.no_info)
            } else {
                vacancy.contactPhones.joinToString("\n")
            }
            
            tvContactsName.text = vacancy.contactName
            tvContactsEmail.text = vacancy.contactEmail
            tvExperience.text = vacancy.experience
            tvScheduleEmployment.text = tvSchedule
            tvDescription.text = formattedDescription
            tvNameOfVacancy.text = vacancy.title
            tvSalary.text = vacancy.salary
            tvNameOfCompany.text = vacancy.company
            tvArea.text = vacancy.area
            imageView.setImage(
                vacancy.logo,
                R.drawable.ic_placeholder_company,
                binding.root.context.resources.getDimensionPixelSize(R.dimen.size_12dp)
            )
        }
    }
    
    private fun showEmpty() = Unit
    private fun showError(message: String) {
        binding.scrollView.visibility = View.GONE
        binding.placeHolderText.text = message
        binding.placeHolder.visibility = View.VISIBLE
    }
    
    private fun showLoading() {
        binding.scrollView.visibility = View.GONE
        binding.iwAnim.visibility = View.VISIBLE
    }
    
    private fun showOffline(message: String) {
        binding.scrollView.visibility = View.GONE
        binding.placeHolderText.text = message
        binding.placeHolder.visibility = View.VISIBLE
    }
    
    private fun showPlayHeartAnimation(isInFavs: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (isInFavs) {
                binding.lottieHeart.speed = STRAIGHT_ANIMATION_SPEED
                binding.lottieHeart.playAnimation()
            } else {
                binding.lottieHeart.speed = REVERS_ANIMATION_SPEED
                binding.lottieHeart.playAnimation()
            }
        }
    }
    
    private fun sendVacancy() {
        binding.shareButton.setOnClickListener {
            viewModel.log(thisName, "buttonSendVacancy.setOnClickListener { }")
            viewModel.sendVacancy()
        }
    }
    
    private fun writeEmail() {
        binding.tvContactsEmail.setOnClickListener {
            viewModel.log(thisName, "buttonWriteEmail.setOnClickListener { }")
            viewModel.writeEmail(requireContext())
        }
    }

    private fun makeCall() {
        binding.tvContactsPhone.setOnClickListener {
            viewModel.log(thisName, "buttonMakeCall.setOnClickListener { }")
            viewModel.makeCall()
        }
    }

    private fun pressSimilarVacanciesButton() {
        binding.buttonSameVacancy.setOnClickListener {
            viewModel.log(thisName, "buttonSameVacancy.setOnClickListener { }")
            navigateToSimilarVacancies()
        }
    }
    
    private fun navigateToSimilarVacancies() {
        findNavController().navigate(
            DetailsFragmentDirections.actionDetailsFragmentToSimilarsVacancyFragment(args.vacancy)
        )
    }
    
    private fun showKeySkills(vacancy: VacancyFullInfo) {
        with(binding) {
            if (vacancy.keySkills.isEmpty()) {
                tvKeySkillsTitle.visibility = View.GONE
                tvKeySkills.visibility = View.GONE
            } else {
                tvKeySkills.text = vacancy.keySkills
            }
        }
    }
    
    private fun hideContactsIfEmpty(vacancy: VacancyFullInfo) {
        with(binding) {
            if (vacancy.contactName.isEmpty()) {
                tvContactsName.visibility = View.GONE
                tvContactsPerson.visibility = View.GONE
            }
            if (vacancy.contactEmail.isEmpty()) {
                tvContactsEmail.visibility = View.GONE
                tvEmail.visibility = View.GONE
            }
            if (vacancy.contactPhones.isEmpty()) {
                tvContactsPhone.visibility = View.GONE
                tvPhone.visibility = View.GONE
            }
            if (vacancy.contactName.isEmpty() && vacancy.contactEmail.isEmpty() && vacancy.contactPhones.isEmpty()) {
                tvContactsTitle.visibility = View.GONE
            }
        }
    }
    
    companion object {
        private const val STRAIGHT_ANIMATION_SPEED = 1f
        private const val REVERS_ANIMATION_SPEED = -1f
    }
}
