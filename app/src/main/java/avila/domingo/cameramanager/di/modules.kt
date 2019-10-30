package avila.domingo.cameramanager.di

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import avila.domingo.camera.*
import avila.domingo.camera.cameranative.INativeCamera
import avila.domingo.camera.cameranative.INativeFlash
import avila.domingo.camera.cameranative.NativeCameraManager
import avila.domingo.camera.model.ScreenSize
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.camera.util.CameraRotationUtil
import avila.domingo.cameramanager.di.qualifiers.CameraId
import avila.domingo.cameramanager.di.qualifiers.RangeForPicture
import avila.domingo.cameramanager.di.qualifiers.RangeForPreview
import avila.domingo.cameramanager.model.mapper.ImageMapper
import avila.domingo.cameramanager.model.mapper.UiCameraSideMapper
import avila.domingo.cameramanager.model.mapper.UiFlashModeMapper
import avila.domingo.cameramanager.schedulers.IScheduleProvider
import avila.domingo.cameramanager.schedulers.ScheduleProviderImp
import avila.domingo.cameramanager.ui.MainActivityViewModel
import avila.domingo.domain.ICamera
import avila.domingo.domain.IFlash
import avila.domingo.domain.interactor.*
import avila.domingo.flash.FlashImp
import avila.domingo.flash.model.mapper.FlashModeMapper
import avila.domingo.lifecycle.ILifecycleObserver
import avila.domingo.lifecycle.LifecycleManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    single { (androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay }
}

val activityModule = module {
    factory { (lifecycle: Lifecycle) ->
        LifecycleManager(arrayOf(get()), lifecycle)
        Unit
    }
}

val viewModelModule = module {
    viewModel {
        MainActivityViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

val useCaseModule = module {
    single { SetFlashModeUseCase(get()) }
    single { GetFlashModeUseCase(get()) }
    single { TakePreviewImageUseCase(get()) }
    single { TakePictureImageUseCase(get()) }
    single { SwitchCameraUseCase(get()) }
    single { GetCameraSideUseCase(get()) }
}

@Suppress("DEPRECATION")
val cameraModule = module {
    single<ICamera> { CameraImp(get(), get()) }

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
            get(RangeForPreview),
            get(RangeForPicture),
            get(),
            get(),
            get(CameraId),
            get()
        )
    } binds arrayOf(
        INativeCamera::class,
        INativeFlash::class,
        ILifecycleObserver::class
    )

    single { CameraRotationUtil(get()) }

    factory {
        Point().apply { (get() as Display).getSize(this) }.let { point ->
            if (point.x > point.y) {
                ScreenSize(point.x, point.y)
            } else {
                ScreenSize(point.y, point.x)
            }
        }
    }

    single(RangeForPicture) { 640..2160 }

    single(RangeForPreview) { 640..2160 }

    single(CameraId) { android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK }

    single { android.hardware.Camera.Parameters.FLASH_MODE_OFF }
}

val flashModule = module {
    single<IFlash> { FlashImp(get(), get()) }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { CameraSideMapper() }
    single { UiCameraSideMapper() }
    single { FlashModeMapper() }
    single { UiFlashModeMapper() }
    single { ImageMapper() }
}