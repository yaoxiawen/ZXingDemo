package com.example.zxingdemo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.iv)
        val bitmap = QrCodeUtil.createQRCodeBitmap("你好，初次见面，请多指教！", 480, 480)
        imageView.setImageBitmap(bitmap)

        val imageViewLogo = findViewById<ImageView>(R.id.iv_logo)
        val logo = BitmapFactory.decodeResource(resources, R.drawable.cat)
        val bitmapLogo = QrCodeUtil.createQRCodeBitmap(
            content = "你好，初次见面，请多指教！",
            width = 480,
            height = 480,
            logoBitmap = logo,
            logoPercent = 0.3f
        )
        imageViewLogo.setImageBitmap(bitmapLogo)
    }
}