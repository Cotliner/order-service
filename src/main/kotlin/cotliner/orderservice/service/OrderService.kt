package cotliner.orderservice.service

import cotliner.orderservice.commons.SearchParam
import cotliner.orderservice.repository.OrderRepository
import cotliner.orderservice.document.order.Order
import cotliner.orderservice.document.order.dto.OrderInputDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

@Service class OrderService(
  /* PROPERTIES */
  @Value("\${spring.mail.values.notification}") private val notification: String,
  /* BEANS */
  private val mailSender: JavaMailSender,
  /* REPOSITORIES */
  private val orderRepository: OrderRepository
) {
  fun findAll(): Flow<Order> = orderRepository.findAll()

  suspend fun create(orderToCreate: OrderInputDto): Order = orderRepository.save(Order(
    randomUUID(),
    "CREATED",
    nextDouble(1.0, 500.0),
    orderToCreate.buyerMail!!
  ))

  suspend fun update(
    orderId: UUID,
    orderToUpdate: OrderInputDto
  ): Order = with(
    orderRepository.findById(orderId) ?: throw Exception()
  ) {
    this.status = orderToUpdate.status!!
    orderRepository.save(this)
  }

  /* TODO-2: RETURN ONLY THE FLOW */
  suspend fun search(param: SearchParam, request: PageRequest): Page<Order> = with(
    param
  ) {
    PageImpl(
      orderRepository.findAllByPriceBetween(startPrice, endPrice, request).toList(),
      request,
      orderRepository.countByPriceBetween(startPrice, endPrice)
    )
  }

  /* TODO-3: RETURN COUNT IN ANOTHER METHOD */

  //  fun delete(id: UUID): Unit = orderRepository.deleteById(id)
  //
  //  private fun Order.sendMail(): Unit = with(SimpleMailMessage()) {
  //    setFrom(notification)
  //    setTo(buyerMail)
  //    setSubject("Order with id $id")
  //    setText("Status: $status")
  //    mailSender.send(this)
  //  }
}
