package ro.valentin.cinemania.presentation.movies

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import ro.valentin.cinemania.core.Constants.IMG_BASE_URL

@BindingAdapter("android:posterPath")
fun setPosterPathToImgView(posterImageView: ImageView, posterPath: String?) {
    posterPath?.let {
        Glide.with(posterImageView.context).load("$IMG_BASE_URL$posterPath").into(posterImageView)
    }

}