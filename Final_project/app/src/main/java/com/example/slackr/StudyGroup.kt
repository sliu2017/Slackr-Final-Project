package com.example.slackr

import android.os.Parcel
import android.os.Parcelable

class StudyGroup() : Parcelable {
    var groupName: String? = null
    var groupDescription: String? = null
    var groupLogistics: String? = null
    var groupLearningStyle: String? = null
    var groupParticipantLimit: String? = null
    var groupSubject: String? = null
    var groupSubjectCode: String?= null
    var searchKey: String ? = null
    var createdBy: String ? = null

    constructor(parcel: Parcel) : this() {
        groupName = parcel.readString()
        groupDescription = parcel.readString()
        groupLogistics = parcel.readString()
        groupLearningStyle = parcel.readString()
        groupParticipantLimit = parcel.readString()
        groupSubject = parcel.readString()
        groupSubjectCode = parcel.readString()
    }


    constructor(
        groupName: String?,
        groupDescription: String?,
        groupLogistics: String?,
        groupParticipantLimit: String,
        groupSubject: String,
        groupSubjectCode: String,
        createdBy:String
    ) : this() {
        this.groupName= groupName
        this.groupDescription = groupDescription
        this.groupLogistics= groupLogistics
        this.groupParticipantLimit = groupParticipantLimit
        groupLearningStyle=""
        searchKey=""
        this.createdBy=createdBy
        this.groupSubject= groupSubject
        this.groupSubjectCode= groupSubjectCode
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(groupName)
        parcel.writeString(groupDescription)
        parcel.writeString(groupLogistics)
        parcel.writeString(groupLearningStyle)
        parcel.writeString(groupParticipantLimit)
        parcel.writeString(groupSubject)
        parcel.writeString(groupSubjectCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudyGroup> {
        override fun createFromParcel(parcel: Parcel): StudyGroup {
            return StudyGroup(parcel)
        }

        override fun newArray(size: Int): Array<StudyGroup?> {
            return arrayOfNulls(size)
        }
    }
}