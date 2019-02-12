package vn.asiantech.travelmate.models

/*data class City (
    val area:String,
    val description: String,
    val image: String,
    val location: String,
    val name: String,
    val province: String
)*/
class City{
    var area:String ? = null
    var description: String ? = null
    var image: String ? = null
    var location: String ? = null
    var name: String ? = null
    var province: String ? = null

    constructor(){

    }

    constructor(area: String?, description: String?, image: String?, location: String?, name: String?, province: String?) {
        this.area = area
        this.description = description
        this.image = image
        this.location = location
        this.name = name
        this.province = province
    }
}
