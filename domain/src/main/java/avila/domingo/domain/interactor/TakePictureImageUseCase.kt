package avila.domingo.domain.interactor

import avila.domingo.domain.ICamera
import avila.domingo.domain.interactor.type.SingleUseCase
import avila.domingo.domain.model.Image
import io.reactivex.Single

class TakePictureImageUseCase(private val camera: ICamera) : SingleUseCase<Image> {
    override fun execute(): Single<Image> = camera.takePicture()
}