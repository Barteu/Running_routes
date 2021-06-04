package com.example.trasybiegowe.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.trasybiegowe.MainActivity

import com.example.trasybiegowe.RouteInfo

//import com.example.gralos.Modal.Message

class DBHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,
    null, DATABASE_VER) {
    companion object {
        private val DATABASE_VER = 17
        private val DATABASE_NAME = "EDMTDB.db"
        //Table routes
        private val TABLE_NAME = "Routes"
        private val COL_ID= "id"
        private val COL_TITLE= "title"
        private val COL_DISTANCE = "distance"
        private val COL_DESCRIPTION = "description"

        private val TABLE_NAME_2 = "Results"
        private val COL_ROUTE_ID_2= "id"
        private val COL_TIME_2= "time"
        private val COL_DATE_2= "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_ID NUMBER, $COL_TITLE TEXT, $COL_DISTANCE TEXT,$COL_DESCRIPTION TEXT)")
        val CREATE_TABLE_QUERY_2 = ("CREATE TABLE $TABLE_NAME_2 ($COL_ROUTE_ID_2 NUMBER, $COL_TIME_2 TEXT, $COL_DATE_2 DATE)")
        db!!.execSQL(CREATE_TABLE_QUERY)
        db!!.execSQL(CREATE_TABLE_QUERY_2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_2")
        onCreate(db!!)
    }

    //CRUD
    val allRoutes:MutableList<RouteInfo>
        get(){
            val listRoute = ArrayList<RouteInfo>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor =  db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do {
                    val route = RouteInfo()
                    route.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    route.title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
                    route.distance = cursor.getString(cursor.getColumnIndex(COL_DISTANCE))
                    route.description =  cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION))
                    listRoute.add(route)

                } while (cursor.moveToNext())
            }
            db.close()
            return listRoute
        }

    fun addRouteToDB(id: Int, title: String, distance:String,description : String){
        val db= this.writableDatabase
        val values = ContentValues()
        if(testOne(id.toString()))
        {
            values.put(COL_ID, id)
            values.put(COL_TITLE, title)
            values.put(COL_DISTANCE, distance)
            values.put(COL_DESCRIPTION, description)
            db.insert(TABLE_NAME, null, values)
        }
        db.close()
    }

    fun testOne(key:String): Boolean {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = '$key';"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            return false
        }
        return true
    }

    public fun getRoute(id : String): RouteInfo{
        var route :RouteInfo = RouteInfo()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_ID='$id';"
        val db = this.writableDatabase
        val cursor =  db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()){
            route.title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
            route.distance = cursor.getString(cursor.getColumnIndex(COL_DISTANCE))
            route.description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION))
        }
        db.close()
       return route
    }


//    fun addResultToDB(id: Int, time: String, date:String){
//        val db= this.writableDatabase
//        val values = ContentValues()
//        if(testOne(id.toString()))
//        {
//            values.put(COL_ROUTE_ID_2, id)
//            values.put(COL_TIME_2,time )
//            values.put(COL_DATE_2, date)
//            db.insert(TABLE_NAME_2, null, values)
//        }
//        db.close()
//    }

    fun addResultToDB(id: Int, time: String, date:String){

        val db= this.writableDatabase
        db!!.execSQL("INSERT INTO $TABLE_NAME_2($COL_ROUTE_ID_2, $COL_TIME_2,$COL_DATE_2) VALUES($id,'$time','$date');")
        db.close()

        val values = ContentValues()
        if(testOne(id.toString()))
        {
            values.put(COL_ROUTE_ID_2, id)
            values.put(COL_TIME_2,time )
            values.put(COL_DATE_2, date)
            db.insert(TABLE_NAME_2, null, values)
        }
    }

    fun getLastResult(id: Int,new :Boolean): String{
            //AND $COL_DATE_2>=ALL(SELECT $COL_DATE_2 FROM $TABLE_NAME_2 WHERE $COL_ROUTE_ID_2=$id)

        val selectQuery = "SELECT * FROM $TABLE_NAME_2 WHERE $COL_ROUTE_ID_2=$id ORDER BY $COL_DATE_2 DESC LIMIT 1;"
        val db = this.writableDatabase
        val cursor =  db.rawQuery(selectQuery, null)
        var time : String = ""
        var date : String = ""
        if(cursor.moveToFirst()){
            time = cursor.getString(cursor.getColumnIndex(COL_TIME_2))
            date  = cursor.getString(cursor.getColumnIndex(COL_DATE_2))
        }
        db.close()
        if(new){
            return time+"  start: "+date
        }
        return time+"  w dniu "+date
    }

    fun getSecondLastResult(id: Int): String{
        //AND $COL_DATE_2>=ALL(SELECT $COL_DATE_2 FROM $TABLE_NAME_2 WHERE $COL_ROUTE_ID_2=$id)

        val selectQuery = "SELECT * FROM $TABLE_NAME_2 WHERE $COL_ROUTE_ID_2=$id ORDER BY $COL_DATE_2 DESC LIMIT 2;"
        val db = this.writableDatabase
        val cursor =  db.rawQuery(selectQuery, null)
        var time : String = ""
        var date : String = ""
        if(cursor.moveToFirst()){
            do {
                time = cursor.getString(cursor.getColumnIndex(COL_TIME_2))
                date  = cursor.getString(cursor.getColumnIndex(COL_DATE_2))
            }while (cursor.moveToNext())

        }
        db.close()
        return time+"  w dniu "+date
    }



}
