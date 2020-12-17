package studio.bz_soft.githubusers.root

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Parcelable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.loadAny
import coil.transform.CircleCropTransformation
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.root.delegated.DelegateAdapter

fun Context.setAnimationTextLeft(): Animation =
    AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)

fun showImage(imageView: ImageView, image: String) {
    loadImage(imageView, image)
}

fun showImage(imageView: ImageView, @DrawableRes image: Int, size: Int = 64) {
    loadImage(imageView, image, size)
}

fun showImage(imageView: ImageView, image: Uri) {
    loadImage(imageView, image)
}

fun showImage(imageView: ImageView, image: Bitmap) {
    loadImage(imageView, image)
}

fun showImage(imageView: ImageView, image: Drawable) {
    loadImage(imageView, image)
}

fun showImage(imageView: ImageView, comparable: Comparable<*>) {
    loadImage(imageView, comparable)
}

private fun loadImage(imageView: ImageView, image: Any, size: Int = 64) {
    imageView.loadAny(image) {
        size(size)
        placeholder(R.drawable.ic_no_user_light)
        fallback(R.drawable.ic_no_user_light)
        transformations(CircleCropTransformation())
    }
}

fun ProgressBar.showProgressBar(progress: Int) {
    this.visibility = if (progress > 0) View.VISIBLE else View.GONE
}

fun <T> RecyclerView.setRecyclerView(v: View, delegateAdapter: DelegateAdapter<T>) {
    val recyclerViewState: Parcelable? = null
    var position = 0
    this.apply {
        adapter = delegateAdapter
        startAnimation(context.setAnimationTextLeft())
        layoutManager = LinearLayoutManager(v.context, RecyclerView.VERTICAL, false)
        recyclerViewState?.apply {
            layoutManager?.onRestoreInstanceState(recyclerViewState)
            scrollToPosition(this@setRecyclerView, position)
        }
    }
}