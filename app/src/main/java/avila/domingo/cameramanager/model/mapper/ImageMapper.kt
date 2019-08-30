package avila.domingo.cameramanager.model.mapper

import android.graphics.*
import avila.domingo.domain.model.Image
import avila.domingo.domain.model.ImageFormat
import avila.domingo.domain.model.mapper.Mapper
import java.io.ByteArrayOutputStream

class ImageMapper : Mapper<Image, Bitmap>() {
    override fun map(model: Image): Bitmap = model.run {
        when (imageFormat) {
            ImageFormat.JPEG -> {
                Bitmap.createBitmap(
                    BitmapFactory.decodeByteArray(this.data, 0, this.data.size),
                    0,
                    0,
                    width,
                    height,
                    Matrix().apply { postRotate(this@run.rotation.toFloat()) },
                    true
                )
            }
            ImageFormat.NV21 -> {
                val yuv = YuvImage(this.data, android.graphics.ImageFormat.NV21, this.width, this.height, null)
                val out = ByteArrayOutputStream()
                yuv.compressToJpeg(Rect(0, 0, width, height), 100, out)
                val bytes = out.toByteArray()
                Bitmap.createBitmap(
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size),
                    0,
                    0,
                    width,
                    height,
                    Matrix().apply { postRotate(this@run.rotation.toFloat()) },
                    true
                )
            }
        }
    }


    override fun inverseMap(model: Bitmap): Image {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}