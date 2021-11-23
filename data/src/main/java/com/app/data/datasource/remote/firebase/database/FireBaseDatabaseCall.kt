package com.app.data.datasource.remote.firebase.database

import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.entity.response.FireBaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*


class FireBaseDatabaseCall {

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null


    init {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance()
        }
        if (mDatabaseReference == null) {
            mDatabaseReference = mDatabase?.reference?.child("location")
        }
    }


    fun getDataBaseReference(): DatabaseReference? {
        mDatabaseReference = mDatabase?.reference
        return mDatabaseReference
    }


    fun <T> readSnapShot(dataSnapshot: DataSnapshot, clazz: Class<T>): ArrayList<T> {
        //         ArrayList<Clazz> tempArray=new ArrayList<>();
        val arrayList = ArrayList<T>()
        for (childSnapshot in dataSnapshot.children) {
            // key for node .!
            // String key = childSnapshot.getKey();
            val `object` = childSnapshot.getValue(clazz) as T
            arrayList.add(`object`)
        }
        return arrayList
    }


    fun <T> readSnapShotKey(
        dataSnapshot: DataSnapshot,
        arrayList: ArrayList<T>, clazz: Class<*>
    ): ArrayList<T> {
        for (childSnapshot in dataSnapshot.children) {
            val key = childSnapshot.key
            val `object` = childSnapshot.getValue(clazz) as T
            arrayList.add(`object`)
        }
        return arrayList
    }


    suspend fun setDatabaseValue(firebaseDatabaseRequest: FirebaseDatabaseRequest): FirebaseDatabaseCallResponse<FireBaseDatabase>? {
        Timber.d("firebaseDatabaseRequest data-->$firebaseDatabaseRequest")
        return try {
            val data = mDatabaseReference?.setValue(
                firebaseDatabaseRequest.data.toString()
            )?.await()
            FirebaseDatabaseCallResponse.Success(FireBaseDatabase(data.toString()))
        } catch (exception: Exception) {
            FirebaseDatabaseCallResponse.Failure(exception)
        }
    }


//    private fun data() {
//        mDatabaseReference?.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val string = dataSnapshot.getValue(String::class.java)
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//
//            }
//        })
//    }


}