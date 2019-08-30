@file:Suppress("DEPRECATION")

package avila.domingo.camera

import android.hardware.Camera
import android.util.Log
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
    private val nativeCamera: NativeCamera,
    private val cameraRotationUtil: CameraRotationUtil,
    private val surfaceView: SurfaceView,
    initialCameraSide: CameraSide,
    lifecycle: Lifecycle // Esto es simplemente para start/stop la preview cuando la activity sale/entre en segundo plano
) : ICamera, LifecycleObserver {

    private val tag = this::class.java.name

    private var currentCameraSide = initialCameraSide
    private var currentCamera: Camera? = null

    init {
        lifecycle.addObserver(this)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                Log.d(tag, "CALLBACK: surfaceChanged")
//                currentCamera?.startPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                Log.d(tag, "CALLBACK: surfaceDestroyed")
//                currentCamera?.stopPreview()
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d(tag, "CALLBACK: surfaceCreated")
                currentCamera?.setPreviewDisplay(holder)
            }
        })

        currentCamera = nativeCamera.getCamera(initialCameraSide)
    }

    override fun takePreview(): Single<Image> = Single.create {
        currentCamera?.setOneShotPreviewCallback { data, camera ->
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
        } ?: it.onError(Throwable("Camera could not be configured"))
    }

    override fun takePicture(): Single<Image> = Single.create {
        currentCamera?.takePicture(null, null, null, { data, camera ->
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
        }) ?: it.onError(Throwable("Camera could not be configured"))
    }

    override fun switchCamera(cameraSide: CameraSide): Completable = Completable.create {
        if (currentCameraSide == cameraSide) {
            it.onComplete()
        } else {
            currentCamera = nativeCamera.getCamera(cameraSide)
            currentCamera?.run {
                startPreview()
                setPreviewDisplay(surfaceView.holder)
                currentCameraSide = cameraSide
            } ?: it.onError(Throwable("Camera could not be switch"))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Log.d(tag, "start()")
        currentCamera?.setPreviewDisplay(surfaceView.holder)
        currentCamera?.startPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d(tag, "stop()")
        currentCamera?.stopPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.d(tag, "destroy()")
        currentCamera?.stopPreview()
        currentCamera?.release()
        currentCamera = null
    }
}