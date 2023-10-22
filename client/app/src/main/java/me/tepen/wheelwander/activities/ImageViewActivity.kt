package me.tepen.wheelwander.activities


import android.content.Intent.getIntent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import me.tepen.wheelwander.R


class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        val imageView: ImageView = findViewById(R.id.imageView)
        Glide.with(this@ImageViewActivity).load(intent.getStringExtra("image")).into(imageView)
    }
}