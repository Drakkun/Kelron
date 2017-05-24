package com.drakkun.kelron

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    var someList: ArrayList<String> = arrayListOf("Dummy content 1", "Another dummy", "This one's smart tho")
    lateinit var adapter: MainActivityAdapter
    lateinit var swipeRefresh: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)

        updateUI()
        swipeRefresh = find<SwipeRefreshLayout>(R.id.swipe_refresh_rsvps)
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        title = "RSVPs for Kelron"
    }

    fun updateUI() {
        val recyclerView = find<RecyclerView>(R.id.recycler_main)
        val layoutManager = LinearLayoutManager(ctx)

        adapter = MainActivityAdapter(someList, this)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun retrieveData() {
        API.pretendToRetrieveFromServer(this)
        swipeRefresh.isRefreshing = false
    }

    //Todo make this list hold RSVP objects
    fun infoRetrieved(rsvps: ArrayList<String>) {
        someList = rsvps
        updateUI()
    }

    override fun onRefresh() {
        retrieveData()
    }
}

class MainActivityHolder(var rsvpView: View) : RecyclerView.ViewHolder(rsvpView)

class MainActivityAdapter(var someList: ArrayList<String>, val ctx: MainActivity) : RecyclerView.Adapter<MainActivityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainActivityHolder {
        val ankoContext = AnkoContext.create(ctx, parent!!)
        val component = RsvpUI()

        return MainActivityHolder(component.createView(ankoContext))
    }

    override fun onBindViewHolder(holder: MainActivityHolder?, position: Int) {
        val itemView = holder!!.rsvpView
        val rsvper = itemView.find<TextView>(R.id.txt_rsvper_name)
        val name = someList[position]

        rsvper.text = name

        itemView.onClick {
            ctx.toast("Reverting data.")
            this@MainActivityAdapter.someList = arrayListOf("Dummy content 1", "Another dummy", "This one's smart tho")
            this@MainActivityAdapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return someList.size
    }
}

private class MainActivityUI : AnkoComponent<MainActivity> {

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent)
            textView {
                id = R.id.txt_total_attendees
                text = "Total so far: 3"
                textSize = 20f
                textColor = Color.BLACK
            }.lparams {
                alignParentTop()
                centerHorizontally()
                verticalMargin = dip(7)
            }
            swipeRefreshLayout {
                id = R.id.swipe_refresh_rsvps
                recyclerView {
                    id = R.id.recycler_main
                }
            }.lparams(matchParent) {
                below(R.id.txt_total_attendees)
                topMargin = dip(8)
            }
        }
    }
}

private class RsvpUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(matchParent)
            textView {
                id = R.id.txt_rsvper_name
                textSize = 17f
            }.lparams {
                verticalMargin = dip(8)
                horizontalMargin = dip(5)
                gravity = Gravity.START
            }
            /*textView {
                id = R.id.txt_total_in_party
                textSize = 15f
                textColor = Color.BLUE
            }.lparams {
                //send to the right of the screen
            }*/
        }
    }
}