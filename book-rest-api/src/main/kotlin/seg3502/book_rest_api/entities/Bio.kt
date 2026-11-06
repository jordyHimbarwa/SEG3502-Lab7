package seg3502.book_rest_api.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Bio() {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
    var biodata: String = ""

    constructor(biodata: String) : this() {
        this.biodata = biodata
    }
}
