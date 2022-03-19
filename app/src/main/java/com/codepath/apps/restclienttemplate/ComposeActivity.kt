package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.TimelineActivity.Companion.TAG
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.apps.restclienttemplate.models.User.Companion.fromJson
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btntweet)

        client = TwitterApplication.getRestClient(this)

        // handles the user's click on the tweet button
        btnTweet.setOnClickListener {

            // Grab the content of edit text(etCompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet is not empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
            } else
            // 2. Make sure the tweet under the character count
                if (tweetContent.length > 280) {
                    Toast.makeText(
                        this,
                        "Tweets is too long! Limit is 280 charcters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                          Log.i(TAG,"Successfully published tweet!")

                          val tweet = Tweet.fromJson(json.jsonObject)

                          val intent = Intent()
                          intent.putExtra("tweet",tweet)
                          setResult(RESULT_OK,intent)
                          finish()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                           Log.e(TAG,"Failed to publish tweet",throwable)
                        }
                    })
                }
        }

        }
    companion object {
        val TAG = "TimelineActivity"
    }
}