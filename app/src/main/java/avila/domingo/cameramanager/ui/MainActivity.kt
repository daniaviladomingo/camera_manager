package avila.domingo.cameramanager.ui

import android.os.Bundle
import android.view.SurfaceView
import androidx.lifecycle.Observer
import avila.domingo.cameramanager.R
import avila.domingo.cameramanager.base.BaseActivity
import avila.domingo.cameramanager.ui.data.ResourceState
import avila.domingo.domain.model.CameraSide
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private val surfaceView: SurfaceView by inject()

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    private var cameraSide = CameraSide.FRONT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setListener()

        take_picture.setOnClickListener {
            mainActivityViewModel.takePicture()
        }

        take_preview.setOnClickListener {
            mainActivityViewModel.takePreview()
        }

        switch_camera.setOnClickListener {
            mainActivityViewModel.switchCamera(when (cameraSide) {
                CameraSide.FRONT -> CameraSide.BACK
                CameraSide.BACK -> CameraSide.FRONT
            }.apply {
                cameraSide = this
            })
        }

//        flash_on.setOnClickListener {
//            mainActivityViewModel.flashOn()
//        }
//
//        flash_off.setOnClickListener {
//            mainActivityViewModel.flashOff()
//        }

    }

    override fun onResume() {
        super.onResume()
        surface_view.addView(surfaceView)
    }

    override fun onPause() {
        super.onPause()
        surface_view.removeView(surfaceView)
    }

    private fun setListener() {
        mainActivityViewModel.flashLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
            }
        })

        mainActivityViewModel.takeImageLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
                if (status == ResourceState.SUCCESS) {
                    data?.run {
                        image.setImageBitmap(this)
                    }
                }
            }
        })

        mainActivityViewModel.switchCameraLiveData.observe(this, Observer { resource ->
            resource?.run {
                managementResourceState(status, message)
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun checkAgain(): () -> Unit = {}

    override fun tryAgain(): () -> Unit = {}
}
