package com.internshala.echo.fragments


import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.*
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.cleveroad.audiovisualization.GLAudioVisualizationView
import com.internshala.echo.CurrentsongHelper
import com.internshala.echo.Database.EchoDatabase
import com.internshala.echo.R
import com.internshala.echo.Songs
import com.internshala.echo.fragments.SongPlayingFragment.Statified.currentsongHelper
import com.internshala.echo.fragments.SongPlayingFragment.Statified.mediaPlayer
import com.internshala.echo.fragments.SongPlayingFragment.Statified.seekBar
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class SongPlayingFragment : Fragment() {
    object Statified {

        var myActivity: Activity? = null
        var mediaPlayer: MediaPlayer? = null
        var startimetext: TextView? = null
        var shuffleButton: ImageButton? = null
        var nextButton: ImageButton? = null
        var previousButton: ImageButton? = null
        var loopButton: ImageButton? = null
        var seekBar: SeekBar? = null
        var songTitle: TextView? = null
        var songArtist: TextView? = null
        var endtimetext: TextView? = null
        var playpauseImageButton: ImageButton? = null
        var currentsongHelper: CurrentsongHelper? = null
        var currentPosition: Int = 0
        var fetchSongs: ArrayList<Songs>? = null
        var glView: GLAudioVisualizationView? = null
        var audioVisualization: AudioVisualization? = null
        var fab: ImageView? = null
        var favouritecontent: EchoDatabase? = null
        var mSensor: SensorManager? = null
        var mSensorListner: SensorEventListener? = null
        var updateSongTimw = object : Runnable {
            override fun run() {


                val getCurrent = mediaPlayer?.currentPosition
                startimetext?.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong() as Long),
                        TimeUnit.MILLISECONDS.toSeconds(getCurrent?.toLong() as Long) -
                                TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong() as Long))))
                seekBar?.setProgress(getCurrent?.toInt() as Int)
                Handler().postDelayed(this, 1000)
            }


        }
        var MY_PREFS_NAME = "shakeFeature"

    }

    object Statiscated {
        var MYPREFSSHUFFLE = "shuffle feature"
        var MY_PREFSLOOP = "my loop "
        fun onSongComplete() {
            if (currentsongHelper?.isShuffle as Boolean) {
                playNext("playNextlikeNormal")
                currentsongHelper?.isplaying = true

            } else {
                if (currentsongHelper?.isLoop as Boolean) {
                    currentsongHelper?.isplaying = false

                    var nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
                    currentsongHelper?.songPath = nextSong?.songData
                    currentsongHelper?.songTitle = nextSong?.songTitle
                    currentsongHelper?.currentPosition = Statified.currentPosition
                    currentsongHelper?.songId = (nextSong?.songID as Long)
                    updateTextView(currentsongHelper?.songTitle as String, currentsongHelper?.songArtist as String)

                    Statified.mediaPlayer?.reset()
                    try {
                        Statified.mediaPlayer?.setDataSource(Statified.myActivity, Uri.parse(Statified.currentsongHelper?.songPath))
                        Statified.mediaPlayer?.prepare()
                        Statified.mediaPlayer?.start()
                        processInformation(mediaPlayer as MediaPlayer)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    playNext("playNextNormal")
                    currentsongHelper?.isplaying = true

                }
            }
            if (Statified.favouritecontent?.checkifIdExists(currentsongHelper?.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_on))
            } else {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_off))
            }


        }

        fun updateTextView(songtitle: String, songartist: String) {
            var songtitleupdated = songtitle
            var songartistupdated = songartist
            if (songtitle.equals("<unknown>", true)) {
                songtitleupdated = "unknown"
            }
            if (songtitle.equals("<unknown>", true)) {
                songartistupdated = "unknown"
            }

            Statified.songTitle?.setText(songtitleupdated)
            Statified.songArtist?.setText(songartistupdated)


        }

        fun processInformation(mediaPlayer: MediaPlayer) {
            val finalTime = mediaPlayer.duration
            val startTime = mediaPlayer.currentPosition
            seekBar?.max = finalTime
            Statified.startimetext?.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(startTime?.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(startTime?.toLong() as Long) -
                            TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong() as Long)))
            )
            Statified.endtimetext?.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(finalTime?.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime?.toLong() as Long) -
                            TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong() as Long))))
            seekBar?.setProgress(startTime)
            Handler().postDelayed(Statified.updateSongTimw, 1000)

        }

        fun playNext(check: String) {
            if (check.equals("playNextNormal", true)) {
                Statified.currentPosition = Statified.currentPosition + 1
            } else if (check.equals("playNextlikeNormalshuffle", true)) {
                var randomObject = Random()
                var randomPosition = randomObject.nextInt(Statified.fetchSongs?.size?.plus(1) as Int)
                Statified.currentPosition = randomPosition


            }
            if (Statified.currentPosition == Statified.fetchSongs?.size) {
                Statified.currentPosition = 0
            }
            currentsongHelper?.isLoop = false
            var nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
            currentsongHelper?.songPath = nextSong?.songData
            currentsongHelper?.songTitle = nextSong?.songTitle
            currentsongHelper?.currentPosition = Statified.currentPosition
            currentsongHelper?.songId = (nextSong?.songID as Long)
            updateTextView(currentsongHelper?.songTitle as String, currentsongHelper?.songArtist as String)
            Statified.mediaPlayer?.reset()
            try {
                Statified.mediaPlayer?.setDataSource(Statified.myActivity, Uri.parse(currentsongHelper?.songPath))
                Statified.mediaPlayer?.prepare()
                Statified.mediaPlayer?.start()
                processInformation(Statified.mediaPlayer as MediaPlayer)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (Statified.favouritecontent?.checkifIdExists(currentsongHelper?.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_on))
            } else {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_off))
            }


        }
    }

    var mAccelaraation: Float = 0f
    var mAccelartionCurrent: Float = 0f
    var mAccelaraationLast: Float = 0f


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_song_playing, container, false)
        activity?.title = "Now Playing"
        setHasOptionsMenu(true)
        Statified.seekBar = view?.findViewById(R.id.seekBar)
        Statified.startimetext = view?.findViewById(R.id.startime)
        Statified.nextButton = view?.findViewById(R.id.nextButton)
        Statified.previousButton = view?.findViewById(R.id.previousButton)
        Statified.loopButton = view?.findViewById(R.id.loopButton)
        Statified.songTitle = view?.findViewById(R.id.songTitele)
        Statified.songArtist = view?.findViewById(R.id.songartist)
        Statified.endtimetext = view?.findViewById(R.id.endtime)
        Statified.playpauseImageButton = view?.findViewById(R.id.playpauseButton)
        Statified.glView = view?.findViewById(R.id.visualizer_view)
        Statified.fab = view?.findViewById(R.id.favouriteicon)
        Statified.fab?.alpha = 0.8f



        return view

    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // you can extract AudioVisualization interface for simplifying things
        Statified.audioVisualization = Statified.glView as AudioVisualization
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Statified.myActivity = context as Activity

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        Statified.myActivity = activity

    }

    override fun onResume() {
        super.onResume()
        Statified.audioVisualization?.onResume()
        Statified.mSensor?.registerListener(Statified.mSensorListner, Statified.mSensor?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        Statified.audioVisualization?.onPause()
        super.onPause()
        Statified.mSensor?.unregisterListener(Statified.mSensorListner)
    }

    override fun onDestroyView() {
        Statified.audioVisualization?.release()
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Statified.mSensor = Statified.myActivity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelaraation = 0.0f
        mAccelartionCurrent = SensorManager.GRAVITY_EARTH
        mAccelaraationLast = SensorManager.GRAVITY_EARTH
        bindShaleListner()


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.song_playing_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem? = menu?.findItem(R.id.redirect)
        item?.isVisible = true
        val item2: MenuItem? = menu?.findItem(R.id.action_sort)
        item2?.isVisible = false


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.redirect -> {
                Statified.myActivity?.onBackPressed()
                return false

            }

        }
        return false

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        Statified.favouritecontent = EchoDatabase(Statified.myActivity)

        Statified.currentsongHelper = CurrentsongHelper()
        Statified.currentsongHelper?.isplaying = true
        Statified.currentsongHelper?.isLoop = false
        Statified.currentsongHelper?.isShuffle = false

        var path: String? = null
        var songTitle: String? = null
        var songArtist: String? = null
        var songId: Long = 0
        try {
            path = arguments?.getString("path")
            songTitle = arguments?.getString("songTitle")
            songArtist = arguments?.getString("songArtist")
            songId = arguments?.getInt(" songId")!!.toLong()
            Statified.currentPosition = arguments?.getInt("songPosition")!!

            Statified.fetchSongs = arguments?.getParcelableArrayList("songData")
            Statified.currentsongHelper?.songPath = path
            Statified.currentsongHelper?.songTitle = songTitle
            Statified.currentsongHelper?.songArtist = songArtist
            Statified.currentsongHelper?.songId = songId
            Statified.currentsongHelper?.currentPosition = Statified.currentPosition
            Statiscated.updateTextView(Statified.currentsongHelper?.songTitle as String, Statified.currentsongHelper?.songArtist as String)

        } catch (e: Exception) {
            e.printStackTrace()

        }
        var fromfavBottomBar = arguments?.get("FavBottomBar") as? String
        if (fromfavBottomBar != null) {
            Statified.mediaPlayer = FavouriteFragment.Statified.mediaPlayer
        } else {
            Statified.mediaPlayer = MediaPlayer()
            Statified.mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                Statified.mediaPlayer?.setDataSource(Statified.myActivity, Uri.parse(path))
                Statified.mediaPlayer?.prepare()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            Statified.mediaPlayer?.start()
        }
        Statiscated.processInformation(Statified.mediaPlayer as MediaPlayer)
        if (Statified.mediaPlayer?.isPlaying as Boolean) {
            Statified.playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)


        } else {
            Statified.playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)

        }
        Statified.mediaPlayer?.setOnCompletionListener {
            Statiscated.onSongComplete()

        }
        clickhandler()
        var visulizationhandler = DbmHandler.Factory.newVisualizerHandler(Statified.myActivity as Context, 0)
        Statified.audioVisualization?.linkTo(visulizationhandler)
        var prefShuffle = Statified.myActivity?.getSharedPreferences(Statiscated.MYPREFSSHUFFLE, Context.MODE_PRIVATE)
        var isShuffleAllowed = prefShuffle?.getBoolean("feature", false)
        if (isShuffleAllowed as Boolean) {
            Statified.currentsongHelper?.isShuffle = true
            Statified.currentsongHelper?.isLoop = false
            Statified.shuffleButton?.setBackgroundColor(R.drawable.shuffle_icon)
            Statified.loopButton?.setBackgroundColor(R.drawable.loop_white_icon)

        } else {
            Statified.currentsongHelper?.isShuffle = false
            Statified.shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)

        }
        var prefLoop = Statified.myActivity?.getSharedPreferences(Statiscated.MY_PREFSLOOP, Context.MODE_PRIVATE)
        var isLoopAllowed = prefLoop?.getBoolean("feature", false)
        if (isLoopAllowed as Boolean) {

            Statified.currentsongHelper?.isShuffle = false
            Statified.currentsongHelper?.isLoop = true
            Statified.shuffleButton?.setBackgroundColor(R.drawable.shuffle_white_icon)
            Statified.loopButton?.setBackgroundColor(R.drawable.loop_icon)

        } else {

            Statified.loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
            Statified.currentsongHelper?.isLoop = false

        }
        if (Statified.favouritecontent?.checkifIdExists(Statified.currentsongHelper?.songId?.toInt() as Int) as Boolean) {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_on))
        } else {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_off))
        }
    }

    fun clickhandler() {
        Statified.fab?.setOnClickListener({
            if (Statified.favouritecontent?.checkifIdExists(Statified.currentsongHelper?.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_off))
                Statified.favouritecontent?.deleteFavourite(Statified.currentsongHelper?.songId?.toInt() as Int)
                Toast.makeText(Statified.myActivity, "Removed from favourities", Toast.LENGTH_SHORT).show()


            } else {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_on))
                Statified.favouritecontent?.storeAsfovurite(Statified.currentsongHelper?.songId?.toInt(), currentsongHelper?.songArtist, currentsongHelper?.songTitle,
                        Statified.currentsongHelper?.songPath)
                Toast.makeText(Statified.myActivity, "Added to favourites", Toast.LENGTH_SHORT).show()
            }
        })
        Statified.shuffleButton?.setOnClickListener({
            var editorShuffle = Statified.myActivity?.getSharedPreferences(Statiscated.MYPREFSSHUFFLE, Context.MODE_PRIVATE)?.edit()
            var editorLoop = Statified.myActivity?.getSharedPreferences(Statiscated.MY_PREFSLOOP, Context.MODE_PRIVATE)?.edit()
            if (currentsongHelper?.isShuffle as Boolean) {
                Statified.shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                currentsongHelper?.isShuffle = false
                editorShuffle?.putBoolean("feature", false)
                editorShuffle?.apply()


            } else {
                currentsongHelper?.isLoop = true
                currentsongHelper?.isShuffle = false
                Statified.loopButton?.setBackgroundResource(R.drawable.loop_icon)
                Statified.shuffleButton?.setBackgroundResource(R.drawable.loop_white_icon)
                editorShuffle?.putBoolean("feature", true)
                editorShuffle?.apply()
                editorLoop?.putBoolean("feature", false)
                editorLoop?.apply()

            }


        })
        Statified.nextButton?.setOnClickListener({
            currentsongHelper?.isplaying = true
            Statified.playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            if (currentsongHelper?.isShuffle as Boolean) {
                Statiscated.playNext("playNextlikeNormalshuffle")
            } else {
                Statiscated.playNext("playNextNormal")
            }


        })
        Statified.previousButton?.setOnClickListener({
            currentsongHelper?.isplaying = true
            if (currentsongHelper?.isLoop as Boolean) {
                Statified.loopButton?.setBackgroundResource(R.drawable.loop_white_icon)

            }
            playPervivous()
        })
        Statified.loopButton?.setOnClickListener({
            var editorShuffle = Statified.myActivity?.getSharedPreferences(Statiscated.MYPREFSSHUFFLE, Context.MODE_PRIVATE)?.edit()
            var editorLoop = Statified.myActivity?.getSharedPreferences(Statiscated.MY_PREFSLOOP, Context.MODE_PRIVATE)?.edit()


            if (currentsongHelper?.isLoop as Boolean) {
                currentsongHelper?.isLoop = false
                Statified.loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
                currentsongHelper?.isShuffle = false
                editorShuffle?.putBoolean("feature", false)
                editorShuffle?.apply()


            } else {
                currentsongHelper?.isLoop = true
                currentsongHelper?.isShuffle = false
                Statified.loopButton?.setBackgroundResource(R.drawable.loop_icon)
                Statified.shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                editorShuffle?.putBoolean("feature", false)
                editorShuffle?.apply()
                editorLoop?.putBoolean("feature", true)
                editorLoop?.apply()


            }
        })

        Statified.playpauseImageButton?.setOnClickListener({
            if (Statified.mediaPlayer?.isPlaying as Boolean) {
                Statified.mediaPlayer?.pause()
                currentsongHelper?.isplaying = false
                Statified.playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            } else {
                Statified.mediaPlayer?.start()
                currentsongHelper?.isplaying = true

                Statified.playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)

            }
        })

    }


    fun playPervivous() {
        Statified.currentPosition = Statified.currentPosition - 1
        if (Statified.currentPosition == -1) {
            Statified.currentPosition == 0
        }
        if (currentsongHelper?.isplaying as Boolean) {
            Statified.playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        } else {
            Statified.playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)


        }
        currentsongHelper?.isLoop = false
        var nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
        currentsongHelper?.songPath = nextSong?.songData
        currentsongHelper?.songTitle = nextSong?.songTitle
        currentsongHelper?.currentPosition = Statified.currentPosition
        currentsongHelper?.songId = (nextSong?.songID as Long)
        Statiscated.updateTextView(currentsongHelper?.songTitle as String, currentsongHelper?.songArtist as String)

        Statified.mediaPlayer?.reset()
        try {
            Statified.mediaPlayer?.setDataSource(Statified.myActivity, Uri.parse(Statified.currentsongHelper?.songPath))
            Statified.mediaPlayer?.prepare()
            Statified.mediaPlayer?.start()
            Statiscated.processInformation(Statified.mediaPlayer as MediaPlayer)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Statified.favouritecontent?.checkifIdExists(Statified.currentsongHelper?.songId?.toInt() as Int) as Boolean) {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_on))
        } else {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myActivity!!, R.drawable.favorite_off))
        }

    }

    fun bindShaleListner() {
        Statified.mSensorListner = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                mAccelaraationLast = mAccelartionCurrent
                mAccelartionCurrent = Math.sqrt(((x * x + y * y + z * z).toDouble())).toFloat()
                val delta = mAccelartionCurrent - mAccelaraationLast
                mAccelaraation = mAccelaraation * 0.9f + delta
                if (mAccelaraation > 12) {
                    val prefs = Statified.myActivity?.getSharedPreferences(Statified.MY_PREFS_NAME, Context.MODE_PRIVATE)
                    val isAllowed = prefs?.getBoolean("feeature", false)
                    if (isAllowed as Boolean) {
                        Statiscated.playNext("PlayNextNormal")
                    }

                }


            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

    }


}// Required empty public constructor


