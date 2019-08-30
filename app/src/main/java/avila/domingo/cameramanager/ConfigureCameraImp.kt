@file:Suppress("DEPRECATION")

package avila.domingo.cameramanager

import android.hardware.Camera
import android.view.WindowManager
import avila.domingo.camera.CameraRotationUtil
import avila.domingo.camera.IConfigureCamera
import avila.domingo.domain.model.CameraSide

class ConfigureCameraImp(
    private val windowManager: WindowManager,
    private val cameraRotationUtil: CameraRotationUtil
) : IConfigureCamera {
    override fun configure(camera: Camera?, cameraSide: CameraSide) {
        camera?.run {
            val customParameters = parameters
            customParameters.supportedFocusModes.run {
                when {
                    this.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) -> customParameters.focusMode =
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                    this.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) -> customParameters.focusMode =
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
                    this.contains(Camera.Parameters.FOCUS_MODE_AUTO) -> customParameters.focusMode =
                        Camera.Parameters.FOCUS_MODE_AUTO
                }
            }

            val screenRatio = screenSize.witdh / screenSize.height.toFloat()

            var diff = Float.MAX_VALUE
            var previewWidth = 0
            var previewHeight = 0

            customParameters.supportedPreviewSizes
                .sortedByDescending { it.width }
                .apply {
                    this.forEach {
                        val previewDiff =
                            Math.abs((it.width / it.height.toFloat()) - screenRatio)
                        if (previewDiff < diff) {
                            diff = previewDiff
                            previewWidth = it.width
                            previewHeight = it.height
                        }
                    }
                }
                .filter { screenRatio == (it.width / it.height.toFloat()) }
                .run {
                    if (size > 0) {
                        get(0).let { customParameters.setPreviewSize(it.width, it.height) }
                    } else {
                        customParameters.setPreviewSize(previewWidth, previewHeight)
                    }
                }

            customParameters.supportedPictureSizes
                .sortedByDescending { it.width }
                .apply {
                    this.forEach {
                        val previewDiff =
                            Math.abs((it.width / it.height.toFloat()) - screenRatio)
                        if (previewDiff < diff) {
                            diff = previewDiff
                            previewWidth = it.width
                            previewHeight = it.height
                        }
                    }
                }
                .filter { screenRatio == (it.width / it.height.toFloat()) }
                .run {
                    if (size > 0) {
                        get(0).let { customParameters.setPictureSize(it.width, it.height) }
                    } else {
                        customParameters.setPictureSize(previewWidth, previewHeight)
                    }
                }

            parameters = customParameters

            setDisplayOrientation(cameraRotationUtil.rotationDegreesSurface())
        }
    }
}