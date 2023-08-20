package com.example.zxingdemo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Toast.makeText(this@MainActivity, "Cancelled due to missing camera permission", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(this@MainActivity, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        }
    }
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

        findViewById<Button>(R.id.bt).setOnClickListener {
            barcodeLauncher.launch(ScanOptions())
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            val options = ScanOptions().setOrientationLocked(false).setCaptureActivity(
                CustomScannerActivity::class.java
            )
            barcodeLauncher.launch(options)
        }
    }
}