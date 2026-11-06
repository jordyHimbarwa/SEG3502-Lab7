package seg3502.book_rest_api.representation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
class OrderRepresentation : RepresentationModel<OrderRepresentation>() {
    var id: Long = 0
    var quantity: Int = 0
    var bookId: Long = 0
    var orderDate: String = "" // Format: YYYY-MM-DD
    var totalCost: Double = 0.0
}
