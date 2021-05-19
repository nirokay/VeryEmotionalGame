import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class VeryEmotionalGame extends PApplet {

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
        int c = get(x, y);
        if (c == 0xffffffff) {
          // White to Black
          set(x, y, 0xff000000);
        } else {
          // Black to White
          set(x, y, 0xffffffff);
        }
      }
    }
  }
}

public class Emoticon {

  private float x, y;         // Position
  private int textS;          // Text Font SIze

  private int task;           // Emote State
  private int state;          // Emotion

  private int aniS;           // Animation Speed
  private int aniF = 0;       // Animation Frame

  private String emote;       // Display Emote
  private String eyeL;        // Left eye
  private String eyeR;        // Right eye
  private String mouth;       // Mouth
  private String faceL;       // Left edge of the emote
  private String faceR;       // Right edge of the emote

  // Tasks
  private static final int tIDLE = 1;
  private static final int tFOLLOWING = 2;
  // Emotions
  private static final int eHAPPY = 1;
  private static final int eSAD = 2;
  private static final int eOWO = 3;
  private static final int eANRGY = 4;

  Emoticon(float tempX, float tempY, int tempTextSize, int tempAniS) {
    // Position
    x = tempX;
    y = tempY;
    // Text
    textS = tempTextSize;
    // Animation Speed
    aniS = tempAniS;
  }



  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------
  //                                         DISPLAY
  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------

  private void display() {
    int f = round(clock()*100);
    animationCount();
    taskCheck();
    emotionCheck();

    if (day) {
      blinking(f);
      pat();
    } else {
      sleep();
    }
    emote = faceL + eyeL + mouth + eyeR + faceR;
    fill(0);
    textAlign(CENTER, CENTER);
    textSize(textS);
    text(emote, x, y);
  }


  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------
  //                                      CONTROL STUFF
  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------
  
  // Animation Frame Count
  private void animationCount() {
    if (aniF >= aniS) {
      aniF = 0;
    } else {
      aniF = aniF +1;
    }
  }
  
  // Task Control
  private void taskCheck() {
    switch(task) {
    case tFOLLOWING:
      if (day) {
        following("°", "O", "o");
      }
      break;

    case tIDLE:
      faceL = "( ";
      faceR = " )";
      break;
    }
  }
  
  // Emotions Control
  public void emotionCheck() {
    int f = round(clock()*100);
    // nothing to see here yet lol
  }

  // Blinking Animation
  private void blinking(int frame) {
    if (frame >= 40 && frame < 45) {
      eyeL = "-";
      eyeR = "-";
    }
  }
  
  
  
  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------
  //                                    PLAYER INTERACTION
  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------

  // Sleep Animation
  private void sleep() {
    float c = clock();
    faceL = "( ";
    eyeL = "-";
    eyeR = "-";
    faceR = " )";
    poke();
    // Snoring
    if (c >= 0.5f) {
      mouth = "o";
    } else {
      mouth = ".";
    }
  }
  
  // Poking (while sleeping) Animation
  private void poke() {
    if (mousePressed && mQ() == 5) {
      eyeL = ">";
      eyeR = "<";
    }
  }

  // Pat Animation
  private void pat() {
    if (mousePressed && mQ() == 5) {
      eyeL = "—";
      mouth = "w";
      eyeR = "—";
    }
  }



  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------
  //                                    EMOTION ANIMATIONS
  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------

  // Clock (in percetage)
  private float clock() {
    return PApplet.parseFloat(aniF)/PApplet.parseFloat(aniS);
  }

  // Follow the mouse
  private void following(String t, String m, String b) {
    mouth = " ";
    // Mouse Qudrant
    // --------------
    //     1 2 3
    //     4 5 6
    //     7 8 9
    // --------------
    switch(mQ()) {
    case 1:
      faceL = "(";
      eyeL = t;
      eyeR = t;
      faceR = "  )";
      break;

    case 2:
      faceL = "( ";
      eyeL = t;
      eyeR = t;
      faceR = " )";
      break;

    case 3:
      faceL = "(  ";
      eyeL = t;
      eyeR = t;
      faceR = ")";
      break;

    case 4:
      faceL = "(";
      eyeL = m;
      eyeR = m;
      faceR = "  )";
      break;

    case 5:
      faceL = "( ";
      eyeL = m;
      eyeR = m;
      faceR = " )";
      break;

    case 6:
      faceL = "(  ";
      eyeL = m;
      eyeR = m;
      faceR = ")";
      break;

    case 7:
      faceL = "(";
      eyeL = b;
      eyeR = b;
      faceR = "  )";
      break;

    case 8:
      faceL = "( ";
      eyeL = b;
      eyeR = b;
      faceR = " )";
      break;

    case 9:
      faceL = "(  ";
      eyeL = b;
      eyeR = b;
      faceR = ")";
      break;

    default:
      faceL = "( ";
      eyeL = "○";
      eyeR = "○";
      faceR = " )";
      break;
    }
  }




  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------
  //                                  MOUSE POSITION STUFF
  // ------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------

  // Calculate Mouse Position Quadrant (3x3)
  private int mQ() {
    int p = 0;
    // Mouse Qudrant
    // --------------
    //     1 2 3
    //     4 5 6
    //     7 8 9
    // --------------

    switch(whereY()) {
    case 1:
      switch(whereX()) {
      case 1:
        p = 1;
        break;

      case 2:
        p = 2;
        break;

      case 3:
        p = 3;
        break;
      }
      break;

    case 2:
      switch(whereX()) {
      case 1:
        p = 4;
        break;

      case 2:
        p = 5;
        break;

      case 3:
        p = 6;
        break;
      }
      break;

    case 3:
      switch(whereX()) {
      case 1:
        p = 7;
        break;

      case 2:
        p = 8;
        break;

      case 3:
        p = 9;
        break;
      }
      break;
    }
    return p;
  }

  // In which row is the mouse?
  private int whereY() {
    if (mouseY <= height/3) {
      // Top Row
      return 1;
    } else if (mouseY > height/3 && mouseY < 2*height/3) {
      // Middle Row
      return 2;
    } else if (mouseY >= 2*height/3) {
      // Bottom Row
      return 3;
    } else {
      // Outside (Return Middle)
      return 2;
    }
  }
  // In which collumn is the mouse?
  private int whereX() {
    if (mouseX <= width/3) {
      // Left Row
      return 1;
    } else if (mouseX > width/3 && mouseX < 2*width/3) {
      // Middle Row
      return 2;
    } else if (mouseX >= 2*width/3) {
      // Right Row
      return 3;
    } else {
      // Outside (Return Middle)
      return 2;
    }
  }
}
  public void settings() {  size(500, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "VeryEmotionalGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
