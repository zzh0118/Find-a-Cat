package com.example.zhenghaozhang.findacat


import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import android.widget.Toast.*
import com.example.zhenghaozhang.findacat.petfinder.PetItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import org.jetbrains.anko.toast
import java.io.IOException
import java.util.*

class PersistenceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val TAG = "PersistenceManager"

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveFavorite(petItem: PetItem) {

        val petItems = fetchFavorite().toMutableList()

        if (!petItems.contains(petItem)) {
            petItems.add(petItem)
            Toast.makeText(context,R.string.addFavorite_success_toast,Toast.LENGTH_SHORT).show()
            Log.d(TAG,"add favorite petItem success!")
        }
        else {
            Toast.makeText(context,R.string.addFavorite_failed_toast,Toast.LENGTH_SHORT).show()
        }
        val editor = sharedPreferences.edit()

        //convert a list of scores into a JSON string
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, PetItem::class.java)
        val jsonAdapter = moshi.adapter<List<PetItem>>(listType)
        val jsonString = jsonAdapter.toJson(petItems)


        editor.putString(Constants.FAVORITE_PREF_KEY, jsonString)
        editor.apply()

        println("saveFavorite called! ")

    }

    fun deleteFavorite(petItem: PetItem) {
        val petItems = fetchFavorite().toMutableList()

        if (petItems.contains(petItem)) {
            petItems.remove(petItem)
            Toast.makeText(context,R.string.deleteFavorite_success_toast,Toast.LENGTH_LONG).show()
            Log.d(TAG,"delete favorite petItem success!")
        }
        else{
            Toast.makeText(context,R.string.deleteFavorite_failed_toast,Toast.LENGTH_LONG).show()
            Log.d(TAG,"delete favorite petItem failed!")
        }
        val editor = sharedPreferences.edit()

        //convert a list of scores into a JSON string
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, PetItem::class.java)
        val jsonAdapter = moshi.adapter<List<PetItem>>(listType)
        val jsonString = jsonAdapter.toJson(petItems)


        editor.putString(Constants.FAVORITE_PREF_KEY, jsonString)
        editor.apply()

        println("saveFavorite called! ")
    }

    fun fetchFavorite(): List<PetItem> {

        val jsonString = sharedPreferences.getString(Constants.FAVORITE_PREF_KEY, null)

        //if null, this means no previous pet, so create an empty array list
        if(jsonString == null) {
            return arrayListOf<PetItem>()
        }
        else {
            //existing pet, so convert the scores JSON string into Score objects, using Moshi
            val listType = Types.newParameterizedType(List::class.java, PetItem::class.java)
            val moshi = Moshi.Builder()
                    .build()
            val jsonAdapter = moshi.adapter<List<PetItem>>(listType)

            var petItems:List<PetItem>? = emptyList<PetItem>()
            try {
                petItems = jsonAdapter.fromJson(jsonString)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message)
            }

            if(petItems != null) {
                return petItems
            }
            else {
                return emptyList<PetItem>()
            }
        }
    }



}