package avila.domingo.domain.interactor

import avila.domingo.domain.IFlash
import avila.domingo.domain.interactor.type.CompletableUseCase
import io.reactivex.Completable

class FlashOffUseCase(private val flash: IFlash) : CompletableUseCase {
    override fun execute(): Completable = flash.off()

}