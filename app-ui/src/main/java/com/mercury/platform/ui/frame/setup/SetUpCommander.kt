package com.mercury.platform.ui.frame.setup

import java.util.ArrayList

abstract class SetUpCommander<T> {
    protected var frames: MutableList<T> = ArrayList()
    protected var activeFrames: MutableList<T> = ArrayList()

    fun addFrame(frame: T) {
        frames.add(frame)
    }

    abstract fun setUpAll()
    abstract fun setUpAllExclude(framesClasses: Array<Class<*>>)
    abstract fun setOrEndUp(frameClass: Class<*>, showingSetUpFrame: Boolean)
    abstract fun endUpAll()
    abstract fun endUp(frameClass: Class<*>)
}
