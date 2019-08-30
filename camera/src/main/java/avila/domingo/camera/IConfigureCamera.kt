@file:Suppress("DEPRECATION")

package avila.domingo.camera

import android.hardware.Camera
import avila.domingo.domain.model.CameraSide

interface IConfigureCamera {
    fun configure(camera: Camera?, cameraSide: CameraSide)
}