package cotliner.orderservice.service

import cotliner.orderservice.repository.OrderRepository
import cotliner.orderservice.document.order.Order
import cotliner.orderservice.document.order.dto.OrderInputDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
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

  /* TODO-1: MAKE THIS METHOD REACTIVE */
  fun create(orderToCreate: OrderInputDto): Order = runBlocking { orderRepository.save(Order(
    randomUUID(),
    "CREATED",
    nextDouble(1.0, 500.0),
    orderToCreate.buyerMail!!
  )) }

  /* TODO-2: MAKE THIS METHOD REACTIVE */
  fun update(
    orderId: UUID,
    orderToUpdate: OrderInputDto
  ): Order = runBlocking { with(
    orderRepository.findById(orderId) ?: throw Exception()
  ) {
    this.status = orderToUpdate.status!!
    orderRepository.save(this)
  } }

  //  fun search(param: SearchParam, request: PageRequest): Page<Order> = with(
  //    param
  //  ) { orderRepository.findAllByPriceBetween(startPrice, endPrice, request) }
  //
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
