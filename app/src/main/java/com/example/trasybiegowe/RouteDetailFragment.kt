package com.example.trasybiegowe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import org.w3c.dom.Text
import com.example.trasybiegowe.DB.DBHelper
import java.text.SimpleDateFormat
import java.util.*


class RouteDetailFragment : Fragment() {

    private var routeId : Long = 0
    internal lateinit var db : DBHelper

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState==null){
            val stoper = StoperFragment()
            val ft : FragmentTransaction = getChildFragmentManager().beginTransaction()
            ft.add(R.id.stoper_container,stoper)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }
        else{
            routeId = savedInstanceState.getLong("routeId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val x =  inflater.inflate(R.layout.fragment_route_detail, container, false)


        return x
    }

    override fun onStart(){
        super.onStart()
        var view = getView()

        if(view!=null){

            val title = view.findViewById<TextView>(R.id.textTitle)
            title.setText("TEST TITLE")
            val distance = view.findViewById<TextView>(R.id.tvDistance)

            db = this.getContext()?.let { DBHelper(it) }!!
            var route : RouteInfo = db.getRoute(routeId.toString())
            db.close()

            title.setText(route.title)

            val description = view.findViewById<TextView>(R.id.textDescription)
             description.setText(route.description)
            distance.setText(route.distance)

        }
    }

    fun setRoute(id: Long){
        routeId = id
    }

    public fun getRouteId():Long{
        return routeId
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle)
    {
        savedInstanceState.putLong("routeId",routeId)
    }


}