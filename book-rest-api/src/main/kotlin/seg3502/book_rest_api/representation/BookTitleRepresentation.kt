package seg3502.book_rest_api.representation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel

@JsonInclude(JsonInclude.Include.NON_NULL)
class BookTitleRepresentation : RepresentationModel<BookTitleRepresentation>() {
    var id: Long = 0
    var title: String = ""
}
