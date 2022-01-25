package cotliner.orderservice.config

import cotliner.orderservice.document.event.Event
import cotliner.orderservice.document.order.Order
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

@Configuration class SharedDataConfig(
  /* PROPERTIES */
  @Value("\${spring.mail.values.notification}") private val notification: String,
  /* BEANS */
  private val mailSender: JavaMailSender
) {
  @Bean fun eventFlow(): MutableSharedFlow<Event> = MutableSharedFlow()

  @DelicateCoroutinesApi
  @Bean fun orderMailer(): MutableSharedFlow<Order> = MutableSharedFlow<Order>().consumeWith { it.sendMail() }

  @DelicateCoroutinesApi
  @Bean fun simpleMailer(): MutableSharedFlow<String> = MutableSharedFlow<String>().consumeWith { it.sendMail() }

  @DelicateCoroutinesApi
  private fun <T> MutableSharedFlow<T>.consumeWith(
    consumer: (it: T) -> Unit
  ): MutableSharedFlow<T> = this.also { GlobalScope.launch { buffer().onEach(consumer::invoke).collect() } }

  private fun Order.sendMail(): Unit = with(SimpleMailMessage()) {
    setFrom(notification)
    setTo(buyerMail)
    setSubject("Order with id $id")
    setText("Status: $status")
    mailSender.send(this@with)
  }

  private fun String.sendMail(): Unit = with(SimpleMailMessage()) {
    setFrom(notification)
    setTo(this@sendMail)
    setSubject("Order updated")
    setText("Status updated")
    mailSender.send(this)
  }
}