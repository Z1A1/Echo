package com.internshala.echo

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by admin on 3/3/2018.
 */
class Songs(var songID: Long, var songTitle: String, var artist: String, var songData: String, var dateAdded: Long) : Parcelable {
    override fun describeContents(): Int {
        return 0


    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {

    }

    object Statified {
        var nameComparable: Comparator<Songs> = Comparator<Songs> { song1, song2 ->
            val songone = song1.songTitle.toUpperCase()
            val songTwo = song2.songTitle.toUpperCase()
            songone.compareTo(songTwo)

        }
        var dateComparator: Comparator<Songs> = Comparator<Songs> { song1, song2 ->
            val songOne = song1.dateAdded.toDouble()
            val songTwo = song2.dateAdded.toDouble()
            songOne.compareTo(songTwo)
        }
    }


}
