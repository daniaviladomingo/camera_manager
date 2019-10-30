@file:Suppress("DEPRECATION")

package avila.domingo.flash.model.mapper

import android.hardware.Camera
import avila.domingo.domain.model.FlashMode
import avila.domingo.domain.model.mapper.Mapper

class FlashModeMapper : Mapper<FlashMode, String>() {
    override fun map(model: FlashMode): String = when (model) {
        FlashMode.OFF -> Camera.Parameters.FLASH_MODE_OFF
        FlashMode.ON -> Camera.Parameters.FLASH_MODE_TORCH
    }

    override fun inverseMap(model: String): FlashMode = when (model) {
        Camera.Parameters.FLASH_MODE_OFF -> FlashMode.OFF
        Camera.Parameters.FLASH_MODE_TORCH -> FlashMode.ON
        else -> FlashMode.OFF
    }
}