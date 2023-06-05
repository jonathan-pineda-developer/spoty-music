package cr.ac.una.spoty.entity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import cr.ac.una.spoty.entity.Track
import cr.ac.una.spoty.R

class TrackAdapter(context: Context, tracks: List<Track>) : ArrayAdapter<Track>(context, 0, tracks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_list_view_item, parent, false)
        }

        val track = getItem(position)

        val imgPhoto = itemView?.findViewById<ImageView>(R.id.imgPhoto)
        val txtTrackName = itemView?.findViewById<TextView>(R.id.txtTrackName)
        val txtArtistName = itemView?.findViewById<TextView>(R.id.txtArtistName)

        txtTrackName?.text = track?.name
        txtArtistName?.text = track?.album?.name

        Picasso.get().load(track?.album?.images?.get(0)?.url).into(imgPhoto)

        return itemView!!
    }
}