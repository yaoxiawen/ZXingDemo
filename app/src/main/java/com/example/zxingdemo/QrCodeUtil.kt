package com.example.zxingdemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*


object QrCodeUtil {
    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content 字符串内容
     * @param width 位图宽度,要求>=0(单位:px)
     * @param height 位图高度,要求>=0(单位:px)
     * @param character_set 字符集/字符转码格式 (支持格式:{@link CharacterSetECI })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param error_correction 容错级别 (支持级别:{@link ErrorCorrectionLevel })。传null时,zxing源码默认使用 "L"
     * @param margin 空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black 黑色色块的自定义颜色值
     * @param color_white 白色色块的自定义颜色值
     * @param logoBitmap logo小图片
     * @param logoPercent logo小图片在二维码图片中的占比大小,范围[0F,1F],超出范围->默认使用0.2F。
     * @return
     */
    fun createQRCodeBitmap(
        content: String,
        width: Int,
        height: Int,
        character_set: String = "UTF-8",
        error_correction: String = "H",
        margin: String = "1",
        @ColorInt color_black: Int = Color.BLACK,
        @ColorInt color_white: Int = Color.WHITE,
        logoBitmap: Bitmap? = null,
        logoPercent: Float = 0f
    ): Bitmap? {
        /** 1.参数合法性判断  */
        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null
        }
        try {
            /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
            val hints: Hashtable<EncodeHintType, String> = Hashtable()
            if (character_set.isNotEmpty()) {
                hints[EncodeHintType.CHARACTER_SET] = character_set // 字符转码格式设置
            }
            if (error_correction.isNotEmpty()) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction // 容错级别设置
            }
            if (margin.isNotEmpty()) {
                hints[EncodeHintType.MARGIN] = margin // 空白边距设置
            }
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = color_black // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            /** 5.为二维码添加logo小图标 */
            if (logoBitmap != null) {
                return addLogo(bitmap, logoBitmap, logoPercent)
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun addLogo(srcBitmap: Bitmap?, logoBitmap: Bitmap?, logoPercent: Float): Bitmap? {
        /** 1.参数合法性判断  */
        if (srcBitmap == null || logoBitmap == null) {
            return null
        }
        var percent = logoPercent
        if (logoPercent < 0F || logoPercent > 1F) {
            percent = 0.2F
        }
        /** 2. 获取原图片和Logo图片各自的宽、高值 */
        val srcWidth = srcBitmap.width
        val srcHeight = srcBitmap.height
        val logoWidth = logoBitmap.width
        val logoHeight = logoBitmap.height

        /** 3. 计算画布缩放的宽高比 */
        val scaleWidth = srcWidth * percent / logoWidth
        val scaleHeight = srcHeight * percent / logoHeight

        /** 4. 使用Canvas绘制,合成图片 */
        val bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(srcBitmap, 0f, 0f, null)
        canvas.scale(scaleWidth, scaleHeight, (srcWidth / 2).toFloat(), (srcHeight / 2).toFloat())
        canvas.drawBitmap(logoBitmap, srcWidth * 1f / 2 - logoWidth / 2, srcHeight * 1f / 2 - logoHeight / 2, null)
        return bitmap
    }
}