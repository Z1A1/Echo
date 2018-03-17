package com.internshala.echo.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.internshala.echo.R
import com.internshala.echo.Songs
import com.internshala.echo.adapters.MainScreenAdapter
import kotlinx.android.synthetic.main.fragment_main_screen.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MainScreenFragment : Fragment() {
    var getSongslist:ArrayList<Songs>?=null
    var nowplayingbuttom: RelativeLayout?=null
    var visibleLayout:RelativeLayout?=null
    var playPaseButton: ImageButton?=null
    var songTitle: TextView?=null
    var  nosongs: RelativeLayout?=null
    var recyclerView: RecyclerView?=null
    var myactivity:Activity?=null
    var _mainscreenAdapter:MainScreenAdapter?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragmentiew=ibflater

        val view=inflater!!.inflate(R.layout.fragment_main_screen,container,false)
        activity?.title="All songs"
        setHasOptionsMenu(true)
        visibleLayout=view?.findViewById<RelativeLayout>(R.id.visiblelayout)
        nosongs=view?.findViewById<RelativeLayout>(R.id.nosongs)
        nowplayingbuttom=view?.findViewById<RelativeLayout>(R.id.hiddenBarMainScreen)
        songTitle=view?.findViewById<TextView>(R.id.sondtitleMainScreen)
        playPaseButton=view.findViewById<ImageButton>(R.id.playpauseButton)
        recyclerView=view.findViewById<RecyclerView>(R.id.contentmain)
        return  view


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getSongslist=gettindsongsfromphone()
        val prefs=activity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)
        val actions_sort_ascending=prefs?.getString("action_sort_ascending","true")
        val actions_sort_recent=prefs?.getString("action_recent","false")
        if (getSongslist==null){
            visiblelayout?.visibility=View.INVISIBLE
            nosongs?.visibility=View.VISIBLE

        }else {
            _mainscreenAdapter = MainScreenAdapter(getSongslist as ArrayList<Songs>, myactivity as Context)
            val mlayoutManager = LinearLayoutManager(myactivity)
            recyclerView?.layoutManager = mlayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = _mainscreenAdapter
        }
        if (getSongslist!=null){
            if (actions_sort_ascending!!.equals("true",true)){
                Collections.sort(getSongslist,Songs.Statified.nameComparable)
            _mainscreenAdapter?.notifyDataSetChanged()
            }else if (actions_sort_recent!!.equals("true",true)){
                Collections.sort(getSongslist,Songs.Statified.dateComparator)


            }
        }

        bottom_barSetup()



    }

     fun bottom_barSetup() {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main,menu)
        return


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val switcher=item?.itemId
        if (switcher==R.id.action_sort_ascending){
            val editor=myactivity?.getSharedPreferences("action_ascending",Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_ascending","true")
            editor?.putString("action_sort_recent","false")
            editor?.apply()
            if (getSongslist!=null){
                Collections.sort(getSongslist,Songs.Statified.nameComparable)
            }
            _mainscreenAdapter?.notifyDataSetChanged()
            return false

        }else if(switcher==R.id.action_sort_recent){
            val editorTwo=myactivity?.getSharedPreferences("action_ascending",Context.MODE_PRIVATE)?.edit()
            editorTwo?.putString("action_sort_ascending","true")
            editorTwo?.putString("action_sort_recent","false")
            editorTwo?.apply()
            if (getSongslist!=null){
                Collections.sort(getSongslist,Songs.Statified.dateComparator)
            }
            _mainscreenAdapter?.notifyDataSetChanged()
            return false



        }

        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myactivity=context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myactivity=activity
    }
    fun gettindsongsfromphone():ArrayList<Songs>{
        var arrayList=java.util.ArrayList<Songs>()
        var contentResolver=myactivity?.contentResolver
        var songurl=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songuser=contentResolver?.query(songurl,null,null,null,null)
        if (songuser!=null&&songuser.moveToFirst()){
            val songId=songuser.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle=songuser.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist=songuser.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData=songuser.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex=songuser.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songuser.moveToNext()){
               var currentId= songuser.getLong(songId)
                var currentTitle= songuser.getString(songTitle)
                var currentArtist= songuser.getString(songArtist)
                var currentDaTa= songuser.getString(songData)
                var currentDate= songuser.getLong(dateIndex)
                arrayList.add(Songs(currentId,currentTitle,currentArtist,currentDaTa,currentDate))

            }


        }
        return arrayList

    }

}// Required empty public constructor
