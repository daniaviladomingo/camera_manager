package avila.domingo.cameramanager.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import avila.domingo.android.ILifecycleObserver
import avila.domingo.android.LifecycleManager
import avila.domingo.camera.*
import avila.domingo.camera.model.mapper.CameraSideMapper
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
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    factory { (lifecycle: Lifecycle) ->
        LifecycleManager(get(), lifecycle)
        Unit
    }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val useCaseModule = module {
    single { FlashOffUseCase(get()) }
    single { FlashOnUseCase(get()) }
    single { TakePreviewImageUseCase(get()) }
    single { SwitchCameraUseCase(get()) }
    single { TakePictureImageUseCase(get()) }
}

val cameraModule = module {
    single<ICamera> { CameraImp(get(), get(), get()) }

    single {
        SurfaceView(androidContext()).apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }

    single {
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

    single { CameraRotationUtil(get(), get(), get()) }

    single { CameraSide.BACK }

    single(RangeForPicture) { 640..2160 }

    single(RangeForPreview) { 640..2160 }
}

val flashModule = module {
    single<IFlash> { FlashImp(get()) }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { CameraSideMapper() }
    single { ImageMapper() }
}