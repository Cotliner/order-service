package cotliner.orderservice.repository

import cotliner.serversideevent.document.order.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository interface OrderRepository: MongoRepository<Order, UUID> {
  /* SEARCH QUERIES */
  fun findAllByPriceBetween(startPrice: Number, endPrice: Number, pageable: Pageable): Page<Order>
}
