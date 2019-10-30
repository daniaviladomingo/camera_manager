package avila.domingo.cameramanager.model.mapper

import avila.domingo.domain.model.CameraSide
import avila.domingo.domain.model.mapper.Mapper

class UiCameraSideMapper : Mapper<Boolean, CameraSide>() {
    override fun map(model: Boolean): CameraSide = if (model) CameraSide.BACK else CameraSide.FRONT

    override fun inverseMap(model: CameraSide): Boolean = when (model) {
        CameraSide.BACK -> true
        CameraSide.FRONT -> false
    }
}