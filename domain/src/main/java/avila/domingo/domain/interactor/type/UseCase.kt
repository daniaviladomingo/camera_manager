package avila.domingo.domain.interactor.type

import io.reactivex.Observable

interface UseCase<P> {
    fun execute(): Observable<P>
}