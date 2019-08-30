package avila.domingo.domain.model

data class Image(
    val imageFormat: ImageFormat,
    val data: ByteArray,
    val width: Int,
    val height: Int,
    val rotation: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (imageFormat != other.imageFormat) return false
        if (!data.contentEquals(other.data)) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (rotation != other.rotation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageFormat.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + rotation
        return result
    }
}