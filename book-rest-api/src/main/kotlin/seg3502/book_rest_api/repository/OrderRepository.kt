package seg3502.book_rest_api.repository

import org.springframework.data.repository.CrudRepository
import seg3502.book_rest_api.entities.Order

interface OrderRepository : CrudRepository<Order, Long> {
    fun findByBookId(bookId: Long): List<Order>
}
