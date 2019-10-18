@file:Suppress("DEPRECATION")

package avila.domingo.flash

import android.hardware.Camera
import avila.domingo.camera.NativeCameraManager
import avila.domingo.domain.IFlash
import io.reactivex.Completable

class FlashImp(private val nativeCameraManager: NativeCameraManager) : IFlash {
    override fun on(): Completable = Completable.create {
        action("ON")
        it.onComplete()
    }

    override fun off(): Completable = Completable.create {
        action("OFF")
        it.onComplete()
    }

    private fun action(action: String) {
        nativeCameraManager.getCurrentCamera().run {
            val customParameters = parameters

            customParameters.flashMode = when (action) {
                "ON" -> Camera.Parameters.FLASH_MODE_TORCH
                "OFF" -> Camera.Parameters.FLASH_MODE_OFF
                else -> Camera.Parameters.FLASH_MODE_OFF
            }
            parameters = customParameters
        }
    }
}