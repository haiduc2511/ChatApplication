package com.example.chatapplication2.utils

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import android.content.Context
import com.example.chatapplication2.BuildConfig


object CloudinaryHelper {
    val instance = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", BuildConfig.CLOUD_NAME,
            "api_key", BuildConfig.API_KEY,
            "api_secret", BuildConfig.API_SECRET
        )
    )

}

