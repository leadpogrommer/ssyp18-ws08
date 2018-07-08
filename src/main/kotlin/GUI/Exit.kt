package GUI

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Image
import org.newdawn.slick.Input

class Exit(gc: GameContainer) : Button(gc,
        imageCommon = Image("res/exit1.png"),
        imageLighted = Image("res/exit2.png"),
        imageClicked = Image("res/exit3.png"),
        sizeX = gc.screenWidth.toFloat() / 3f,
        sizeY = gc.screenWidth.toFloat() / 8f,
        xButton = gc.screenWidth.toFloat() / 3f,
        yButton = gc.screenHeight.toFloat() / 1.7f) {
}