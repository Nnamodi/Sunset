package com.bignerdranch.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View
    private lateinit var sunReflection: View
    private lateinit var seaView: View
    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }
    private var initialPos = 0f
    private var reflectPos = 0f
    private var sunUp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)
        sunReflection = findViewById(R.id.sun_reflection)
        seaView = findViewById(R.id.sea)
        sceneView.setOnClickListener {
            if (sunUp) { startAnimation() }
            else { reverseAnimation() }
        }
    }

    private fun startAnimation() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()
        val sunReflectYStart = sunReflection.top.toFloat()
        val sunReflectYEnd = seaView.height.toFloat()
        initialPos = sunYStart
        reflectPos = sunReflectYStart
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val sunReflectionAnimator = ObjectAnimator
            .ofFloat(sunReflection, "y", sunReflectYStart, -sunReflectYEnd)
            .setDuration(4500)
        sunReflectionAnimator.interpolator = AccelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .with(sunReflectionAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
        sunUp = false
        Log.d("Anim...", "$sunYStart")
    }

    /** Based on a challenge. **/
    private fun reverseAnimation() {
        val sunYStart = skyView.height.toFloat()
        val sunYEnd = initialPos
        val sunReflectYStart = seaView.height.toFloat()
        val sunReflectYEnd = reflectPos
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val blueSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(1500)
        blueSkyAnimator.setEvaluator(ArgbEvaluator())

        val sunReflectionAnimator = ObjectAnimator
            .ofFloat(sunReflection, "y", -sunReflectYStart, sunReflectYEnd)
            .setDuration(4000)
        sunReflectionAnimator.interpolator = AccelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunriseSkyAnimator)
            .with(sunReflectionAnimator)
            .after(blueSkyAnimator)
        animatorSet.start()
        sunUp = true
    }
}