package avila.domingo.domain

import avila.domingo.domain.model.Image
import io.reactivex.Completable
import io.reactivex.Single

interface ICamera {
    fun takePreview(): Single<Image>
    fun takePicture(): Single<Image>
    fun switchCamera(): Completable
}