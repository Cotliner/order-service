package cotliner.orderservice.repository

import cotliner.orderservice.document.order.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository interface OrderRepository: CoroutineCrudRepository<Order, UUID> {
  /* SEARCH QUERIES */
  fun findAllByPriceBetween(startPrice: Number, endPrice: Number, pageable: Pageable): Page<Order> /* TODO-1: MAKE THIS REQUEST REACTIVE */
}
