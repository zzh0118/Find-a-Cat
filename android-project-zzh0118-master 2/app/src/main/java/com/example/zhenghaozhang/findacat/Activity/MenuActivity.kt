package com.example.zhenghaozhang.findacat


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.zhenghaozhang.findacat.Activity.PetActivity
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*

class MenuActivity:AppCompatActivity(),CatFactFetcher.FactSearchCompletionListener,LocationDetector.LocationListener {
    private val TAG = "MenuActivity"
    private var buttonType:String = ""

    private val LOCATION_PERMISSION_REQUEST_CODE = 7
    private lateinit var catFactFetcher: CatFactFetcher
    private var zipCode: String =  "22202"
    private lateinit var locationDetector: LocationDetector

    companion object {
        val BUTTON_TYPE = "ButtonType"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)

        requestPermissionsIfNecessary()
        catFactFetcher = CatFactFetcher(this,catFact)
        catFactFetcher.factSearchCompletionListener = this

        catFactFetcher.factFetched()

        find_a_cat_button.setOnClickListener {
            Log.d(TAG, "find a cat button tapped")
            loadCatData()
        }

        favorite_cat_button.setOnClickListener {
            Log.d(TAG, "favorite cat button tapped")
            loadFavoriteCatData()
        }


        //find location
        locationDetector = LocationDetector(this)
        locationDetector.locationListener = this
        locationDetector.detectLocation()
    }

    private fun requestPermissionsIfNecessary() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                toast(R.string.permissions_granted)
            }
            else {
                toast(R.string.permissions_denied)
            }
        }
    }



    override fun onfetcher(fact: String){
        catFact.text=fact
    }
    override fun onError() {
    }

    private fun loadCatData() {
        //main thread

        doAsync {
            activityUiThread {
                //load cat data
                val intent = Intent(this@MenuActivity, PetActivity::class.java)
                buttonType = "find_a_cat_button"
                intent.putExtra("ButtonType",buttonType)
                intent.putExtra("ZipCode",zipCode)
                startActivity(intent)
            }
        }


    }

    private fun loadFavoriteCatData() {
        //main thread

        doAsync {
            activityUiThread {
                //load favorite cat data
                val intent = Intent(this@MenuActivity, PetActivity::class.java)
                buttonType = "favorite_cat_button"
                intent.putExtra("ButtonType",buttonType)
                intent.putExtra("ZipCode",zipCode)
                startActivity(intent)
            }
        }


    }

    override fun locationFound(location: Location) {
        val geocode = Geocoder(this, Locale.getDefault())
        val addressList = geocode.getFromLocation(location.latitude, location.longitude, 1)
        zipCode = addressList.get(0).postalCode.toString()


    }

    override fun locationNotFound(reason: LocationDetector.FailureReason) {

        when (reason) {
            LocationDetector.FailureReason.TIMEOUT -> toast(getString(R.string.location_not_found))
            LocationDetector.FailureReason.NO_PERMISSION -> toast(getString(R.string.no_location_permission))
        }
    }
}
