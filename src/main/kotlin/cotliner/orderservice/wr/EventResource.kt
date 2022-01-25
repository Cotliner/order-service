package cotliner.orderservice.wr

import cotliner.orderservice.document.event.Event
import cotliner.orderservice.service.EventService
import cotliner.orderservice.wr.EventResource.Companion.URI
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RequestMapping(value = [URI], produces = [APPLICATION_JSON_VALUE])
@RestController class EventResource(
  /* SERVICES */
  private val eventService: EventService
) {
  companion object { const val URI: String = "/api/v1/events" }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun getAll(@RequestParam(required = true) buyerMail: String): Flow<Event> = eventService.getAll(buyerMail)
}