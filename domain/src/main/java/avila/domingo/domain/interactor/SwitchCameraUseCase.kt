package avila.domingo.domain.interactor

import avila.domingo.domain.ICamera
import avila.domingo.domain.interactor.type.CompletableUseCase
import io.reactivex.Completable

class SwitchCameraUseCase(private val camera: ICamera) : CompletableUseCase {
    override fun execute(): Completable = camera.switchCamera()
}