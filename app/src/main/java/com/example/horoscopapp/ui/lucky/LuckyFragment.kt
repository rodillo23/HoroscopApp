package com.example.horoscopapp.ui.lucky

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.horoscopapp.R
import com.example.horoscopapp.databinding.FragmentLuckyBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class LuckyFragment : Fragment() {

    private val viewModel by viewModels<LuckyViewModel>()
    private var _binding: FragmentLuckyBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var randomCardsProvider: RandomCardsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.tvLuckyInfo.animate().rotationY(50f).duration = 1500
        //binding.tvLuckyInfo.animate().rotation(360f).duration=1500

        // val rotation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        //binding.tvLuckyInfo.startAnimation(rotation)

        binding.viewBackContainer.viewBack.setOnClickListener {
            prepareCard()
            flipCard()
        }
    }

    private fun flipCard() {
        try {

            //visibilidad
            binding.viewFrontContainer.viewFront.isVisible = true

            //Efecto 3D
            val scale = requireContext().resources.displayMetrics.density
            val cameraDist = 8000 * scale
            binding.viewFrontContainer.viewFront.cameraDistance = cameraDist
            binding.viewBackContainer.viewBack.cameraDistance = cameraDist

            //Recuperamos la animacion Flip Out
            val flipOutAnimatorSet =
                AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_out) as AnimatorSet
            flipOutAnimatorSet.setTarget(binding.viewBackContainer.viewBack)

            //Recuperamos la animacion Flip In
            val flipInAnimatorSet =
                AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_in) as AnimatorSet
            flipInAnimatorSet.setTarget(binding.viewFrontContainer.viewFront)

            //Iniciamos animacion
            flipOutAnimatorSet.start()
            flipInAnimatorSet.start()

            flipInAnimatorSet.doOnEnd {
                binding.viewBackContainer.viewBack.isVisible = false
                binding.tvLuckyInfo.animate().alpha(1f).duration = 1000
            }

        } catch (e: Exception) {
            Log.i("Error", e.toString())
        }
    }

    private fun prepareCard() {
        val luck = randomCardsProvider.getLucky()

        binding.viewFrontContainer.ivLuckyCard.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(), luck.image
            )
        )

        binding.tvLuckyInfo.text = getString(luck.text)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLuckyBinding.inflate(inflater, container, false)
        return binding.root
    }


}