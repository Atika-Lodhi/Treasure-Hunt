package Main;

import java.awt.*;

// EventReact extends java.awt.Rectangle to add specific fields for event handling.
public class EventReact extends Rectangle {

    // Stores the default X offset of this event rectangle within its specific world tile.
    int eventRectDefaultX;

    // Stores the default Y offset of this event rectangle within its specific world tile.
    int eventRectDefaultY;

    // Flag indicating whether the event associated with this rectangle has already been triggered and completed.
    boolean eventDone = false;
}