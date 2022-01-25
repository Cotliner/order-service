package cotliner.orderservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration class SharedDataConfig(
  /* PROPERTIES */
  @Value("\${spring.mail.values.notification}") private val notification: String,
  /* BEANS */
  private val mailSender: JavaMailSender
) {
  /* TODO-2: UNCOMMENT THIS CODE */
  //
  // @DelicateCoroutinesApi
  // @Bean fun orderMailer(): MutableSharedFlow<Order> = MutableSharedFlow<Order>().consumeWith { it.sendMail() }
  //
  //  @DelicateCoroutinesApi
  //  private fun <T> MutableSharedFlow<T>.consumeWith(
  //    consumer: (it: T) -> Unit
  //  ): MutableSharedFlow<T> = this.also { GlobalScope.launch { onEach(consumer::invoke).collect() } }
}