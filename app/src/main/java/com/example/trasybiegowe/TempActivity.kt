package com.example.trasybiegowe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction

class TempActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        if(savedInstanceState == null){
            val stoper = StoperFragment()
            val ft : FragmentTransaction = getSupportFragmentManager().beginTransaction()
            ft.add(R.id.stoper_container,stoper)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }

    }


}