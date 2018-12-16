package com.droppledev.deschat

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class ChatActivity : AppCompatActivity() {
    private lateinit var mQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val username = intent.getStringExtra("username")
        val db = FirebaseDatabase.getInstance().reference.child("DESChat")
        mQueue = Volley.newRequestQueue(this)
        var key: String? = null
        val chats = mutableListOf<Map<String, String>>()

        tv_key.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                key = tv_key.text.toString()
                for (chat in chats) {
                    if (chat["username"] == username) {
                        tv_chat.append(chat["username"] + " (You) : " + DES.decrypt(key, chat["msg"]) + "\n")

                    } else {
                        tv_chat.append(chat["username"] + " : " + DES.decrypt(key, chat["msg"]) + "\n")

                    }
                }

            }
        })

        jsonParse("https://deschat-78dc9.firebaseapp.com/session")
        if (key.isNullOrEmpty()) {
            Log.d("DESChat", "Key null")
        } else {
            Log.d("DESChat-key", key)
        }


        db.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val chat = dataSnapshot.value as Map<String, String>
                chats.add(chat)
                Log.d("DESChat", chat.toString())
                Log.d("DESChat", "break")
                if (!key.isNullOrEmpty()) {
                    if (chat["username"] == username) {
                        tv_chat.append(chat["username"] + " (You) : " + DES.decrypt(key, chat["msg"]) + "\n")

                    } else {
                        tv_chat.append(chat["username"] + " : " + DES.decrypt(key, chat["msg"]) + "\n")

                    }
                }

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        btn_send.setOnClickListener {
            val msg = et_msg.text?.toString()
            if (!msg.isNullOrEmpty() && !key.isNullOrEmpty()) {
                val encryptMsg = DES.encrypt(key, msg)
                val test = DES.stringToByteArray(encryptMsg)
                Log.d("DESChat", Arrays.toString(test))
                val chat = ChatMessage(username, encryptMsg)
                db.push().setValue(chat)
                et_msg.setText("")
            }

        }

    }

    private fun jsonParse(url: String) {
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val key = response.getString("seskey")
                    tv_key.text = key
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                error.printStackTrace()

            })
        mQueue.add(request)
    }
}
