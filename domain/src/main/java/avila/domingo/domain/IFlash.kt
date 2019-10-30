package avila.domingo.domain

import avila.domingo.domain.model.FlashMode
import io.reactivex.Completable
import io.reactivex.Single

interface IFlash {
    fun mode(mode: FlashMode): Completable
    fun mode(): Single<FlashMode>
}