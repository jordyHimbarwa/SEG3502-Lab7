package seg3502.book_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import seg3502.book_rest_api.controller.ApiController
import seg3502.book_rest_api.entities.Bio
import seg3502.book_rest_api.representation.BioRepresentation

@Component
class BioModelAssembler :
        RepresentationModelAssemblerSupport<Bio, BioRepresentation>(
                ApiController::class.java,
                BioRepresentation::class.java
        ) {

    override fun toModel(entity: Bio): BioRepresentation {
        val bioRepresentation = instantiateModel(entity)
        bioRepresentation.id = entity.id
        bioRepresentation.biodata = entity.biodata
        bioRepresentation.add(
                linkTo(methodOn(ApiController::class.java).getBioById(entity.id)).withSelfRel()
        )
        return bioRepresentation
    }

    override fun toCollectionModel(entities: Iterable<Bio>): CollectionModel<BioRepresentation> {
        val bioRepresentations = super.toCollectionModel(entities)
        bioRepresentations.add(linkTo(methodOn(ApiController::class.java).allBios()).withSelfRel())
        return bioRepresentations
    }
}
