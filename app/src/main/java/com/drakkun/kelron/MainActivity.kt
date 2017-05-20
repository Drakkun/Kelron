package com.drakkun.kelron

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    var someList: ArrayList<String> = arrayListOf("Dummy content 1", "Another dummy", "This one's smart tho")
    var adapter = MainActivityAdapter(someList, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)

        initRecyclerView()
    }

    fun initRecyclerView() {
        val recyclerView = find<RecyclerView>(R.id.recycler_main)
        val layoutManager = LinearLayoutManager(ctx)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun retrieveData() {
        API.pretendToRetrieveFromServer(this)
    }

    //Todo make this list hold RSVP objects
    fun infoRetrieved(rsvps: ArrayList<String>) {
        someList = rsvps
        adapter.notifyDataSetChanged()
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
            ctx.someList = arrayListOf("Dummy content 1", "Another dummy", "This one's smart tho")
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
            swipeRefreshLayout {
                id = R.id.swipe_refresh_rsvps
                recyclerView {
                    id = R.id.recycler_main
                }
            }.lparams {
                topMargin = dip(8)
            }
        }
    }
}

private class RsvpUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            textView {
                id = R.id.txt_rsvper_name
            }.lparams {
                margin = dip(5)
            }
            //for later
            /*textView {
                id = R.id.txt_total_in_party
            }*/
        }
    }
}