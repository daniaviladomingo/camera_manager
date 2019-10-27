package avila.domingo.cameramanager.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import avila.domingo.lifecycle.ILifecycleObserver
import avila.domingo.lifecycle.LifecycleManager
import avila.domingo.camera.*
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.cameramanager.di.qualifiers.Camera
import avila.domingo.cameramanager.di.qualifiers.Flash
import avila.domingo.cameramanager.di.qualifiers.RangeForPicture
import avila.domingo.cameramanager.di.qualifiers.RangeForPreview
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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    factory { (lifecycle: Lifecycle) ->
        LifecycleManager(arrayOf(get(Camera), get(Flash)), lifecycle)
        Unit
    }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val useCaseModule = module {
    single { FlashOffUseCase(get(Flash)) }
    single { FlashOnUseCase(get(Flash)) }
    single { TakePreviewImageUseCase(get()) }
    single { SwitchCameraUseCase(get()) }
    single { TakePictureImageUseCase(get()) }
}

val cameraModule = module {
    single<ICamera> { CameraImp(get(Camera), get(Camera), get()) }

    single {
        SurfaceView(androidContext()).apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }

    single(Camera) {
        NativeCameraManager(
            get(),
            get(),
            get(RangeForPreview),
            get(RangeForPicture),
            get(),
            get()
        )
    } binds arrayOf(
        ICameraSide::class,
        INativeCamera::class,
        ISwitchCamera::class,
        ILifecycleObserver::class
    )

    single { CameraRotationUtil(get(), get(Camera), get()) }

    single { CameraSide.BACK }

    single(RangeForPicture) { 640..2160 }

    single(RangeForPreview) { 640..2160 }
}

val flashModule = module {
    single<IFlash>(Flash) { FlashImp(get(Camera)) } bind ILifecycleObserver::class
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { CameraSideMapper() }
    single { ImageMapper() }
}