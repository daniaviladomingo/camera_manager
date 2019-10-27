package avila.domingo.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LifecycleManager(
    private val lifecycleObserver: Array<ILifecycleObserver>,
    lifecycle: Lifecycle
) : LifecycleObserver {
    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        lifecycleObserver.forEach { it.start() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        lifecycleObserver.forEach { it.stop() }
    }
}