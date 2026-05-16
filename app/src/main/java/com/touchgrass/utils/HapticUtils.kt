package com.touchgrass.utils

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticUtils @Inject constructor(
    private val vibrator: Vibrator,
) {
    fun lightTap() = vibrate(50, VibrationEffect.EFFECT_TICK)
    fun mediumTap() = vibrate(100, VibrationEffect.EFFECT_CLICK)
    fun heavyTap() = vibrate(200, VibrationEffect.EFFECT_HEAVY_CLICK)
    fun success() = vibrate(150, VibrationEffect.EFFECT_DOUBLE_CLICK)
    fun error() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 100, 50, 100), -1))
        }
    }

    private fun vibrate(ms: Long, effectId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(ms)
        }
    }
}
