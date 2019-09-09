package avila.domingo.cameramanager.di

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.camera.CameraImp
import avila.domingo.camera.CameraRotationUtil
import avila.domingo.camera.IConfigureCamera
import avila.domingo.camera.NativeCamera
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.cameramanager.ConfigureCameraImp
import avila.domingo.cameramanager.di.qualifiers.ForActivity
import avila.domingo.cameramanager.di.qualifiers.ForApplication
import avila.domingo.cameramanager.model.mapper.ImageMapper
import avila.domingo.cameramanager.schedulers.IScheduleProvider
import avila.domingo.cameramanager.schedulers.ScheduleProviderImp
import avila.domingo.cameramanager.ui.MainActivityViewModel
import avila.domingo.domain.ICamera
import avila.domingo.domain.IFlash
import avila.domingo.domain.interactor.*
import avila.domingo.domain.model.CameraSide
import avila.domingo.flash.FlashImp
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single(ForApplication) { androidContext() }
    single { (get(ForApplication) as Context).getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    lateinit var activityReference: AppCompatActivity
    factory { (activity: AppCompatActivity) -> activityReference = activity }
    factory<Context>(ForActivity) { activityReference }
    factory { activityReference.lifecycle }

}

val viewModelModule = module {
    viewModel {
        Log.d("yyy", "Creo viewmodule")
        MainActivityViewModel(get(), get(), get(), get(), get()/*, get(), get()*/) }
}

val useCaseModule = module {
    factory { FlashOffUseCase(get()) }
    factory { FlashOnUseCase(get()) }
    factory { SwitchCameraUseCase(get()) }
    factory { TakePictureImageUseCase(get()) }
    factory { TakePreviewImageUseCase(get()) }
}

val cameraModule = module {
    factory<ICamera> {
        Log.d("yyy", "Creo Camera")
        CameraImp(get(), get(), get(), get(), get())
    }

    single {
        Log.d("yyy", "Creo surface")
        SurfaceView(get()).apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }

    factory { NativeCamera(get(), get()) }

    single<IConfigureCamera> { ConfigureCameraImp(get(), get()) }

    single { CameraRotationUtil(get(), get()) }

    single { CameraSide.BACK }
}

//val flashModule = module {
//    factory<IFlash> { FlashImp(get()) }
//}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { CameraSideMapper() }
    single { ImageMapper() }
}