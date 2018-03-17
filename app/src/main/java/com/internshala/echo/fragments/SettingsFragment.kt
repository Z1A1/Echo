package com.internshala.echo.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import com.internshala.echo.R


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    var myActivity: Activity? = null

    object Statified {
        var MY_PRESS_NAMW = "shakefeature"
    }


    var shakeSwitch: Switch? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_settings, container, false)
        activity?.title = "Settings"


        shakeSwitch = view?.findViewById(R.id.switchShake)
        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = myActivity?.getSharedPreferences(Statified.MY_PRESS_NAMW, Context.MODE_PRIVATE)
        val isAllowed = prefs?.getBoolean("feature", false)
        if (isAllowed as Boolean) {
            shakeSwitch?.isChecked = true

        } else {
            shakeSwitch?.isChecked = false

        }
        shakeSwitch?.setOnCheckedChangeListener({ CompoundButton, b ->
            if (b) {
                val editor = myActivity?.getSharedPreferences(Statified.MY_PRESS_NAMW, Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature", true)
                editor?.apply()

            } else {
                val editor = myActivity?.getSharedPreferences(Statified.MY_PRESS_NAMW, Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature", false)
                editor?.apply()


            }

        })

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = context as Activity
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item=menu?.findItem(R.id.action_sort)
        item?.isVisible=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


}// Required empty public constructor
