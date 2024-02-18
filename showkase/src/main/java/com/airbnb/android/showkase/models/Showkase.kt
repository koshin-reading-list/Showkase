package com.airbnb.android.showkase.models

/**
 * Object that will be the receiver for all extension functions that are generated as part of 
 * Showkase. Some examples of extension funtions that will be generated for your use are 
 * "createShowkaseBrowserIntent" that allows you to start the ShowkaseBrowser and 
 * "getShowkaseMetadata" that gives you access to all the metadata that's available to Showkase.
 */
// 不太常见的思路，只定义了一个 API 入口，使用 extension 的方式去实现。
object Showkase
