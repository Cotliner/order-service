package cotliner.orderservice.repository

import cotliner.orderservice.document.order.Order
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository interface OrderRepository: MongoRepository<Order, UUID> { /* TODO-6: MAKE REPOSITORY REACTIVE */
  /* SEARCH QUERIES */
  // fun findAllByPriceBetween(startPrice: Number, endPrice: Number, pageable: Pageable): Page<Order>
}
