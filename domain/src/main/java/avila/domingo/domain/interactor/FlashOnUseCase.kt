package avila.domingo.domain.interactor

import avila.domingo.domain.IFlash
import avila.domingo.domain.interactor.type.CompletableUseCase
import avila.domingo.domain.interactor.type.CompletableUseCaseWithParameter
import avila.domingo.domain.model.CameraSide
import io.reactivex.Completable

class FlashOnUseCase(private val flash: IFlash) : CompletableUseCase {
    override fun execute(): Completable = flash.on()

}