package com.example.trasybiegowe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.trasybiegowe.DB.DBHelper


class RouteListFragment : ListFragment() {

    internal lateinit var db : DBHelper

        interface Listener {
            fun itemClicked(id: Long)
        }


    private lateinit var listener : Listener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var listRoutes : MutableList<RouteInfo>
        var names : MutableList<Any> = ArrayList()

        db = this.getContext()?.let { DBHelper(it) }!!
        listRoutes = db.allRoutes

        if(listRoutes.size>0){
            listRoutes.forEach {
                names.add(it.title+" - "+it.distance)
            }
        }

        // DO TESTOW
//        for (i in 1..8){
//            names.add("element "+i.toString())
//        }

        db.close()

       val adapter = ArrayAdapter<Any>(inflater.getContext(), R.layout.list_frag_elem, names) //android.R.layout.simple_list_item_1
        setListAdapter(adapter)
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onAttach(context : Context){
        super.onAttach(context)
        this.listener = context as Listener
    }

     override fun onListItemClick(listView:ListView,itemView:View,position:Int, id:Long)
    {
     if(listener!=null){
         listener.itemClicked(id)
     }
    }


}

