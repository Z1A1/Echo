package com.internshala.echo

import android.os.Parcelable

/**
 * Created by admin on 3/1/2018.
 */
class CurrentsongHelper {
    var songArtist: String? = null
    var songTitle: String? = null
    var songPath: String? = null
    var songId: Long=0
    var currentPosition: Int = 0
    var isplaying: Boolean = false
    var isLoop: Boolean = false
    var isShuffle: Boolean = false
    var trackPosition: Int = 0


}