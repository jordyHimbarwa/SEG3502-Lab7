package seg3502.book_rest_api.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import seg3502.book_rest_api.entities.Book

interface BookRepository : CrudRepository<Book, Long> {
    fun findByTitleContaining(title: String): List<Book>
    fun findByCategory(category: String): List<Book>

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    fun findByAuthorId(authorId: Long): List<Book>

    fun countByIdIsNotNull(): Long
}
