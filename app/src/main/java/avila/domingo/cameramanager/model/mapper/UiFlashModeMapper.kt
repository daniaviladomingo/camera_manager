package avila.domingo.cameramanager.model.mapper

import avila.domingo.domain.model.FlashMode
import avila.domingo.domain.model.mapper.Mapper

class UiFlashModeMapper : Mapper<Boolean, FlashMode>() {
    override fun map(model: Boolean): FlashMode = if (model) FlashMode.ON else FlashMode.OFF

    override fun inverseMap(model: FlashMode): Boolean = when (model) {
        FlashMode.ON -> true
        FlashMode.OFF -> false
    }
}