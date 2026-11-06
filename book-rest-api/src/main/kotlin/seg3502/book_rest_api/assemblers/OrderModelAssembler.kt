package seg3502.book_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import seg3502.book_rest_api.controller.ApiController
import seg3502.book_rest_api.entities.Order
import seg3502.book_rest_api.representation.OrderRepresentation

@Component
class OrderModelAssembler :
        RepresentationModelAssemblerSupport<Order, OrderRepresentation>(
                ApiController::class.java,
                OrderRepresentation::class.java
        ) {

    override fun toModel(entity: Order): OrderRepresentation {
        val orderRepresentation = instantiateModel(entity)
        orderRepresentation.id = entity.id
        orderRepresentation.quantity = entity.quantity
        orderRepresentation.orderDate = entity.orderDate
        orderRepresentation.bookId = entity.book.id
        orderRepresentation.totalCost = entity.quantity * entity.book.cost

        orderRepresentation.add(
                linkTo(methodOn(ApiController::class.java).getOrderById(entity.id)).withSelfRel()
        )
        orderRepresentation.add(
                linkTo(methodOn(ApiController::class.java).getBookById(entity.book.id))
                        .withRel("book")
        )
        return orderRepresentation
    }

    override fun toCollectionModel(
            entities: Iterable<Order>
    ): CollectionModel<OrderRepresentation> {
        val orderRepresentations = super.toCollectionModel(entities)
        orderRepresentations.add(
                linkTo(methodOn(ApiController::class.java).allOrders()).withSelfRel()
        )
        return orderRepresentations
    }
}
