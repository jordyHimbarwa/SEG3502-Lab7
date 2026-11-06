package seg3502.book_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import seg3502.book_rest_api.controller.ApiController
import seg3502.book_rest_api.entities.Author
import seg3502.book_rest_api.entities.Book
import seg3502.book_rest_api.representation.AuthorNameRepresentation
import seg3502.book_rest_api.representation.BookRepresentation

@Component
class BookModelAssembler :
        RepresentationModelAssemblerSupport<Book, BookRepresentation>(
                ApiController::class.java,
                BookRepresentation::class.java
        ) {

    override fun toModel(entity: Book): BookRepresentation {
        val bookRepresentation = instantiateModel(entity)

        bookRepresentation.add(
                linkTo(methodOn(ApiController::class.java).getBookById(entity.id)).withSelfRel()
        )
        bookRepresentation.add(
                linkTo(methodOn(ApiController::class.java).getBookOrdersById(entity.id))
                        .withRel("orders")
        )

        bookRepresentation.id = entity.id
        bookRepresentation.isbn = entity.isbn
        bookRepresentation.category = entity.category
        bookRepresentation.title = entity.title
        bookRepresentation.cost = entity.cost
        bookRepresentation.year = entity.year
        bookRepresentation.description = entity.description
        bookRepresentation.authors = toAuthorsRepresentation(entity.authors)

        return bookRepresentation
    }

    override fun toCollectionModel(entities: Iterable<Book>): CollectionModel<BookRepresentation> {
        val bookRepresentations = super.toCollectionModel(entities)
        bookRepresentations.add(linkTo(methodOn(ApiController::class.java).allBooks()).withSelfRel())
        return bookRepresentations
    }

    private fun toAuthorsRepresentation(authors: List<Author>): List<AuthorNameRepresentation> =
            authors.map { authorRepresentation(it) }

    private fun authorRepresentation(author: Author): AuthorNameRepresentation {
        val representation = AuthorNameRepresentation()
        representation.firstName = author.firstName
        representation.lastName = author.lastName
        representation.add(
                linkTo(methodOn(ApiController::class.java).getAuthorById(author.id)).withSelfRel()
        )
        return representation
    }
}
