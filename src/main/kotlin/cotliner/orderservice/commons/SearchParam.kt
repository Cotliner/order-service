package cotliner.orderservice.commons

data class SearchParam(
  val startPrice: Number = 0,
  val endPrice: Number = Int.MAX_VALUE
)
