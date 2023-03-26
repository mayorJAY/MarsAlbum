package com.josycom.mayorjay.marsalbum.common.util

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.josycom.mayorjay.marsalbum.R
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Any?.isEmptyOrNull(): Boolean {
    if (this is Collection<*>?) return this.isNullOrEmpty()
    if (this is String?) return this.isNullOrEmpty()
    return this == null
}

fun ImageView.displayImage(imageUrl: String) {
    Glide.with(this.context)
        .load(if (imageUrl.startsWith("https")) imageUrl else imageUrl.replace("http", "https"))
        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
        .error(android.R.drawable.stat_notify_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun TextView.setSpannedText(text: String) {
    val index = text.indexOf(':')
    if (index > 0) {
        val sb = SpannableStringBuilder(text)
        sb.setSpan(StyleSpan(Typeface.BOLD), 0, index, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        this.text = SpannableString(sb)
    }
}

fun String.getFormattedDate(): String {
    val fromFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val toFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
    return try {
        toFormat.format(fromFormat.parse(this) ?: Date())
    } catch (e: ParseException) {
        Timber.e(e)
        this
    }
}

fun Fragment.switchFragment(destination: Fragment, bundle: Bundle?, addToBackStack: Boolean) {
    this.parentFragmentManager.beginTransaction().apply {
        bundle?.let { destination.arguments = it }
        if (addToBackStack) addToBackStack(null)
        replace(R.id.main_fragment, destination)
        commit()
    }
}