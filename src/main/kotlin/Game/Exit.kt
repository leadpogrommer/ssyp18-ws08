package Game

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Image
import org.newdawn.slick.Input

class Exit(gc: GameContainer) : GUI.Button(gc,
        imageCommon = Image("res/exit1.png"),
        imageLighted = Image("res/exit2.png"),
        imageClicked = Image("res/exit3.png"),
        sizeX = gc.width.toFloat() / 3f,
        sizeY = gc.width.toFloat() / 8f,
        xButton = gc.width.toFloat() / 3f,
        yButton = gc.height.toFloat() / 1.7f) {
}