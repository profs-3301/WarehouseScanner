package  com.goodswarehouse.scanner.data.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RxBus<T: Any> @Inject constructor() {

    private val subject: PublishSubject<T> = PublishSubject.create()

    fun post(event: T) {
        subject.onNext(event)
    }

    fun getEvents(): Observable<T> = subject

}