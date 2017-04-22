package com.mercury.platform.ui.frame.setup.scale


import com.mercury.platform.ui.frame.AbstractScalableComponentFrame
import com.mercury.platform.ui.frame.setup.SetUpCommander

class SetUpScaleCommander : SetUpCommander<AbstractScalableComponentFrame>() {
    override fun setUpAllExclude(framesClasses: Array<Class<*>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUpAll() {
        frames.forEach { frame -> frame.setState(ScaleState.ENABLE) }
    }


    override fun setOrEndUp(frameClass: Class<*>, showingSetUpFrame: Boolean) {

    }

    override fun endUpAll() {
        frames.forEach { frame -> frame.setState(ScaleState.DEFAULT) }
    }

    override fun endUp(frameClass: Class<*>) {

    }
}
