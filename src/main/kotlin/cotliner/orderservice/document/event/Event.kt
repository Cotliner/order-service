package cotliner.orderservice.document.event

import cotliner.orderservice.document.event.enums.EventType

sealed class Event {
  abstract val type: EventType
  data class StandardEvent<T>(val content: T, override val type: EventType, val buyerMail: String? = null): Event()
}
