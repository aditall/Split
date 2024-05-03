package com.example.split1.ui

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ImageUtil {
    fun laodImage(imageUri: Uri?, context: Fragment, imageView: ImageView) {
        Glide.with(context)
            .load(imageUri)
            .into(imageView)

    }
}