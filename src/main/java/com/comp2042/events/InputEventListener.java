package com.comp2042.events;

import com.comp2042.Logic.DownData;
import com.comp2042.Logic.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    DownData onHardDropEvent(MoveEvent event);

    void createNewGame();
}
