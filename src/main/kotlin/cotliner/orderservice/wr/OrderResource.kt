package cotliner.orderservice.wr

import cotliner.orderservice.commons.SearchParam
import cotliner.orderservice.service.OrderService
import cotliner.orderservice.wr.OrderResource.Companion.URI
import cotliner.orderservice.document.order.Order
import cotliner.orderservice.document.order.dto.OrderInputDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.mono
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RequestMapping(value = [ URI ], produces = [MediaType.APPLICATION_JSON_VALUE] )
@RestController
class OrderResource(
  /* SERVICES */
  private val orderService: OrderService
) {
  companion object { const val URI: String = "/api/v1/orders" }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  fun getAll(): Flow<Order> = orderService.findAll()

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  fun create(@RequestBody orderInput: OrderInputDto): Mono<Order> = mono { orderService.create(orderInput) }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{id}")
  fun update(
    @PathVariable id: UUID,
    @RequestBody orderInput: OrderInputDto
  ): Mono<Order> = mono { orderService.update(id, orderInput) }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("search")
  fun search(
    @RequestBody param: SearchParam,
    @RequestParam page: Number,
    @RequestParam size: Number
  ): Flow<Order> = orderService.search(
    param,
    PageRequest.of(page.toInt(), size.toInt())
  )

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("search-count")
  fun searchCount(@RequestBody param: SearchParam): Mono<Number> = mono { orderService.searchCount(param) }

  /* TODO-2: MAKE THIS ENDPOINT REACTIVE */
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("{id}")
  fun delete(@PathVariable id: UUID): Unit = orderService.delete(id)
}