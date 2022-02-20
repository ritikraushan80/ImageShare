package com.example.imageshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String?=null
    lateinit var progressBar: ProgressBar
    lateinit var imageView: ImageView
    lateinit var nextBtn: MaterialButton
    lateinit var shareBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextBtn=findViewById(R.id.nextBtn)
        shareBtn=findViewById(R.id.ShareBtn)
        imageView=findViewById(R.id.imageView)
        progressBar=findViewById(R.id.progressBar)

        loadMeme()
    }

    private fun loadMeme() {
        progressBar.visibility=View.VISIBLE

        val url="https://meme-api.herokuapp.com/gimme"


        @Suppress("RedundantSamConstructor")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageUrl=response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?,model: Any?,target: Target<Drawable>?,dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }
                }).into(imageView)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Something Went wrong ....", Toast.LENGTH_SHORT).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    fun nextBtn(view: View) {
        loadMeme()
    }
    fun shareBtn(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey Checkout this cool meme I got from Reddit : $currentImageUrl"
        )
        val chooser=Intent.createChooser(intent, "Share this meme Using...")
        startActivity(chooser)
    }

}