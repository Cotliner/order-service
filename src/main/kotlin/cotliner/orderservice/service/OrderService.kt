package cotliner.orderservice.service

import cotliner.orderservice.commons.SearchParam
import cotliner.orderservice.document.event.Event
import cotliner.orderservice.document.event.Event.StandardEvent
import cotliner.orderservice.document.event.enums.EventType.ORDER_CREATED
import cotliner.orderservice.document.event.enums.EventType.ORDER_UPDATED
import cotliner.orderservice.repository.OrderRepository
import cotliner.orderservice.document.order.Order
import cotliner.orderservice.document.order.dto.OrderInputDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

@Service class OrderService(
  /* SHARED FLOW */
  private val eventFlow: MutableSharedFlow<Event>,
  private val orderMailer: MutableSharedFlow<Order>,
  private val simpleMailer: MutableSharedFlow<String>,
  /* REPOSITORIES */
  private val orderRepository: OrderRepository
) {
  fun findAll(): Flow<Order> = orderRepository.findAll()

  suspend fun create(orderToCreate: OrderInputDto): Order = orderRepository.save(Order(
    randomUUID(),
    "CREATED",
    nextDouble(1.0, 500.0),
    orderToCreate.buyerMail!!
  )).also {
    eventFlow.emit(StandardEvent(it.id, ORDER_CREATED, it.buyerMail))
    orderMailer.emit(it)
    CoroutineScope(Default).launch { randomMail().collect() }
  }

  suspend fun update(
    orderId: UUID,
    orderToUpdate: OrderInputDto
  ): Order = with(
    orderRepository.findById(orderId) ?: throw Exception()
  ) {
    this.status = orderToUpdate.status!!
    orderRepository.save(this)
  }.also {
    eventFlow.emit(StandardEvent(it.id, ORDER_UPDATED, it.buyerMail))
    orderMailer.emit(it)
    CoroutineScope(Default).launch { randomMail().collect() }
  }

  fun search(
    param: SearchParam,
    request: PageRequest
  ): Flow<Order> = with(
    param
  ) { orderRepository.findAllByPriceBetween(startPrice, endPrice, request) }

  suspend fun searchCount(
    param: SearchParam
  ): Number = with(
    param
  ) { orderRepository.countByPriceBetween(startPrice, endPrice) }

  suspend fun delete(id: UUID): Unit = orderRepository.deleteById(id)

  private fun randomMail(): Flow<Int> = (1..20000).asFlow().onEach { simpleMailer.emit("${randomUUID()}@europcar.com") }
}
