package com.drakkun.kelron

import org.jetbrains.anko.*
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Samantha on 5/20/2017.
 */

//Coming soon!
/*interface RSVPServive {
    @GET("/rsvps")
    Call<List<RSVP>> listRSVPs(@Path("rsvps") name: String)
}*/

class API {

    companion object {

        fun pretendToRetrieveFromServer(ctx: MainActivity) {
            val dialog = ctx.indeterminateProgressDialog("Fetching data...")
            doAsync {
                Thread.sleep(2000)
                uiThread {
                    if(dialog.isShowing) dialog.dismiss()
                }
                onComplete {
                    val rsvps = arrayListOf("This content has totes changed", "this is a new item", "here's another item")
                    ctx.toast("Finished.")
                    ctx.infoRetrieved(rsvps)
                }
            }
        }
    }
}
