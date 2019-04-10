package com.example.zhenghaozhang.findacat.Activity


import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog

import android.support.v7.widget.GridLayoutManager

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.zhenghaozhang.findacat.R
import com.example.zhenghaozhang.findacat.*
import com.example.zhenghaozhang.findacat.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.zipcode_dialog.view.*


class PetActivity : AppCompatActivity(),PetSearchManager.PetSearchCompletionListener,PetAdapter.OnItemClickListener  {


    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petAdapter: PetAdapter
    private lateinit var buttonType: String
    private lateinit var persistenceManager: PersistenceManager

    private lateinit var zipCode: String
    private val TAG = "PetActivity"


    companion object {
        val ITEM_DATA_KEY = "itemData"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        println("oncreate once")
        super.onCreate(savedInstanceState)

        println("createView once")

        setContentView(R.layout.activity_pet)


        //get button type

        buttonType = intent.getStringExtra("ButtonType")
        //get zipcode
        zipCode = intent.getStringExtra("ZipCode")

        buttonType = intent.getStringExtra(MenuActivity.BUTTON_TYPE)
        //get zipcode


        //search pet
        if (buttonType == "find_a_cat_button") {
            petSearchManager = PetSearchManager(this,zipCode)
            petSearchManager.petSearchCompletionListener = this
            petSearchManager.searchPets()
        }

        //show favorite pet
        else if (buttonType == "favorite_cat_button") {
            persistenceManager = PersistenceManager(this)

            val petItems = persistenceManager.fetchFavorite()

            nearby_cat.text = getString(R.string.myFavoriteCat_title)

            petAdapter = PetAdapter(petItems, this)

            petImage_view.layoutManager = LinearLayoutManager(this)
            petImage_view.adapter = petAdapter

            petImage_view.setLayoutManager(GridLayoutManager(this,2))
        }

        back_button.setOnClickListener{
            finish()
            Log.d(TAG,"back button tapped")
        }
    }


    override fun onItemClick(item: PetItem, itemView: View) {
        val detailsIntent = Intent(this, PetDetailActivity::class.java)
        detailsIntent.putExtra(ITEM_DATA_KEY, item)
        startActivity(detailsIntent)
    }

    //zipcode button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_zipcode,menu)
        return true
    }

    fun zipcodeButtonPressed(item: MenuItem){
        // set dialog view
        val zipcodeDialogView = LayoutInflater.from(this).inflate(R.layout.zipcode_dialog,null)
        //AlertDialogBuilder
        val zipcodeBuilder = AlertDialog.Builder(this)
                .setView(zipcodeDialogView)
                .setTitle(R.string.zipcode_title)
        val mAlertDialog = zipcodeBuilder.show()

        //enter button click of custom layout
        zipcodeDialogView.dialogZipcodeEnterButton.setOnClickListener{
            //get text from EditTexts of custom layout
            if (zipcodeDialogView.dialogZipcodeEnter.text.toString().length == 5) {

                var zipcodeGet = zipcodeDialogView.dialogZipcodeEnter.text.toString()

                //refresh activity
                mAlertDialog.dismiss()
                val intent = Intent(this, PetActivity::class.java)
                buttonType = "find_a_cat_button"
                intent.putExtra("ButtonType",buttonType)
                intent.putExtra("ZipCode",zipcodeGet)
                finish()
                startActivity(intent)


            }
            else {
                Toast.makeText(this,R.string.wrongZipcodeToast,Toast.LENGTH_SHORT).show()
            }
        }

        //cancel button click
        zipcodeDialogView.dialogZipcodeCancelButton.setOnClickListener{
            mAlertDialog.dismiss()
        }


    }


    override fun petsLoaded(petItems: List<PetItem>){

        petAdapter = PetAdapter(petItems,this)

        petImage_view.layoutManager = LinearLayoutManager(this)
        petImage_view.adapter = petAdapter

        petImage_view.setLayoutManager(GridLayoutManager(this,2))

        Log.d(TAG,"Load successful")

    }
    override fun petsNotLoaded(){
        Log.d(TAG,"Load failed")

    }





}
