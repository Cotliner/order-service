package cotliner.orderservice.service

import cotliner.orderservice.document.event.Event
import cotliner.orderservice.document.event.Event.StandardEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import org.springframework.stereotype.Service

@Service class EventService(
  /* SHARED FLOW */
  private val eventFlow: MutableSharedFlow<Event>,
) {
  fun getAll(buyerMail: String): Flow<Event> = eventFlow.buffer().filter { it.rules(buyerMail) }

  private fun Event.rules(buyerMail: String): Boolean = when (this) {
    is StandardEvent<*> -> simpleEventRules(buyerMail)
  }

  private fun StandardEvent<*>.simpleEventRules(buyerMail: String): Boolean = buyerMail == this.buyerMail
}