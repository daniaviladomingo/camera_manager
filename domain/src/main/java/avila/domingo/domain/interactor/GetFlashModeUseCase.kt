package avila.domingo.domain.interactor

import avila.domingo.domain.IFlash
import avila.domingo.domain.interactor.type.CompletableUseCase
import avila.domingo.domain.interactor.type.CompletableUseCaseWithParameter
import avila.domingo.domain.interactor.type.SingleUseCase
import avila.domingo.domain.model.FlashMode
import io.reactivex.Completable
import io.reactivex.Single

class GetFlashModeUseCase(private val flash: IFlash) : SingleUseCase<FlashMode> {
    override fun execute(): Single<FlashMode> = flash.mode()

}