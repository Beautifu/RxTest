package com.rubylichtenstein.rxkotlintest

import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldHave
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class AssertionTest {
    @Test
    fun completeTest() {
        Observable.just("a")
                .test {
                    it.assertComplete()
                    it should complete()
                }
    }

    @Test
    fun notCompleteTest() {
        PublishSubject.create<String>()
                .apply {
                    onNext("a")
                    onNext("b")
                }
                .test {
                    it.assertNotComplete()
                    it should notComplete()
                }
    }

    @Test
    fun errorTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        val assertionError = AssertionError()
        publishSubject.subscribe(to)
        publishSubject.onNext("a")
        publishSubject.onError(assertionError)

        to.assertError(assertionError)
        to shouldHave error(assertionError)
        to.assertError(AssertionError::class.java)
        to shouldHave error(AssertionError::class.java)

        to.assertError({ true })
        to shouldHave error({ true })
    }

    @Test
    fun noErrorTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        publishSubject.subscribe(to)
        publishSubject.onNext("a")

        to.assertNoErrors()
        to shouldHave noErrors()
    }

    @Test
    fun valueTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        val value = "a"
        publishSubject.subscribe(to)
        publishSubject.onNext(value)

        to.assertValue(value)
        to shouldHave value(value)

        to.assertValue({ it.equals(value) })
        to shouldHave value({ it.equals(value) })
    }

    @Test
    fun valueAtTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        val value0 = "a"
        val value1 = "b"
        publishSubject.subscribe(to)
        publishSubject.onNext(value0)
        publishSubject.onNext(value1)

        to.assertValueAt(0, value0)
        to.assertValueAt(1, value1)

        to shouldHave valueAt(0, value0)
        to shouldHave valueAt(1, value1)

        to.assertValueAt(0, { it == value0 })
        to.assertValueAt(1, { it == value1 })

        to shouldHave valueAt(0, { it == value0 })
        to shouldHave valueAt(1, { it == value1 })
    }

    @Test
    fun valuesTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        val value0 = "a"
        val value1 = "b"
        publishSubject.subscribe(to)
        publishSubject.onNext(value0)
        publishSubject.onNext(value1)

        to.assertValues(value0, value1)
        to shouldHave values(value0, value1)
    }

    @Test
    fun neverTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        val value = "a"
        val never = "b"
        publishSubject.subscribe(to)
        publishSubject.onNext(value)

        to.assertNever(never)
        to shouldHave never(never)

        to.assertNever({ it.equals(never) })
        to shouldHave never({ it.equals(never) })
    }

    @Test
    fun emptyTest() {
        val to = TestObserver<String>()
        val publishSubject = PublishSubject.create<String>()
        publishSubject.subscribe(to)

        to.assertEmpty()
        to should empty()
    }

    @Test
    fun failureTest() {
        val value0 = "a"
        val value1 = "b"
        val error = Throwable()
        PublishSubject.create<String>()
                .also {
                    it.onNext(value0)
                    it.onNext(value1)
                    it.onError(error)
                }
                .test {
                    //                    it.assertFailure(true, value0)
//                    it shouldHave failure()
                }
    }

    @Test
    fun resultTest() {
        val value0 = "a"
        val value1 = "b"
        val values = listOf(value0, value1)
        Observable.just(values)
                .test {
                    it.assertResult(values)
                    it shouldHave result(values)
                }
    }

    @Test
    fun subscribeTest() {
        val value0 = "a"
        Observable.just(value0).apply {
            subscribe({ })
            test {
                it.assertSubscribed()
                it should subscribed()
            }
        }
    }

    @Test
    fun notSubscribeTest() {
        val value0 = "a"
        Observable.just(value0)
                .test {
                    it.assertSubscribed()
                    it should subscribed()
                }

    }
}
