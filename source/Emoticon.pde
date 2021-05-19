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
    if (c >= 0.5) {
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
    return float(aniF)/float(aniS);
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
