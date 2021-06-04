package com.example.trasybiegowe

import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import com.example.trasybiegowe.DB.DBHelper

class StoperFragment : Fragment(), View.OnClickListener {
    private var seconds: Int = 0
    private var running: Boolean = false
    private var wasRunning: Boolean = false
    private var routeId: Int =-1
    private var lastDate: String = ""
    private var showTime: Boolean = false

    internal lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState!=null){
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
            showTime = savedInstanceState.getBoolean("wasShowing")
            lastDate = savedInstanceState.getString("startDate").toString()
            routeId = savedInstanceState.getInt("routeId")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_stoper, container, false)
        runStoper(layout)

        val startButton  = layout.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener(this)
        val stopButton  = layout.findViewById<Button>(R.id.stop_button)
        stopButton.setOnClickListener(this)
        val saveButton  = layout.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener(this)
        val textLastResult = layout.findViewById<TextView>(R.id.tvLastResult)
        val layoutLastTime = layout.findViewById<LinearLayout>(R.id.layoutLastTime)

        val layoutTime = layout.findViewById<LinearLayout>(R.id.layoutTime)
        val tvTime = layout.findViewById<TextView>(R.id.tvTime)

        val parentFragment =  getParentFragment() as RouteDetailFragment
        routeId =   parentFragment.getRouteId().toInt()

       // Toast.makeText(this.context, "[StoperFragment] sekundy: "+seconds.toString()+" id "+routeId.toString(), Toast.LENGTH_SHORT).show()

        db = DBHelper(layout.context)

        if(showTime)
        {
            val time = db.getLastResult(routeId,true)

            if(time.length>10){
                tvTime.text = time
                layoutTime.setVisibility(View.VISIBLE)
            }

            textLastResult.text =db.getSecondLastResult(routeId)
            if(textLastResult.text.length>10){
                layoutLastTime.setVisibility(View.VISIBLE)
            }
        }

        else
        {
            textLastResult.text = db.getLastResult(routeId,false)
            if(textLastResult.text.length>10){
                layoutLastTime.setVisibility(View.VISIBLE)
            }
        }
        db.close()


        return layout
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
            if(wasRunning){
                running = true
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
        outState.putBoolean("wasShowing",showTime)
        outState.putString("startDate",lastDate)
        outState.putInt("routeId",routeId)
    }

    private fun onClickStart(view: View){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        if(wasRunning==false)
        {
            lastDate= sdf.format(Date()).toString()
        }
        running = true
        val view2 = getView()
        val stopButton  = view2?.findViewById<Button>(R.id.stop_button)
        if (stopButton != null) {
            stopButton.text =getString(R.string.stop)
        }

    }

    private fun onClickStop(view: View){

        running = false

        val view2 = getView()
        val stopButton  = view2?.findViewById<Button>(R.id.stop_button)

        if (stopButton != null) {
            if(stopButton.text == getString(R.string.stop)){
                stopButton.text =getString(R.string.reset)
            }
            else{
                stopButton.text =getString(R.string.stop)
                seconds=0
                wasRunning=false
            }
        }
    }


    private fun onClickSave(view: View) {
        running = false
        if(seconds>0){
            db = DBHelper(view.context)
            val view2 = getView()
            val textLastResult = view2?.findViewById<TextView>(R.id.tvLastResult)
            val layoutLastTime = view2?.findViewById<LinearLayout>(R.id.layoutLastTime)
            if (textLastResult != null) {
                textLastResult.text = db.getLastResult(routeId,false)
                if(textLastResult.text.length>9){
                    if (layoutLastTime != null) {
                        layoutLastTime.setVisibility(View.VISIBLE)
                    }
                }
            }


            val hours = seconds / 3600
            val minutes = seconds % 3600 / 60
            val secs = seconds % 60
            val time = String.format("%d:%02d:%02d", hours, minutes, secs)
            seconds = 0

            db.addResultToDB(routeId,time,lastDate)
            db.close()

            showTime = true

            val layoutTime = view2?.findViewById<LinearLayout>(R.id.layoutTime)
            val tvTime = view2?.findViewById<TextView>(R.id.tvTime)

            if (tvTime != null) {
                tvTime.text = time+"  start: "+lastDate
            }
            if (layoutTime != null) {

                layoutTime.setVisibility(View.VISIBLE)
            }
        }

    }



    private fun runStoper(view: View){
        val timeView : TextView = view.findViewById(R.id.time_view) as TextView
        val handler : Handler = Handler()

        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, secs)
                timeView.text = time
                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when(v.getId()){
                R.id.start_button -> onClickStart(v)
                R.id.stop_button -> onClickStop(v)
                R.id.save_button -> onClickSave(v)
            }
        }

    }


}