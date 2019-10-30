package avila.domingo.domain.interactor

import avila.domingo.domain.IFlash
import avila.domingo.domain.interactor.type.CompletableUseCaseWithParameter
import avila.domingo.domain.model.FlashMode
import io.reactivex.Completable

class SetFlashModeUseCase(private val flash: IFlash) : CompletableUseCaseWithParameter<FlashMode> {
    override fun execute(parameter: FlashMode): Completable = flash.mode(parameter)
}