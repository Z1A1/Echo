package com.internshala.echo.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.internshala.echo.R


/**
 * A simple [Fragment] subclass.
 */
class AboutUsFragment : Fragment() {
    var profilephoto:ImageView?=null
    var  developerdetails:TextView?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view= inflater!!.inflate(R.layout.fragment_about_us, container, false)
        profilephoto=view?.findViewById(R.id.joey)
        developerdetails=view?.findViewById(R.id.details)
        activity?.title="About us"
        return view
    }

}// Required empty public constructor
