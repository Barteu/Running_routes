package com.example.trasybiegowe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.trasybiegowe.DB.DBHelper
import org.json.JSONArray
import java.lang.Exception
import java.net.URL


class MainActivity : AppCompatActivity() , RouteListFragment.Listener {  //AppCompatActivity

    internal lateinit var db : DBHelper
    var clickedId :Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if(savedInstanceState!=null) {
            clickedId = savedInstanceState.getLong("clickedId")
        }

        var downloaded = -1

        val thread = Thread() {
            run {
                // DB

                val url = " http://192.168.1.6:3000/routes"
                var body =""// URL(url).readText()
                        try{
                           body = URL(url).readText()
                        }
                        catch (e:Exception)
                        {
                            downloaded=0
                        }
                    db = DBHelper(this)
                    if (body != "") {
                        val routes = JSONArray(body)
                        for (i in 0 until routes.length()) {
                            downloaded = 1
                            var route = routes.getJSONObject(i)
                            db.addRouteToDB(route.getInt("id"), route.getString("title"), route.getString("distance"), route.getString("description"))
                        }
                    }
                    else{
                        downloaded=0
                    }
                    db.close()
                }
            runOnUiThread() {
                setContentView(R.layout.activity_main)
                if (downloaded < 1)
                {
                    Toast.makeText(this, "Błąd w pobieraniu danych, sprawdź połączenie z internetem", Toast.LENGTH_LONG).show()
                }

            }
        }
        thread.start()
    }

     override fun itemClicked(id: Long) {

         var fragmentContainer = findViewById<View>(R.id.fragment_container)

         if (fragmentContainer != null) {
             clickedId = id
             val details = RouteDetailFragment()
             val ft = supportFragmentManager.beginTransaction()
             details.setRoute(id)
             ft.replace(R.id.fragment_container, details)
             ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
             ft.addToBackStack(null)
             ft.commit()

         } else {
             var intent = Intent(this, DetailActivity::class.java)
             intent.putExtra(DetailActivity.EXTRA_ROUTE_ID, id.toString())  //DetailActivity.EXTRA_ROUTE_ID
             startActivity(intent)
         }
     }


    override fun onSaveInstanceState(savedInstanceState: Bundle)
    {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putLong("clickedId",clickedId)
    }

}

