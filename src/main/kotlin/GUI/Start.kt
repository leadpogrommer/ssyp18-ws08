package GUI

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Image
import org.newdawn.slick.Input

class Start(gc: GameContainer) : Button(gc,
        imageCommon = Image("res/start1.png"),
        imageLighted = Image("res/start2.png"),
        imageClicked = Image("res/start3.png"),
        sizeX = gc.width.toFloat() / 3f,
        sizeY = gc.width.toFloat() / 8f,
        xButton = gc.width.toFloat() / 3f,
        yButton = gc.height.toFloat() / 12f) {

}