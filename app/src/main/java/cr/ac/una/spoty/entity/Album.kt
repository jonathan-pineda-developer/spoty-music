package cr.ac.una.spoty.entity



data class Album (val name:String, val images: List<Image> ){
}
data class Image(
    val url: String,
    val width: Int,
    val height: Int
)