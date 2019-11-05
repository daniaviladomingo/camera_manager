package avila.domingo.cameramanager.ui

import android.graphics.Bitmap
import avila.domingo.cameramanager.base.BaseViewModel
import avila.domingo.cameramanager.model.mapper.ImageMapper
import avila.domingo.cameramanager.model.mapper.UiCameraSideMapper
import avila.domingo.cameramanager.model.mapper.UiFlashModeMapper
import avila.domingo.cameramanager.schedulers.IScheduleProvider
import avila.domingo.cameramanager.ui.data.Resource
import avila.domingo.cameramanager.util.SingleLiveEvent
import avila.domingo.domain.interactor.*

class MainActivityViewModel(
    private val setFlashModeUseCase: SetFlashModeUseCase,
    private val getFlashModeUseCase: GetFlashModeUseCase,
    private val takePictureImageUseCase: TakePictureImageUseCase,
    private val takePreviewImageUseCase: TakePreviewImageUseCase,
    private val switchCameraUseCase: SwitchCameraUseCase,
    private val getCameraSideUseCase: GetCameraSideUseCase,
    private val imageMapper: ImageMapper,
    private val flashModeMapper: UiFlashModeMapper,
    private val cameraSideMapper: UiCameraSideMapper,
    private val scheduleProvider: IScheduleProvider
) : BaseViewModel() {

    val flashModeLiveData = SingleLiveEvent<Resource<Boolean>>()
    val cameraSideLiveData = SingleLiveEvent<Resource<Boolean>>()

    val takeImageLiveData = SingleLiveEvent<Resource<Bitmap>>()
    val switchCameraLiveData = SingleLiveEvent<Resource<Any?>>()
    val flashLiveData = SingleLiveEvent<Resource<Any?>>()

    fun getFlashMode() {
        flashModeLiveData.value = Resource.loading()
        addDisposable(getFlashModeUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({ mode ->
                flashModeLiveData.value = Resource.success(flashModeMapper.inverseMap(mode))
            }) {
                flashModeLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun getCameraSide() {
        cameraSideLiveData.value = Resource.loading()
        addDisposable(getCameraSideUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({ side ->
                cameraSideLiveData.value = Resource.success(cameraSideMapper.inverseMap(side))
            }) {
                cameraSideLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun takePicture() {
        takeImageLiveData.value = Resource.loading()
        addDisposable(takePictureImageUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({ picture ->
                takeImageLiveData.value = Resource.success(imageMapper.map(picture))
            }) {
                takeImageLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun takePreview() {
        takeImageLiveData.value = Resource.loading()
        addDisposable(takePreviewImageUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({ preview ->
                takeImageLiveData.value = Resource.success(imageMapper.map(preview))
            }) {
                takeImageLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun switchCamera(back: Boolean) {
        switchCameraLiveData.value = Resource.loading()
        addDisposable(switchCameraUseCase.execute(cameraSideMapper.map(back))
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({
                switchCameraLiveData.value = Resource.success(null)
            }) {
                switchCameraLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun flashMode(on: Boolean) {
        flashLiveData.value = Resource.loading()
        addDisposable(setFlashModeUseCase.execute(flashModeMapper.map(on))
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({
                flashLiveData.value = Resource.success(null)
            }) {
                flashLiveData.value = Resource.error(it.localizedMessage)
            })
    }
}