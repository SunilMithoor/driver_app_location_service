package com.app.data.datasource.remote.firebase.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


interface FireBaseSnapShotResult {

    //todo Uncomment code to use Classes
    fun onFireBaseCallComplete(dataSnapshot: DataSnapshot)
    fun onFireBaseCallFailure(databaseError: DatabaseError)

}