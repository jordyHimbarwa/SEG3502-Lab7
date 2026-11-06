package seg3502.book_rest_api.representation

/** Request body for creating or updating an order. */
data class OrderRequest(
        var bookId: Long = 0,
        var quantity: Int = 0,
        var orderDate: String = ""
)
