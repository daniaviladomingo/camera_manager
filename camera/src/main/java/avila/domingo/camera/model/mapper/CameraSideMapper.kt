@file:Suppress("DEPRECATION")

package avila.domingo.camera.model.mapper

import android.hardware.Camera
import avila.domingo.domain.model.CameraSide
import avila.domingo.domain.model.mapper.Mapper

class CameraSideMapper : Mapper<CameraSide, Int>() {
    override fun map(model: CameraSide): Int = when (model) {
        CameraSide.BACK -> Camera.CameraInfo.CAMERA_FACING_BACK
        CameraSide.FRONT -> Camera.CameraInfo.CAMERA_FACING_FRONT
    }

    override fun inverseMap(model: Int): CameraSide = when (model) {
        Camera.CameraInfo.CAMERA_FACING_BACK -> CameraSide.BACK
        Camera.CameraInfo.CAMERA_FACING_FRONT -> CameraSide.FRONT
        else -> CameraSide.BACK
    }
}