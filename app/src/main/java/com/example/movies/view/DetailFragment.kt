package com.example.movies.view


import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.movies.R
import com.example.movies.databinding.FragmentDetailBinding
import com.example.movies.databinding.SendSmsDialogBinding
import com.example.movies.model.Movie
import com.example.movies.model.MoviePalette
import com.example.movies.model.SmsInfo
import com.example.movies.viewmodel.DetailViewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var movieUuid = 0
    private var movieId = 0
    private var fromFavourite = false

    private lateinit var dataBinding: FragmentDetailBinding
    private var sendSmsStarted = false

    private var currentMovie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movieUuid = DetailFragmentArgs.fromBundle(it).movieUuid
            fromFavourite = DetailFragmentArgs.fromBundle(it).fromFavourite

        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        if(fromFavourite) {
            viewModel.fetchFromFavourite(movieUuid)
        } else {
            viewModel.fetch(movieUuid)
        }


        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.movieLiveData.observe(this, Observer { movie ->
            currentMovie = movie
            movie?.let {
                dataBinding.movie = movie

                it.imagePath?.let {
                    setupBackgroundColor(it)
                }
            }

        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(resources.getString(R.string.IMAGE_BASE_URL) + url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            val myPalette = MoviePalette(intColor)
                            dataBinding.palette = myPalette
                        }
                }

            })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()

            }

            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this movie")
                intent.putExtra(Intent.EXTRA_TEXT, "${currentMovie?.movieName} has a popularity raiting ${currentMovie?.voteAverage}")
                intent.putExtra(Intent.EXTRA_STREAM, getString(R.string.IMAGE_BASE_URL)+currentMovie?.imagePath)
                startActivity(Intent.createChooser(intent, "Share with"))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentMovie?.movieName} has a popularity raiting ${currentMovie?.voteAverage}",
                     currentMovie?.imagePath
                )
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { dialog, which ->
                        if(!dialogBinding.smsDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") {dialog, which ->}
                    .show()

                dialogBinding.smsInfo = smsInfo
            }
        }

    }


    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
    }

}
