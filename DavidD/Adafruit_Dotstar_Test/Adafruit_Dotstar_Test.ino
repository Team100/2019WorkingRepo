#include <SPI.h>
#include <Adafruit_DotStar.h>


#define NUMPIXELS 60


// Create the srtip


Adafruit_DotStar strip = Adafruit_DotStar(NUMPIXELS, DOTSTAR_BRG);

//---------------------------------------------------------------------------------------//
int ledState = LOW;     // ledState used to set the LED
bool doblinkR = false;
bool doblinkG = false;
bool doblinkB = false;
bool doblinkY = false;
bool dochaseR = false;
int chaseR = 0;
// Generally, you should use "unsigned long" for variables that hold time
// The value will quickly become too large for an int to store
unsigned long previousMillis = 0;        // will store last time LED was updated

// constants won't change:
const long interval = 500;
//---------------------------------------------------------------------------------------//
uint32_t color = 0x35FF00; //orange

void simpleOn(uint32_t color, int tail, int head) {
  for (int i = tail; i < head; i++) {
    strip.setPixelColor(i, color);
  }
  strip.show();
}

void setup() {
  Serial.begin(9600); //initializes the serial port
  strip.begin(); //initializes the strip
  strip.show(); //turn off LEDs within NONPIXELS
}


void loop() {
  int tail = 0;
  int head = 60;
  if (Serial.available() > 0) {
    int colorByte = Serial.read();
    switch (colorByte) {
      case 'R':
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        color = 0x00FF00; //red
        Serial.println('R');
        break;
      case 'G':
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        color = 0xFF0000; //green
        Serial.println('G');
        break;
      case 'B':
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        color = 0x0000FF; //blue
        Serial.println('B');
        break;
      case 'Y':
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        color = 0xFFFF00; //yellow
        Serial.println('Y');
        break;
      case 'X':
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        color = 0x000000; //off
        Serial.println('X');
        break;
      case 'r':
        Serial.println("Rblink");
        doblinkR = true;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        break;
      case 'g':
        Serial.println("Gblink");
        doblinkR = false;
        doblinkG = true;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        break;
      case 'b':
        Serial.println("Bblink");
        doblinkR = false;
        doblinkG = false;
        doblinkB = true;
        doblinkY = false;
        dochaseR = false;
        break;
      case 'y':
        Serial.println("Yblink");
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = true;
        dochaseR = false;
        break;
      case 'O':
        Serial.println('O');
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = false;
        color = 0x35FF00;
        break;
      case '1':
        Serial.println("Rchase");
        doblinkR = false;
        doblinkG = false;
        doblinkB = false;
        doblinkY = false;
        dochaseR = true;
        color = 0x00FF00;
        for (int i = tail; i < head; i += 2) {
          strip.setPixelColor(i + chaseR, color);
        }
    }
    strip.show();
  }
  if (doblinkR) {
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      if (ledState == LOW) {
        ledState = HIGH;
        color = 0x00FF00;
      } else {
        ledState = LOW;
        color = 0x000000;
      }
    }
    simpleOn(color, tail, head);
  }
  if (doblinkG) {
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      if (ledState == LOW) {
        ledState = HIGH;
        color = 0xFF0000;
      } else {
        ledState = LOW;
        color = 0x000000;
      }
    }
    simpleOn(color, tail, head);
  }
  if (doblinkB) {
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      if (ledState == LOW) {
        ledState = HIGH;
        color = 0x0000FF;
      } else {
        ledState = LOW;
        color = 0x000000;
      }
    }
    simpleOn(color, tail, head);
  }
  if (doblinkY) {
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      if (ledState == LOW) {
        ledState = HIGH;
        color = 0xFFFF00;
      } else {
        ledState = LOW;
        color = 0x000000;
      }
    }
    simpleOn(color, tail, head);
  }
  if (dochaseR) {
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      if (chaseR = 0) {
        ledState = HIGH;
        chaseR = 1;
      } else {
        ledState = LOW;
        chaseR = 0;
      }
        for (int i = tail; i < head; i+=2) {
          strip.setPixelColor(i + chaseR, color);
        }
        strip.show();
    }
    simpleOn(color, tail, head);
  }
}
