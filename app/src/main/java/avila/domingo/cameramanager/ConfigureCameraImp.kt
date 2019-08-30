@file:Suppress("DEPRECATION")

package avila.domingo.cameramanager

import android.hardware.Camera
import avila.domingo.camera.CameraRotationUtil
import avila.domingo.camera.IConfigureCamera
import avila.domingo.domain.model.CameraSide

class ConfigureCameraImp(
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

            customParameters.setPreviewSize(640, 360)
            customParameters.setPictureSize(640, 360)

            parameters = customParameters

            setDisplayOrientation(cameraRotationUtil.rotationDegreesSurface())
        }
    }
}