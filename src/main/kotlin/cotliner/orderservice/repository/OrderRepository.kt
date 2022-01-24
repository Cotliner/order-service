package cotliner.orderservice.repository

import cotliner.orderservice.document.order.Order
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository interface OrderRepository: CoroutineCrudRepository<Order, UUID> {
  /* SEARCH QUERIES */
  fun findAllByPriceBetween(startPrice: Number, endPrice: Number, pageable: Pageable): Flow<Order>
  /* COUNT QUERIES */
  suspend fun countByPriceBetween(startPrice: Number, endPrice: Number): Long
}
