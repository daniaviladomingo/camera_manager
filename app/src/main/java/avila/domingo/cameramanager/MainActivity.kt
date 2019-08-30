package avila.domingo.cameramanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.camera.CameraImp
import avila.domingo.camera.CameraRotationUtil
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.cameramanager.model.mapper.ImageMapper
import avila.domingo.domain.model.CameraSide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tag = this::class.java.name

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageMapper = ImageMapper()

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val cameraSideMapper = CameraSideMapper()
        val cameraRotationUtil = CameraRotationUtil(windowManager, cameraSideMapper)
        val configureCamera = ConfigureCameraImp(cameraRotationUtil)
        val camera = CameraImp(
            configureCamera,
            cameraRotationUtil,
            surfaceView,
            cameraSideMapper,
            CameraSide.FRONT,
            this.lifecycle
        )

        take_image.setOnClickListener {
            addDisposable(
                camera
                    .takePicture()
                    .subscribe({ picture ->
                        image.setImageBitmap(imageMapper.map(picture))
                    }) {
                        Log.d(tag, it.localizedMessage)
                    })
        }

        switch_camera.setOnClickListener {
            addDisposable(
                camera
                    .switchCamera(CameraSide.BACK)
                    .subscribe({
                        Log.d(tag, "Camera Switched!!")
                    }) {
                        Log.d(tag, it.localizedMessage)
                    })
        }

        take_preview.setOnClickListener {
            addDisposable(
                camera
                    .takePreview()
                    .subscribe({ picture ->
                        image.setImageBitmap(imageMapper.map(picture))
                    }) {
                        Log.d(tag, it.localizedMessage)
                    })

//            addDisposable(
//                Observable
//                    .interval(1000 / 7L, TimeUnit.MILLISECONDS)
//                    .subscribeOn(Schedulers.io())
//                    .flatMap { camera.takePreview().toObservable().map { imageMapper.map(it) } }
//                    .subscribe({
//                        image.setImageBitmap(it)
//                    }) {
//                        Log.d(tag, it.localizedMessage)
//                    })
        }

        flash_on.setOnClickListener {
            addDisposable(camera
                .on()
                .subscribe({
                    Log.d(tag, "FLASH ON")
                }) {
                    Log.d(tag, it.localizedMessage)
                })
        }

        flash_off.setOnClickListener {
            addDisposable(camera
                .off()
                .subscribe({
                    Log.d(tag, "FLASH OFF")
                }) {
                    Log.d(tag, it.localizedMessage)
                })
        }
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onStop()
    }

    private fun addDisposable(d: Disposable) {
        disposable.add(d)
    }
}
