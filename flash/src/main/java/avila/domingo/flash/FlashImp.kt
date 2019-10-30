@file:Suppress("DEPRECATION")

package avila.domingo.flash

import avila.domingo.camera.cameranative.INativeFlash
import avila.domingo.domain.IFlash
import avila.domingo.domain.model.FlashMode
import avila.domingo.flash.model.mapper.FlashModeMapper
import io.reactivex.Completable
import io.reactivex.Single

class FlashImp(
    private val nativeFlash: INativeFlash,
    private val flashModeMapper: FlashModeMapper
) : IFlash {
    override fun mode(mode: FlashMode): Completable = Completable.create {
        nativeFlash.mode(flashModeMapper.map(mode))
        it.onComplete()
    }

    override fun mode(): Single<FlashMode> = Single.create {
        it.onSuccess(flashModeMapper.inverseMap(nativeFlash.mode()))
    }
}