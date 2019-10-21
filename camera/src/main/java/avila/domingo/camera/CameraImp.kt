@file:Suppress("DEPRECATION")

package avila.domingo.camera

import androidx.lifecycle.LifecycleObserver
import avila.domingo.domain.ICamera
import avila.domingo.domain.model.Image
import avila.domingo.domain.model.ImageFormat
import io.reactivex.Completable
import io.reactivex.Single

class CameraImp(
    private val nativeCamera: INativeCamera,
    private val switchCamera: ISwitchCamera,
    private val cameraRotationUtil: CameraRotationUtil
) : ICamera, LifecycleObserver {

//    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
//        override fun surfaceChanged(
//            holder: SurfaceHolder,
//            format: Int,
//            width: Int,
//            height: Int
//        ) {
//        }
//
//        override fun surfaceDestroyed(holder: SurfaceHolder) {}
//
//        override fun surfaceCreated(holder: SurfaceHolder) {
//            nativeCamera.camera().setPreviewDisplay(holder)
//        }
//    }

    override fun takePreview(): Single<Image> = Single.create {
        nativeCamera.camera().setOneShotPreviewCallback { data, camera ->
            val previewSize = camera.parameters.previewSize
            it.onSuccess(
                Image(
                    ImageFormat.NV21,
                    data,
                    previewSize.width,
                    previewSize.height,
                    cameraRotationUtil.rotationDegrees()
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
                    cameraRotationUtil.rotationDegrees()
                )
            )
        })
    }

    override fun switchCamera(): Completable = Completable.create {
        switchCamera.switch()
        it.onComplete()
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun start() {
//        nativeCamera.camera().setPreviewDisplay(surfaceView.holder)
//        nativeCamera.camera().startPreview()
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun stop() {
//        nativeCamera.camera().stopPreview()
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    fun destroy() {
//        surfaceView.holder.removeCallback(surfaceHolderCallback)
//        nativeCamera.camera().stopPreview()
//        nativeCamera.camera().release()
//    }
}