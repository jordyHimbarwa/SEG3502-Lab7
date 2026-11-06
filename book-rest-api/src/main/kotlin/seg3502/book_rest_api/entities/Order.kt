package seg3502.book_rest_api.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "BookOrder")
class Order {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0

    var quantity: Int = 0

    var orderDate: String = "" // Format: YYYY-MM-DD

    @field:JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    lateinit var book: Book
}
