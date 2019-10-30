@file:Suppress("DEPRECATION")

package avila.domingo.camera

import avila.domingo.camera.cameranative.INativeCamera
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.domain.ICamera
import avila.domingo.domain.model.CameraSide
import avila.domingo.domain.model.Image
import avila.domingo.domain.model.ImageFormat
import io.reactivex.Completable
import io.reactivex.Single

class CameraImp(
    private val nativeCamera: INativeCamera,
    private val cameraSideMapper: CameraSideMapper
) : ICamera {
    override fun takePreview(): Single<Image> = Single.create {
        nativeCamera.camera().setOneShotPreviewCallback { data, camera ->
            val previewSize = camera.parameters.previewSize
            it.onSuccess(
                Image(
                    ImageFormat.NV21,
                    data,
                    previewSize.width,
                    previewSize.height,
                    nativeCamera.rotationDegrees()
                )
            )
        }
    }

    override fun takePicture(): Single<Image> = Single.create {
        nativeCamera.camera().takePicture(null, null, null, { data, camera ->
            camera.startPreview()
            val pictureSize = camera.parameters.pictureSize
            it.onSuccess(
                Image(
                    ImageFormat.JPEG,
                    data,
                    pictureSize.width,
                    pictureSize.height,
                    nativeCamera.rotationDegrees()
                )
            )
        })
    }

    override fun switch(cameraSide: CameraSide): Completable = Completable.create {
        nativeCamera.switch(cameraSideMapper.map(cameraSide))
        it.onComplete()
    }

    override fun side(): Single<CameraSide> = Single.create {
        it.onSuccess(cameraSideMapper.inverseMap(nativeCamera.cameraId()))
    }
}