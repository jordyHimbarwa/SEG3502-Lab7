package seg3502.book_rest_api.representation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel

@JsonInclude(JsonInclude.Include.NON_NULL)
class AuthorNameRepresentation : RepresentationModel<AuthorNameRepresentation>() {
    var firstName: String = ""
    var lastName: String = ""
}
