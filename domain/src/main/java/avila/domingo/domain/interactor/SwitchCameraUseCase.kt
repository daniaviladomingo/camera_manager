package avila.domingo.domain.interactor

import avila.domingo.domain.ICamera
import avila.domingo.domain.interactor.type.CompletableUseCaseWithParameter
import avila.domingo.domain.model.CameraSide
import io.reactivex.Completable

class SwitchCameraUseCase(private val camera: ICamera) : CompletableUseCaseWithParameter<CameraSide> {
    override fun execute(parameter: CameraSide): Completable = camera.switch(parameter)
}