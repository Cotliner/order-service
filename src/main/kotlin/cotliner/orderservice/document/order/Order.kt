package cotliner.serversideevent.document.order

import org.springframework.data.annotation.Id
import java.util.*

data class Order(@Id val id: UUID, var status: String, val price: Double, val buyerMail: String)
