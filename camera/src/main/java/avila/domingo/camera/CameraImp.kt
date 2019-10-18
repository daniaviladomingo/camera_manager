@file:Suppress("DEPRECATION")

package avila.domingo.camera

import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import avila.domingo.domain.ICamera
import avila.domingo.domain.model.CameraSide
import avila.domingo.domain.model.Image
import avila.domingo.domain.model.ImageFormat
import io.reactivex.Completable
import io.reactivex.Single

class CameraImp(
    private val nativeCameraManager: NativeCameraManager,
    private val cameraRotationUtil: CameraRotationUtil,
    private val surfaceView: SurfaceView,
    initialCameraSide: CameraSide,
    lifecycle: Lifecycle // Esto es simplemente para start/stop la preview cuando la activity sale/entre en segundo plano
) : ICamera, LifecycleObserver {
    
    private var camera = nativeCameraManager.getCamera(initialCameraSide)
    private var currentCameraSide = initialCameraSide

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {}

        override fun surfaceCreated(holder: SurfaceHolder) {
            camera.setPreviewDisplay(holder)
        }
    }

    init {
        lifecycle.addObserver(this)
        surfaceView.holder.addCallback(surfaceHolderCallback)
    }

    override fun takePreview(): Single<Image> = Single.create {
        camera.setOneShotPreviewCallback { data, camera ->
            val previewSize = camera.parameters.previewSize
            it.onSuccess(
                Image(
                    ImageFormat.NV21,
                    data,
                    previewSize.width,
                    previewSize.height,
                    cameraRotationUtil.rotationDegrees(currentCameraSide)
                )
            )
        }
    }

    override fun takePicture(): Single<Image> = Single.create {
        camera.takePicture(null, null, null, { data, camera ->
            camera.startPreview()
            val pictureSize = camera.parameters.pictureSize
            it.onSuccess(
                Image(
                    ImageFormat.JPEG,
                    data,
                    pictureSize.width,
                    pictureSize.height,
                    cameraRotationUtil.rotationDegrees(currentCameraSide)
                )
            )
        })
    }

    override fun switchCamera(cameraSide: CameraSide): Completable = Completable.create {
        if (currentCameraSide == cameraSide) {
            it.onComplete()
        } else {
            camera = nativeCameraManager.getCamera(cameraSide)
            camera.run {
                startPreview()
                setPreviewDisplay(surfaceView.holder)
                currentCameraSide = cameraSide
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        camera.setPreviewDisplay(surfaceView.holder)
        camera.startPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        camera.stopPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        surfaceView.holder.removeCallback(surfaceHolderCallback)
        camera.stopPreview()
        camera.release()
    }
}