// ------------------------------------------------------------------------------------------
// -------------------------------------   v1.0   -------------------------------------------
//                                  Very Emotional Game
// --------------------------   Source Code  |  made by Niro   ------------------------------
// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------

Emoticon emo;
// Image Files
PImage curs;
PImage curs_grab;
PImage moon;
PImage sun;

public boolean debug = false;
public boolean day = true;
public int mouseCooldown;
// Day-Night Radius (Circle)
public int invRadius = 0;



// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------
//                                          SETUP
// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------

public void setup() {
  size(500, 600);
  frameRate(60);
  surface.setTitle("Very Emotional Game");
  surface.setResizable(false);
  // Cursor
  noCursor();
  curs = loadImage("data/cursor/cursor.png");
  curs_grab = loadImage("data/cursor/cursor_grab.png");
  //Images
  moon = loadImage("data/graphics/moon.png");
  sun = loadImage("data/graphics/sun.png");
  // Class Stuff
  emo = new Emoticon(width/2, height/2, 40, 300);
}



// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------
//                                          DRAW
// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------

public void draw() {
  background(255);
  debugPrint();
  // Emoticon
  emo.task = 2;
  emo.display();

  timeButton(20, 20, 50, 10);
  // Colour Inverter
  colourInvert(45, 45);
  // Cursor
  customCursor(20);
  mouseCooldownUpdate();
}

public void debugPrint() {
  // Enable / Disable
  if (keyPressed && key == 'd' && mouseCooldown == 0) {
    mouseCooldown = 20;
    debug = !debug;
  }
  if (debug) {
    println("Frame: " + emo.aniF + "   MaxFrame: " + emo.aniS + "   Clock: " + emo.clock());
    println("Mouse Quadrant: " + emo.mQ());
    
    textAlign(CENTER, CENTER);
    textSize(15);
    fill(0);
    text("<< Debug Console Printing enabled >>", width/2, height - 40);
    textSize(10);
    text("press d again to disable", width/2, height - 20);
  }
}




// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------
//                                         CURSOR
// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------

public void customCursor(int s) {
  if (mousePressed) {
    curs_grab.resize(s, s);
    image(curs_grab, mouseX, mouseY);
  } else {
    curs.resize(s, s);
    image(curs, mouseX, mouseY);
  }
}

public void mouseCooldownUpdate() {
  if (mouseCooldown > 0) {
    mouseCooldown = mouseCooldown - 1;
  }
}



// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------
//                                       TIME CYCLE
// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------

public void timeButton(float x, float y, int s, float e) {
  fill(0);
  rect(x, y, s, s, e);
  if (day) {
    sun.resize(s, s);
    image(sun, x, y);
  } else {
    moon.resize(s, s);
    image(moon, x, y);
  }

  if (mousePressed && (mouseX >= x && mouseX <= x+s) && (mouseY >= y && mouseY <= y+s) && mouseCooldown <= 0) {
    mouseCooldown = 100;
    day = !day;
  }
}

public void colourInvert(float cX, float cY) {
  // Counter
  if (day && invRadius >= 0) {
    // Shrinking Circle
    invRadius = invRadius -10;
  } else if (invRadius <= sqrt(sq(width)+sq(height))) {
    // Grow Circle
    invRadius = invRadius +10;
  }
  // Reverse white and black
  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      if (dist(cX, cY, x, y) < invRadius) {
        color c = get(x, y);
        if (c == #ffffff) {
          // White to Black
          set(x, y, #000000);
        } else {
          // Black to White
          set(x, y, #ffffff);
        }
      }
    }
  }
}
