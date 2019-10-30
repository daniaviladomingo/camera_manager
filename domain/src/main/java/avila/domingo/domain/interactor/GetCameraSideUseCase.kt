package avila.domingo.domain.interactor

import avila.domingo.domain.ICamera
import avila.domingo.domain.interactor.type.SingleUseCase
import avila.domingo.domain.model.CameraSide
import io.reactivex.Single

class GetCameraSideUseCase(private val camera: ICamera) : SingleUseCase<CameraSide> {
    override fun execute(): Single<CameraSide> = camera.side()
}