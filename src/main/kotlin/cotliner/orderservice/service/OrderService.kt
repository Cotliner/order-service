package cotliner.orderservice.service

import cotliner.orderservice.commons.SearchParam
import cotliner.orderservice.repository.OrderRepository
import cotliner.serversideevent.document.order.Order
import cotliner.serversideevent.document.order.dto.OrderInputDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Service class OrderService(
  /* PROPERTIES */
  @Value("\${spring.mail.values.notification}") private val notification: String,
  /* BEANS */
  private val mailSender: JavaMailSender,
  /* REPOSITORIES */
  private val orderRepository: OrderRepository
) {
  fun findAll(): List<Order> = orderRepository.findAll()

  fun create(orderToCreate: OrderInputDto): Order = orderRepository.save(Order(
    UUID.randomUUID(),
    "CREATED",
    Random.nextDouble(1.0, 500.0),
    orderToCreate.buyerMail!!
  )).also { it.sendMail() }

  fun update(
    orderId: UUID,
    orderToUpdate: OrderInputDto
  ): Order = with(
    orderRepository.findById(orderId).orElseThrow()
  ) {
    this.status = orderToUpdate.status!!
    orderRepository.save(this)
  }.also { it.sendMail() }

  fun search(param: SearchParam, request: PageRequest): Page<Order> = with(
    param
  ) { orderRepository.findAllByPriceBetween(startPrice, endPrice, request) }

  fun delete(id: UUID): Unit = orderRepository.deleteById(id)

  private fun Order.sendMail(): Unit = with(SimpleMailMessage()) {
    setFrom(notification)
    setTo(buyerMail)
    setSubject("Order with id $id")
    setText("Status: $status")
    mailSender.send(this)
  }
}
