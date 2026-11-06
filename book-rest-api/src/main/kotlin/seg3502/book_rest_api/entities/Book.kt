package seg3502.book_rest_api.entities

import jakarta.persistence.*

@Entity
class Book {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
    var title: String = ""
    var category: String = ""
    var isbn: String = ""
    var cost: Double = 0.0
    var year: Int = 0
    var description: String = ""

    @ManyToMany(mappedBy = "books", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var authors: MutableList<Author> = ArrayList()

    @OneToMany(mappedBy = "book", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orders: MutableList<Order> = ArrayList()
}
