package com.example.metalhealthapp.Model


data class YTVideoResponse(
    val success : Boolean,
    val data : YTVideoData
)

data class YTVideoData(
    val videos : List<YtVideo>
)

data class YtVideo(
    val tag : String,
    val videoId : String,
    val title : String,
    val thumbnail : String,
    val channelTitle : String,
    val url : String
)