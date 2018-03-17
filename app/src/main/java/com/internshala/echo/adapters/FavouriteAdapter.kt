package com.internshala.echo.adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.internshala.echo.R
import com.internshala.echo.Songs
import com.internshala.echo.fragments.MainScreenFragment

/**
 * Created by admin on 3/3/2018.
 */
class FavouriteAdapter(_songDetails: ArrayList<Songs>, _context: Context) : RecyclerView.Adapter<FavouriteAdapter.MyviewHOLDER>() {
    var songDetails: ArrayList<Songs>? = null
    var mcontext: Context? = null

    init {
        this.songDetails = _songDetails
        this.mcontext = _context
    }

    override fun onBindViewHolder(holder: MyviewHOLDER, position: Int) {
        val songobject = songDetails?.get(position)
        holder.trackTitle?.text = songobject?.songTitle
        holder.trackArtist?.text = songobject?.artist
        holder.contentHolder?.setOnClickListener({
            val songPlayingFragment = MainScreenFragment()
            val args = Bundle()
            args.putString("songArtist", songobject?.artist)
            args.putString("path", songobject?.songData)
            args.putString("songtitle", songobject?.songTitle)
            args.putInt("songId", songobject?.songID?.toInt() as Int)
            args.putInt("songposition", position)
            args.putParcelableArrayList("songData", songDetails)


            (mcontext as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment, songPlayingFragment)
                    .addToBackStack("SongPlayingFragmentFavourit ")

                    .commit()
        })


    }

    override fun getItemCount(): Int {
        if (songDetails == null) {
            return 0
        } else {
            return (songDetails as ArrayList<Songs>).size
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyviewHOLDER {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.row_custom_mainscreen_adaptor, parent, false)
        return MyviewHOLDER(itemView)


    }

    class MyviewHOLDER(view: View) : RecyclerView.ViewHolder(view) {
        var trackTitle: TextView? = null
        var trackArtist: TextView? = null
        var contentHolder: RelativeLayout? = null

        init {
            trackTitle = view.findViewById<TextView>(R.id.tracktitle)
            trackArtist = view.findViewById<TextView>(R.id.trackArtist)
            contentHolder = view.findViewById<RelativeLayout>(R.id.contentRow)
        }


    }
}