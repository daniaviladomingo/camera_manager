package avila.domingo.cameramanager.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.android.ILifecycleUpdate
import avila.domingo.camera.*
import avila.domingo.camera.model.mapper.CameraSideMapper
import avila.domingo.cameramanager.di.qualifiers.ForActivity
import avila.domingo.cameramanager.di.qualifiers.ForApplication
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

var nativeCameraManager: ILifecycleUpdate? = null

val appModule = module {
    single(ForApplication) { androidContext() }
    single { (get(ForApplication) as Context).getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    lateinit var activityReference: AppCompatActivity

    factory { (activity: AppCompatActivity) ->
        nativeCameraManager?.update(activity.lifecycle)
        activityReference = activity
        Unit
    }

    factory<Context>(ForActivity) { activityReference }

    factory { activityReference.lifecycle }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val useCaseModule = module {
    factory { FlashOffUseCase(get()) }
    factory { FlashOnUseCase(get()) }
    factory { TakePreviewImageUseCase(get()) }
    factory { SwitchCameraUseCase(get()) }
    factory { TakePictureImageUseCase(get()) }
}

val cameraModule = module {
    factory<ICamera> { CameraImp(get(), get(), get()) }

    single {
        SurfaceView(get()).apply {
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
            get(),
            get()
        ).apply {
            nativeCameraManager = this
        }
    } binds arrayOf(
        ICameraSide::class,
        INativeCamera::class,
        ISwitchCamera::class,
        ILifecycleUpdate::class
    )

    single { CameraRotationUtil(get(), get(), get()) }

    single { CameraSide.BACK }

    single(RangeForPicture) { 640..2160 }

    single(RangeForPreview) { 640..2160 }
}

val flashModule = module {
    factory<IFlash> { FlashImp(get()) }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { CameraSideMapper() }
    single { ImageMapper() }
}