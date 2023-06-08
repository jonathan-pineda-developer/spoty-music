package cr.ac.una.spoty.entity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.squareup.picasso.Picasso
import cr.ac.una.spoty.R
import cr.ac.una.spoty.album
import cr.ac.una.spoty.artista


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


        //OPCIONES DEL MENU
        val imgMenu = convertView?.findViewById<ImageView>(R.id.imgAction)
        imgMenu?.setOnClickListener {
            val popupMenu = PopupMenu(context, imgMenu)
            popupMenu.inflate(R.menu.main_menu)
            // Agrega clics de escucha para las opciones del menú
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_view_album -> {
                        // Acción para Ver álbum
                        val intent = Intent(context, album::class.java)

                        val appContext = context.applicationContext
                        //e asegura que la actividad se inicie en una nueva tarea independiente en lugar de agregarse a la tarea existente.
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        appContext.startActivity(intent)
                        true
                    }
                    R.id.menu_view_artis -> {
                        // Acción para Ver artista
                        val intent = Intent(context, artista::class.java)

                        val appContext = context.applicationContext
                        //e asegura que la actividad se inicie en una nueva tarea independiente en lugar de agregarse a la tarea existente.
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        appContext.startActivity(intent)

                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }


        return itemView!!
    }
}
