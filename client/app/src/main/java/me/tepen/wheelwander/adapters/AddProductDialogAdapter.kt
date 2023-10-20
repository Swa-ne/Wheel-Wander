package me.tepen.wheelwander.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import me.tepen.wheelwander.R


class AddProductDialogAdapter : PagerAdapter {
    private lateinit var context : Context
    private lateinit var ImageUrls : ArrayList<Uri>
//    private lateinit var layoutInflater : LayoutInflater

    constructor(context : Context, imageUrls : ArrayList<Uri>) {
        this.context = context
        ImageUrls = imageUrls
    }

    override fun getCount(): Int {
        return ImageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view : View = LayoutInflater.from(context).inflate(R.layout.product_images_layout, container, false)
        val imageView : ImageView = view.findViewById(R.id.imageUpload)
        imageView.setImageURI(ImageUrls[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        return (`object` as RelativeLayout).removeView(container)
    }

}