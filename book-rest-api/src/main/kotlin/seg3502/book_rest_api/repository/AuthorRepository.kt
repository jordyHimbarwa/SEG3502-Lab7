package seg3502.book_rest_api.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import seg3502.book_rest_api.entities.Author

interface AuthorRepository : CrudRepository<Author, Long> {
    @Query("select aut from Author aut where aut.firstName = :firstName and aut.lastName = :lastName")
    fun findAuthorsByName(firstName: String, lastName: String): List<Author>
}
