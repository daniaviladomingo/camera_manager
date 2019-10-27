@file:Suppress("DEPRECATION")

package avila.domingo.flash

import android.hardware.Camera
import avila.domingo.android.ILifecycleObserver
import avila.domingo.camera.INativeCamera
import avila.domingo.domain.IFlash
import io.reactivex.Completable

class FlashImp(private val camera: INativeCamera) : IFlash, ILifecycleObserver {
    private var flashState = false
    override fun on(): Completable = Completable.create {
        action(true)
        it.onComplete()
        flashState = true

    }

    override fun off(): Completable = Completable.create {
        action(false)
        it.onComplete()
        flashState = false
    }

    override fun start() {
        if (flashState) {
            action(true)
        }
    }

    override fun stop() {
        if (flashState) {
            action(false)
        }
    }

    private fun action(flashState: Boolean) {
        camera.camera().run {
            try {
                parameters = parameters.apply {
                    flashMode =
                        if (flashState) Camera.Parameters.FLASH_MODE_TORCH else Camera.Parameters.FLASH_MODE_OFF
                }
            } catch (e: RuntimeException) {
            }
        }
    }
}