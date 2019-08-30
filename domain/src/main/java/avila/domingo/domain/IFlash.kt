package avila.domingo.domain

import io.reactivex.Completable

interface IFlash {
    fun on(): Completable
    fun off(): Completable
}