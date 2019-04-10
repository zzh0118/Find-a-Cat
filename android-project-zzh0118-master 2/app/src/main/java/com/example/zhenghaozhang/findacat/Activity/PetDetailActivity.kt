package com.example.zhenghaozhang.findacat.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.zhenghaozhang.findacat.PersistenceManager
import com.example.zhenghaozhang.findacat.R
import com.example.zhenghaozhang.findacat.petfinder.PetItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.activity_pet_detail.*
import org.jetbrains.anko.email
import org.jetbrains.anko.toast

class PetDetailActivity : AppCompatActivity() {
    private lateinit var petItem: PetItem
    private lateinit var persistenceManager: PersistenceManager
    private val TAG = "PetDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)
        persistenceManager = PersistenceManager(this)


        //set details
        petItem = intent.getParcelableExtra(PetActivity.ITEM_DATA_KEY)

        println("pet name is : " + petItem.name.t)
        Log.d(TAG,"Passed data from PetActivity")

        setItemsDetails()


        //set favorite button
        addFavorite_button.setOnClickListener{
            persistenceManager.saveFavorite(petItem)
            Log.d(TAG,"addFavorite button tapped")

        }

        unfavorite_button.setOnClickListener{
            persistenceManager.deleteFavorite(petItem)
            Log.d(TAG,"delete button tapped")
        }



        petdetailBack_button.setOnClickListener() {
            Log.d(TAG, "back button tapped")
            finish()
        }
    }



    private fun setItemsDetails() {
        val PetName = getString(R.string.cat_name_string,petItem.name.t)
        pet_name_textview.text = PetName
        val PetBreed = getString(R.string.cat_breed_string,petItem.breeds.breed[0].t)
        pet_breed_textview.text = PetBreed
        val PetGender = getString(R.string.cat_gender_string,petItem.sex.t)
        pet_gender_textview.text = PetGender
        val PetZipcode = getString(R.string.cat_zipcode_string,petItem.contact.zip.t)
        pet_zipcode_textview.text = PetZipcode
        val PetDescription = getString(R.string.cat_description_string,petItem.description?.t.toString())
        pet_description_textview.text = PetDescription


        Picasso.get().load(petItem.media.photos.photo[0].t).into(pet_imageview)
    }

    //share button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_share,menu)
        menuInflater.inflate(R.menu.menu_email,menu)
        return true
    }


    fun emailButtonPressed(item:MenuItem){
        val to = petItem.contact.email.t
        val subject = "Pet Adoption"
        val message = getString(R.string.email_message,petItem.name.t)

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, to)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.setType("message/rfc822")
        startActivity(Intent.createChooser(intent, resources.getText(R.string.email_title)))

        petdetailBack_button.setOnClickListener{
            Log.d(TAG,"back button tapped")
            finish()
        }
    }

    fun shareButtonPressed(item: MenuItem) {
        val sendIntent = Intent()

        sendIntent.action = Intent.ACTION_SEND


        val sharedPetItem = petItem
        val shareText = getString(R.string.share_message,sharedPetItem.name.t,sharedPetItem.contact.email.t,sharedPetItem.media.photos.photo[0].t)

        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"

        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_title)))
    }


}
