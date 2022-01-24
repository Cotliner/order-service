package cotliner.orderservice.wr

import cotliner.orderservice.service.OrderService
import cotliner.orderservice.wr.OrderResource.Companion.URI
import cotliner.orderservice.document.order.Order
import cotliner.orderservice.document.order.dto.OrderInputDto
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
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

  /* TODO-3: MAKE THIS ENDPOINT REACTIVE */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  fun create(@RequestBody orderInput: OrderInputDto): Order = orderService.create(orderInput)

  /* TODO-4: MAKE THIS ENDPOINT REACTIVE */
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{id}")
  fun update(
    @PathVariable id: UUID,
    @RequestBody orderInput: OrderInputDto
  ): Order = orderService.update(id, orderInput)

  //  @ResponseStatus(HttpStatus.OK)
  //  @PostMapping("search")
  //  fun search(
  //    @RequestBody param: SearchParam,
  //    @RequestParam page: Number,
  //    @RequestParam size: Number
  //  ): Page<Order> = orderService.search(
  //    param,
  //    PageRequest.of(page.toInt(), size.toInt())
  //  )
  //
  //  @ResponseStatus(HttpStatus.NO_CONTENT)
  //  @DeleteMapping("{id}")
  //  fun delete(@PathVariable id: UUID): Unit = orderService.delete(id)
}