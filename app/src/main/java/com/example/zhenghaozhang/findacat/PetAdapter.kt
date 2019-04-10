package com.example.zhenghaozhang.findacat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.zhenghaozhang.findacat.petfinder.PetItem
import com.squareup.picasso.Picasso


class PetAdapter(private val petItems: List<PetItem>, private val clickListener: OnItemClickListener):
        RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    // creates a new ViewHolder
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup?.context)

        return ViewHolder(layoutInflater.inflate(R.layout.pet_image, viewGroup, false))
    }

    // tell items adapter the number of items to display
    override fun getItemCount(): Int {
        println("petItems.size = " + petItems.size.toString())

        return petItems.size
    }

    //glues each Item from the list with a ViewHolder to populate it using the bind() function.
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val petitem = petItems.get(position)
        //println("petitem.name = " + petitem.name.t + "petitem.get(position) = " + position)
        viewHolder.bind(petitem,clickListener)
    }

    interface OnItemClickListener {
        fun onItemClick(item: PetItem, itemView: View)
    }



    //cache into memory and reuse  to display item inside RecyclerView
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val petnameTextView: TextView = view.findViewById(R.id.PetItem_textview)
        private val petimageImageView: ImageView = view.findViewById(R.id.PetPhoto_imageview)

        // fun bind: binding item with view
        fun bind(petitem: PetItem, listener: OnItemClickListener) = with(itemView){
            petnameTextView.text = petitem.name.t
            Picasso.get().load(petitem.media.photos.photo[0].t).into(petimageImageView)
            setOnClickListener {
                listener.onItemClick(petitem, it)
            }
        }
    }
}


