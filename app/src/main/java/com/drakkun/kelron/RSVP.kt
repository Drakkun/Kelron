package com.drakkun.kelron

/**
 * Created by Samantha on 5/20/2017.
 */

data class RSVP(
        val id: Int,
        val rsvper: String,
        val totalInParty: Int,
        val email: String?
)