package seg3502.book_rest_api.repository

import org.springframework.data.repository.CrudRepository
import seg3502.book_rest_api.entities.Bio

interface BioRepository : CrudRepository<Bio, Long>
