package com.mercury.platform.ui.frame.setup.location

import com.mercury.platform.ui.frame.movable.AbstractMovableComponentFrame
import com.mercury.platform.ui.frame.other.SetUpLocationFrame
import com.mercury.platform.ui.frame.setup.SetUpCommander
import com.mercury.platform.ui.manager.FramesManager

import java.util.*
import java.util.function.Consumer

class SetUpLocationCommander : SetUpCommander<AbstractMovableComponentFrame>() {
    override fun setUpAllExclude(framesClasses: Array<Class<*>>) {
        val framesList = Arrays.asList(*framesClasses)
        frames.forEach { frame ->
            if (!framesList.contains(frame.javaClass)) {
                enableMovement(frame, true)
                activeFrames.add(frame)
            }
        }
    }

    override fun setUpAll() {
        frames.forEach { frame ->
            enableMovement(frame, true)
            activeFrames.add(frame)
        }
    }


    override fun setOrEndUp(frameClass: Class<*>, showingSetUpFrame: Boolean) {
        val frame = frames.stream()
                .filter { it -> it.javaClass == frameClass }
                .findFirst().get()
        if (frame.moveState == LocationState.DEFAULT) {
            enableMovement(frame, showingSetUpFrame)
            activeFrames.add(frame)
        } else {
            disableMovement(frame)
            activeFrames.remove(frame)
        }
    }

    override fun endUpAll() {
        activeFrames.forEach(Consumer<AbstractMovableComponentFrame> { this.disableMovement(it) })
        activeFrames.clear()
    }

    override fun endUp(frameClass: Class<*>) {
        val frame = frames.stream()
                .filter { it -> it.javaClass == frameClass }
                .findFirst().get()
        disableMovement(frame)
        activeFrames.remove(frame)
    }

    private fun enableMovement(frame: AbstractMovableComponentFrame, showSetUpFrame: Boolean) {
        frame.setState(LocationState.MOVING)
        if (showSetUpFrame) {
            FramesManager.INSTANCE.showFrame(SetUpLocationFrame::class.java)
        }
    }

    private fun disableMovement(frame: AbstractMovableComponentFrame) {
        frame.setState(LocationState.DEFAULT)
        FramesManager.INSTANCE.hideFrame(SetUpLocationFrame::class.java)
    }
}
