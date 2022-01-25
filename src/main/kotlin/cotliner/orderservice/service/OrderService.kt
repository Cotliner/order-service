package cotliner.orderservice.service

import cotliner.orderservice.commons.SearchParam
import cotliner.orderservice.repository.OrderRepository
import cotliner.orderservice.document.order.Order
import cotliner.orderservice.document.order.dto.OrderInputDto
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

@Service class OrderService(
  /* PROPERTIES */ /* TODO-3: REMOVE THIS CODE */
  @Value("\${spring.mail.values.notification}") private val notification: String, /* TODO-3: REMOVE THIS CODE */
  /* BEANS */ /* TODO-5: CHANGE BEAN TO SHARED FLOW */
  private val mailSender: JavaMailSender, /* TODO-4: REPLACE THIS BEAN BY private val orderMailer: MutableSharedFlow<Order>, */
  /* REPOSITORIES */
  private val orderRepository: OrderRepository
) {
  fun findAll(): Flow<Order> = orderRepository.findAll()

  suspend fun create(orderToCreate: OrderInputDto): Order = orderRepository.save(Order(
    randomUUID(),
    "CREATED",
    nextDouble(1.0, 500.0),
    orderToCreate.buyerMail!!
  )).also { it.sendMail() } /* TODO-6: CALL CHANNEL orderMailer */

  suspend fun update(
    orderId: UUID,
    orderToUpdate: OrderInputDto
  ): Order = with(
    orderRepository.findById(orderId) ?: throw Exception()
  ) {
    this.status = orderToUpdate.status!!
    orderRepository.save(this)
  }.also { it.sendMail() } /* TODO-7: CALL CHANNEL orderMailer */

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

  /* TODO-1: MOVE THIS METHOD IN SharedDataConf CLASS */
  private fun Order.sendMail(): Unit = with(SimpleMailMessage()) {
    setFrom(notification)
    setTo(buyerMail)
    setSubject("Order with id $id")
    setText("Status: $status")
    mailSender.send(this)
  }
}
