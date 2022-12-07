package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

public interface FrameAdapter {

    int x();

    int y();

    int width();

    int height();

    Rectangle canvasBounds();

    Rectangle bounds();

    String title();

    void setCursor(Cursor cursor);

    boolean hasFocus();

    boolean isVisible();

    void setTitle(String title);

    DisplayMode displayMode();

    Cursor createCustomCursor(Image cursor, Point hotSpot, String name);

    DisplayMode[] displayModes();

    void dispose();

    void setDisplayMode(DisplayMode displayMode);

    void setFullScreenWindow(Window window);

    void setSelfAsFullscreenWindow();

    void setLocationRelativeTo(Component component);

    void setBounds(int x, int y, int width, int height);

    void setSize(int width, int height);

    void setResizable(boolean resizable);

    void setVisible(boolean visible);

    void setIgnoreRepaint(boolean ignoreRepaint);

    void createCanvasBufferStrategy();

    Graphics2D canvasDrawGraphics();

    void showDrawGraphics();

    Insets insets();

    BufferedImage createScreenCapture(Rectangle screenRect);

    void addWindowListener(WindowListener windowListener);

    void addMouseListener(MouseListener mouseListener);

    void addMouseMotionListener(MouseMotionListener mouseMotionListener);

    void addMouseWheelListener(MouseWheelListener mouseWheelListener);

    void addKeyListener(KeyListener keyListener);

}
