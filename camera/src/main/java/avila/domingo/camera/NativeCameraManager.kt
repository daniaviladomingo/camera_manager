@file:Suppress("DEPRECATION")

package avila.domingo.camera

import android.hardware.Camera
import avila.domingo.camera.config.IConfigureCamera
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.domain.model.CameraSide

class NativeCameraManager(
    private val configureCamera: IConfigureCamera,
    private val cameraSideMapper: CameraSideMapper,
    initialCameraSide: CameraSide
) : INativeCamera, ISwitchCamera, ICameraSide {
    private lateinit var currentCamera: Camera
    private var currentCameraSide = initialCameraSide

    init {
        openCamera(initialCameraSide)
    }

    override fun switch() {
        currentCamera.release()
        openCamera(when (currentCameraSide) {
            CameraSide.BACK -> CameraSide.FRONT
            CameraSide.FRONT -> CameraSide.BACK

        }.apply {
            currentCameraSide = this
        })
    }

    override fun cameraSide(): CameraSide = currentCameraSide

    override fun camera(): Camera = currentCamera

    private fun openCamera(cameraSide: CameraSide) {
        currentCamera = Camera.open(cameraSideMapper.map(cameraSide))
        configureCamera.configure(currentCamera, cameraSide)
        currentCameraSide = cameraSide
    }
}