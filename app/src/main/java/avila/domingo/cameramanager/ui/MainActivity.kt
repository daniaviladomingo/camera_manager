package avila.domingo.cameramanager.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import avila.domingo.camera.CameraImp
import avila.domingo.camera.CameraRotationUtil
import avila.domingo.camera.NativeCamera
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.cameramanager.ConfigureCameraImp
import avila.domingo.cameramanager.R
import avila.domingo.cameramanager.base.BaseActivity
import avila.domingo.cameramanager.model.mapper.ImageMapper
import avila.domingo.domain.model.CameraSide
import avila.domingo.flash.FlashImp
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private val tag = this::class.java.name

    private val mainActivityViewModel: MainActivityViewModel by viewModel()


    private val disposable = CompositeDisposable()

    private var cameraSide = CameraSide.FRONT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageMapper = ImageMapper()
        val cameraSideMapper = CameraSideMapper()

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val cameraRotationUtil = CameraRotationUtil(windowManager, cameraSideMapper)
        val configureCamera =
            ConfigureCameraImp(windowManager, cameraRotationUtil)
        val nativeCamera = NativeCamera(configureCamera, cameraSideMapper)

        val flash = FlashImp(nativeCamera)

        val camera = CameraImp(
            nativeCamera,
            cameraRotationUtil,
            surfaceView,
            cameraSide,
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
                    .switchCamera(
                        when (cameraSide) {
                            CameraSide.FRONT -> CameraSide.BACK
                            CameraSide.BACK -> CameraSide.FRONT
                        }.apply {
                            cameraSide = this
                        }
                    )
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
        }

        flash_on.setOnClickListener {
            addDisposable(flash
                .on()
                .subscribe({
                    Log.d(tag, "FLASH ON")
                }) {
                    Log.d(tag, it.localizedMessage)
                })
        }

        flash_off.setOnClickListener {
            addDisposable(flash
                .off()
                .subscribe({
                    Log.d(tag, "FLASH OFF")
                }) {
                    Log.d(tag, it.localizedMessage)
                })
        }
    }

    private fun setListener() {

    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}
}
