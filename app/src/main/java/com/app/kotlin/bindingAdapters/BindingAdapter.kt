package com.app.kotlin.bindingAdapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter

class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun bindImageUrl(view: ImageView, url: String) {
            try {
                view.setImageURI(Uri.parse(url))
            } catch (ignored: Exception) {
            }
        }
    }
}