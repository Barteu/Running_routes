package com.example.trasybiegowe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


class DetailActivity : AppCompatActivity() {
    companion object
    {
        public final val EXTRA_ROUTE_ID : String = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.hide()
        val frag: RouteDetailFragment = supportFragmentManager.findFragmentById(R.id.detail_frag) as RouteDetailFragment
        var routeId = intent.getStringExtra(EXTRA_ROUTE_ID)?.toLong() // getIntent().getExtras().get(EXTRA_ROUTE_ID) as Int
        if(routeId==null){
            routeId=0
        }
        frag.setRoute(routeId)


    }
}



