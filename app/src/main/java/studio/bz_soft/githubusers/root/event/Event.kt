package studio.bz_soft.githubusers.root.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class Event<out T>(private val content: T) {

    private var isHandled: Boolean = false

    fun handle(block: (T) -> Unit) = when (isHandled) {
        true -> Unit
        false -> {
            isHandled = true
            block(content)
        }
    }

    override fun toString(): String = "Event(content => $content)"
}

suspend fun <T> Flow<Event<T>?>.collectEvent(block: (T) -> Unit) {
    collect { it?.handle(block) }
}

fun <T> LiveData<Event<T>?>.collectEvent(owner: LifecycleOwner, block: (T) -> Unit) {
    observe(owner) { e -> e?.handle(block) }
}