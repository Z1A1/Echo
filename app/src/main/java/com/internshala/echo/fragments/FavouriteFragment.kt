package com.internshala.echo.fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Build.ID
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.internshala.echo.Database.EchoDatabase
import com.internshala.echo.R
import com.internshala.echo.Songs
import com.internshala.echo.adapters.FavouriteAdapter
import kotlinx.android.synthetic.main.fragment_favourite.*


@Suppress("UNREACHABLE_CODE")
/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {
    var myActivity: Activity? = null
    var getSongsList: ArrayList<Songs>? = null
    var noFavourites: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var songTitle: TextView? = null
    var recyclerView: RecyclerView? = null
    var playpauseButton: ImageButton? = null
    var trackPosition: Int = 0
    var favouriteItemContent: EchoDatabase? = null
    var refreshList: ArrayList<Songs>? = null

    var getListFromDatabase: ArrayList<Songs>? = null

    object Statified {
        var mediaPlayer: MediaPlayer? = null

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_main_screen, container, false)
        activity?.title = "Favourites"
        noFavourites = view?.findViewById(R.id.nofavourities)
        nowPlayingBottomBar = view?.findViewById(R.id.hiddenBarFavScreen)
        songTitle = view?.findViewById(R.id.songtitleFavScreen)
        playpauseButton = view?.findViewById(R.id.playPauseButto)
        noFavourites = view?.findViewById(R.id.nofavourities)
        recyclerView = view?.findViewById(R.id.favouriterecycler)
        return view

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouriteItemContent = EchoDatabase(myActivity)
        getSongsList = gettindsongsfromphone()
        if (getSongsList == null) {
            recyclerView?.visibility = View.INVISIBLE
            noFavourites?.visibility = View.VISIBLE

        } else {
            var favouriteAdapter = FavouriteAdapter(getSongsList as ArrayList<Songs>, myActivity as Context)
            var mLayoutManager = LinearLayoutManager(activity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = favouriteAdapter
            recyclerView?.setHasFixedSize(true)

        }
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

    fun gettindsongsfromphone(): ArrayList<Songs> {
        var arrayList = java.util.ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songurl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songuser = contentResolver?.query(songurl, null, null, null, null)
        if (songuser != null && songuser.moveToFirst()) {
            val songId = songuser.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songuser.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songuser.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songuser.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex = songuser.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songuser.moveToNext()) {
                var currentId = songuser.getLong(songId)
                var currentTitle = songuser.getString(songTitle)
                var currentArtist = songuser.getString(songArtist)
                var currentDaTa = songuser.getString(songData)
                var currentDate = songuser.getLong(dateIndex)
                arrayList.add(Songs(currentId, currentTitle, currentArtist, currentDaTa, currentDate))

            }

        }
        return arrayList
    }

    fun bottomBarSetup() {

        try {
            bottomBarClickHandler()
            songTitle?.setText(SongPlayingFragment.Statified.currentsongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener({
                songTitle?.setText(SongPlayingFragment.Statified.currentsongHelper?.songTitle)
                SongPlayingFragment.Statiscated.onSongComplete()
            })
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                nowPlayingBottomBar?.visibility = View.VISIBLE
            } else {
                nowPlayingBottomBar?.visibility = View.INVISIBLE
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bottomBarClickHandler() {
        nowPlayingBottomBar?.setOnClickListener({
            Statified.mediaPlayer = SongPlayingFragment.Statified.mediaPlayer
            val songPlayingFragment = SongPlayingFragment()
            val args = Bundle()
            args.putString("songArtist", SongPlayingFragment.Statified.currentsongHelper?.songArtist)
            args.putString("path", SongPlayingFragment.Statified.currentsongHelper?.songPath)
            args.putString("songtitle", SongPlayingFragment.Statified.currentsongHelper?.songTitle)
            args.putInt("songId", SongPlayingFragment.Statified.currentsongHelper?.songId?.toInt() as Int)
            args.putInt("songposition", SongPlayingFragment.Statified.currentsongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData", SongPlayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar", "sucess")
            songPlayingFragment.arguments = args
            fragmentManager?.beginTransaction()?.replace(R.id.details_fragment, songPlayingFragment)?.addToBackStack("songPlayingFragment")?.commit()


        })
        playpauseButton?.setOnClickListener({
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackPosition = SongPlayingFragment.Statified.mediaPlayer?.currentPosition as Int
                playpauseButton?.setBackgroundResource(R.drawable.play_icon)

            } else {
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackPosition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                SongPlayingFragment.Statified.currentsongHelper?.isplaying = true

                SongPlayingFragment.Statified.playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)

            }
        })


    }

    fun displayFavouritesBySearching() {
        if (favouriteItemContent?.checkSize() as Int > 0) {
            refreshList = ArrayList<Songs>()
            getSongsList = favouriteItemContent?.quertDBlist()
            var fetchlistFromDevice = gettindsongsfromphone()
            if (fetchlistFromDevice != null) {
                for (i in 0..fetchlistFromDevice?.size - 1) {
                    for (j in 0..getListFromDatabase?.size as Int - 1) {
                        if ((getListFromDatabase?.get(j)?.songID) == fetchlistFromDevice?.get(i)?.songID) {
                            refreshList?.add((getListFromDatabase as ArrayList<Songs>)[j])

                        }
                    }

                }
            } else {

            }


        }

    }


}// Required empty public constructor
