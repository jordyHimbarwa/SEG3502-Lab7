package seg3502.book_rest_api.controller

import io.swagger.v3.oas.annotations.Operation
import java.net.URI
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import seg3502.book_rest_api.assemblers.AuthorModelAssembler
import seg3502.book_rest_api.assemblers.BioModelAssembler
import seg3502.book_rest_api.assemblers.BookModelAssembler
import seg3502.book_rest_api.assemblers.OrderModelAssembler
import seg3502.book_rest_api.entities.Author
import seg3502.book_rest_api.entities.Bio
import seg3502.book_rest_api.entities.Book
import seg3502.book_rest_api.entities.Order
import seg3502.book_rest_api.repository.AuthorRepository
import seg3502.book_rest_api.repository.BioRepository
import seg3502.book_rest_api.repository.BookRepository
import seg3502.book_rest_api.repository.OrderRepository
import seg3502.book_rest_api.representation.AuthorRepresentation
import seg3502.book_rest_api.representation.BioRepresentation
import seg3502.book_rest_api.representation.BookRepresentation
import seg3502.book_rest_api.representation.OrderRepresentation
import seg3502.book_rest_api.representation.OrderRequest

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("books-api", produces = ["application/hal+json"])
class ApiController(
        val authorRepository: AuthorRepository,
        val bookRepository: BookRepository,
        val bioRepository: BioRepository,
        val orderRepository: OrderRepository,
        val authorAssembler: AuthorModelAssembler,
        val bookAssembler: BookModelAssembler,
        val orderAssembler: OrderModelAssembler,
        val bioAssembler: BioModelAssembler
) {
    // --------------------- Book Operations --------------------- //

    @Operation(summary = "Get all books")
    @GetMapping("/books")
    fun allBooks(): ResponseEntity<CollectionModel<BookRepresentation>> {
        val books = bookRepository.findAll()
        return ResponseEntity(bookAssembler.toCollectionModel(books), HttpStatus.OK)
    }

    @Operation(summary = "Get a book by id")
    @GetMapping("/books/{id}")
    fun getBookById(@PathVariable("id") id: Long): ResponseEntity<BookRepresentation> {
        return bookRepository
                .findById(id)
                .map { entity: Book -> bookAssembler.toModel(entity) }
                .map { body: BookRepresentation -> ResponseEntity.ok(body) }
                .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Add a new book")
    @PostMapping("/books")
    fun addBook(@RequestBody book: Book): ResponseEntity<Any> {
        return try {
            val newBook = bookRepository.save(book)
            val location: URI =
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(newBook.id)
                            .toUri()
            ResponseEntity.created(location).body(bookAssembler.toModel(newBook))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Update the information of a book")
    @PutMapping("/books/{id}")
    fun updateBook(@PathVariable("id") id: Long, @RequestBody book: Book): ResponseEntity<Any> {
        val currBook = bookRepository.findById(id).orElse(null)
                ?: return ResponseEntity.notFound().build()
        currBook.title = book.title
        currBook.isbn = book.isbn
        currBook.cost = book.cost
        currBook.category = book.category
        currBook.description = book.description
        currBook.year = book.year
        bookRepository.save(currBook)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Remove a book")
    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val book = bookRepository.findById(id).orElse(null)
                ?: return ResponseEntity.notFound().build()
        // Author owns the many-to-many link, so detach the book from its authors first
        book.authors.forEach { author ->
            author.books.remove(book)
            authorRepository.save(author)
        }
        bookRepository.delete(book)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Get all orders for a specific book")
    @GetMapping("/books/{id}/orders")
    fun getBookOrdersById(
            @PathVariable("id") id: Long
    ): ResponseEntity<CollectionModel<OrderRepresentation>> {
        if (!bookRepository.existsById(id)) return ResponseEntity.notFound().build()
        val orders = orderRepository.findByBookId(id)
        return ResponseEntity.ok(orderAssembler.toCollectionModel(orders))
    }

    // --------------------- Author Operations --------------------- //

    @Operation(summary = "Get all authors")
    @GetMapping("/authors")
    fun allAuthors(): ResponseEntity<CollectionModel<AuthorRepresentation>> {
        val authors = authorRepository.findAll()
        return ResponseEntity(authorAssembler.toCollectionModel(authors), HttpStatus.OK)
    }

    @Operation(summary = "Get an author by id")
    @GetMapping("/authors/{id}")
    fun getAuthorById(@PathVariable("id") id: Long): ResponseEntity<AuthorRepresentation> {
        return authorRepository
                .findById(id)
                .map { entity: Author -> authorAssembler.toModel(entity) }
                .map { body: AuthorRepresentation -> ResponseEntity.ok(body) }
                .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Add a new author")
    @PostMapping("/authors")
    fun addAuthor(@RequestBody author: Author): ResponseEntity<Any> {
        return try {
            val newAuthor = authorRepository.save(author)
            val location: URI =
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(newAuthor.id)
                            .toUri()
            ResponseEntity.created(location).body(authorAssembler.toModel(newAuthor))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Update the information of an author")
    @PutMapping("/authors/{id}")
    fun updateAuthor(
            @PathVariable("id") id: Long,
            @RequestBody author: Author
    ): ResponseEntity<Any> {
        val currAuthor = authorRepository.findById(id).orElse(null)
                ?: return ResponseEntity.notFound().build()
        currAuthor.firstName = author.firstName
        currAuthor.lastName = author.lastName
        authorRepository.save(currAuthor)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Remove an author")
    @DeleteMapping("/authors/{id}")
    fun deleteAuthor(@PathVariable("id") id: Long): ResponseEntity<Any> {
        if (!authorRepository.existsById(id)) return ResponseEntity.notFound().build()
        authorRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // --------------------- Bio Operations --------------------- //

    @Operation(summary = "Get all bios")
    @GetMapping("/bios")
    fun allBios(): ResponseEntity<CollectionModel<BioRepresentation>> {
        val bios = bioRepository.findAll()
        return ResponseEntity(bioAssembler.toCollectionModel(bios), HttpStatus.OK)
    }

    @Operation(summary = "Get a bio by id")
    @GetMapping("/bios/{id}")
    fun getBioById(@PathVariable("id") id: Long): ResponseEntity<BioRepresentation> {
        return bioRepository
                .findById(id)
                .map { entity: Bio -> bioAssembler.toModel(entity) }
                .map { body: BioRepresentation -> ResponseEntity.ok(body) }
                .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Add a new bio")
    @PostMapping("/bios")
    fun addBio(@RequestBody bio: Bio): ResponseEntity<Any> {
        return try {
            val newBio = bioRepository.save(bio)
            val location: URI =
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(newBio.id)
                            .toUri()
            ResponseEntity.created(location).body(bioAssembler.toModel(newBio))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Update the information of a bio")
    @PutMapping("/bios/{id}")
    fun updateBio(@PathVariable("id") id: Long, @RequestBody bio: Bio): ResponseEntity<Any> {
        val currBio = bioRepository.findById(id).orElse(null)
                ?: return ResponseEntity.notFound().build()
        currBio.biodata = bio.biodata
        bioRepository.save(currBio)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Remove a bio")
    @DeleteMapping("/bios/{id}")
    fun deleteBio(@PathVariable("id") id: Long): ResponseEntity<Any> {
        if (!bioRepository.existsById(id)) return ResponseEntity.notFound().build()
        bioRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // --------------------- Order Operations --------------------- //

    @Operation(summary = "Get all orders")
    @GetMapping("/orders")
    fun allOrders(): ResponseEntity<CollectionModel<OrderRepresentation>> {
        val orders = orderRepository.findAll()
        return ResponseEntity(orderAssembler.toCollectionModel(orders), HttpStatus.OK)
    }

    @Operation(summary = "Get an order by id")
    @GetMapping("/orders/{id}")
    fun getOrderById(@PathVariable("id") id: Long): ResponseEntity<OrderRepresentation> {
        return orderRepository
                .findById(id)
                .map { entity: Order -> orderAssembler.toModel(entity) }
                .map { body: OrderRepresentation -> ResponseEntity.ok(body) }
                .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Add a new order (body: bookId, quantity, orderDate)")
    @PostMapping("/orders")
    fun addOrder(@RequestBody request: OrderRequest): ResponseEntity<Any> {
        if (request.quantity <= 0) return ResponseEntity.badRequest().build()
        val book = bookRepository.findById(request.bookId).orElse(null)
                ?: return ResponseEntity.badRequest().build()

        val order = Order()
        order.book = book
        order.quantity = request.quantity
        order.orderDate = request.orderDate
        val newOrder = orderRepository.save(order)

        val location: URI =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newOrder.id)
                        .toUri()
        return ResponseEntity.created(location).body(orderAssembler.toModel(newOrder))
    }

    @Operation(summary = "Update the information of an order (quantity, orderDate)")
    @PutMapping("/orders/{id}")
    fun updateOrder(
            @PathVariable("id") id: Long,
            @RequestBody request: OrderRequest
    ): ResponseEntity<Any> {
        val currOrder = orderRepository.findById(id).orElse(null)
                ?: return ResponseEntity.notFound().build()
        if (request.quantity <= 0) return ResponseEntity.badRequest().build()
        currOrder.quantity = request.quantity
        currOrder.orderDate = request.orderDate
        orderRepository.save(currOrder)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Remove an order")
    @DeleteMapping("/orders/{id}")
    fun deleteOrder(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val order = orderRepository.findById(id).orElse(null)
                ?: return ResponseEntity.notFound().build()
        // Book.orders uses orphanRemoval, so remove from the parent collection too
        order.book.orders.remove(order)
        orderRepository.delete(order)
        return ResponseEntity.noContent().build()
    }
}
