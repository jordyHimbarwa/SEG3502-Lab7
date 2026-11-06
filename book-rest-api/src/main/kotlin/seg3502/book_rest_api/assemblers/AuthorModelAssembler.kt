package seg3502.book_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import seg3502.book_rest_api.controller.ApiController
import seg3502.book_rest_api.entities.Author
import seg3502.book_rest_api.entities.Book
import seg3502.book_rest_api.representation.AuthorRepresentation
import seg3502.book_rest_api.representation.BookTitleRepresentation

@Component
class AuthorModelAssembler :
        RepresentationModelAssemblerSupport<Author, AuthorRepresentation>(
                ApiController::class.java,
                AuthorRepresentation::class.java
        ) {

    override fun toModel(entity: Author): AuthorRepresentation {
        val authorRepresentation = instantiateModel(entity)

        authorRepresentation.add(
                linkTo(methodOn(ApiController::class.java).getAuthorById(entity.id)).withSelfRel()
        )
        entity.bio?.let { bio ->
            authorRepresentation.add(
                    linkTo(methodOn(ApiController::class.java).getBioById(bio.id)).withRel("bio")
            )
        }

        authorRepresentation.id = entity.id
        authorRepresentation.firstName = entity.firstName
        authorRepresentation.lastName = entity.lastName
        authorRepresentation.books = toBooksRepresentation(entity.books)

        return authorRepresentation
    }

    override fun toCollectionModel(
            entities: Iterable<Author>
    ): CollectionModel<AuthorRepresentation> {
        val authorRepresentations = super.toCollectionModel(entities)
        authorRepresentations.add(
                linkTo(methodOn(ApiController::class.java).allAuthors()).withSelfRel()
        )
        return authorRepresentations
    }

    private fun toBooksRepresentation(books: List<Book>): List<BookTitleRepresentation> =
            books.map { book ->
                val representation = BookTitleRepresentation()
                representation.id = book.id
                representation.title = book.title
                representation.add(
                        linkTo(methodOn(ApiController::class.java).getBookById(book.id))
                                .withSelfRel()
                )
                representation
            }
}
