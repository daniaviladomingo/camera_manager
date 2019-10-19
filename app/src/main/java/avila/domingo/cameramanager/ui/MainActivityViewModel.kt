package avila.domingo.cameramanager.ui

import android.graphics.Bitmap
import avila.domingo.cameramanager.base.BaseViewModel
import avila.domingo.cameramanager.model.mapper.ImageMapper
import avila.domingo.cameramanager.schedulers.IScheduleProvider
import avila.domingo.cameramanager.ui.data.Resource
import avila.domingo.cameramanager.util.SingleLiveEvent
import avila.domingo.domain.interactor.*
import avila.domingo.domain.model.CameraSide

class MainActivityViewModel(
    private val flashOnUseCase: FlashOnUseCase,
    private val flashOffUseCase: FlashOffUseCase,
    private val takePictureImageUseCase: TakePictureImageUseCase,
    private val takePreviewImageUseCase: TakePreviewImageUseCase,
    private val switchCameraUseCase: SwitchCameraUseCase,
    private val imageMapper: ImageMapper,
    private val scheduleProvider: IScheduleProvider
) : BaseViewModel() {

    val takeImageLiveData = SingleLiveEvent<Resource<Bitmap>>()
    val switchCameraLiveData = SingleLiveEvent<Resource<Any?>>()
    val flashLiveData = SingleLiveEvent<Resource<Any?>>()

    fun takePicture() {
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
        addDisposable(takePreviewImageUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({ preview ->
                takeImageLiveData.value = Resource.success(imageMapper.map(preview))
            }) {
                takeImageLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun switchCamera(cameraSide: CameraSide) {
        addDisposable(switchCameraUseCase.execute(cameraSide)
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({
                switchCameraLiveData.value = Resource.success(null)
            }) {
                switchCameraLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun flashOn() {
        addDisposable(flashOnUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({
                flashLiveData.value = Resource.success(null)
            }) {
                flashLiveData.value = Resource.error(it.localizedMessage)
            })
    }

    fun flashOff() {
        addDisposable(flashOffUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.computation())
            .subscribe({
                flashLiveData.value = Resource.success(null)
            }) {
                flashLiveData.value = Resource.error(it.localizedMessage)
            })
    }
}