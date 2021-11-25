package com.goodswarehouse.scanner.data.di

import javax.inject.Qualifier

///////
typealias RestQualifier = String

const val REST_SERVER: RestQualifier = "server rest"
const val REST_GENERAL: RestQualifier = "general"

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Rest(val value: String = "")

/////

typealias TrackQualifier = String
const val TRACK_DATABASE: TrackQualifier = "Track database"

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Track(val value: TrackQualifier = "")
