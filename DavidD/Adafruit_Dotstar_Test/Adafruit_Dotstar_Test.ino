#include <SPI.h>
#include <Adafruit_DotStar.h>

#define NUMPIXELS 60

// Create the srtip

Adafruit_DotStar strip = Adafruit_DotStar(NUMPIXELS, DOTSTAR_BRG);

//---------------------------------------------------------------------------------------//
int ledState = LOW;
bool doblinkR = false;
bool doblinkG = false;
bool doblinkB = false;
bool doblinkY = false;
bool dochaseR = false;
bool dochaseG = false;
bool dochaseB = false;
bool dochaseY = false;
// Generally, you should use "unsigned long" for variables that hold time
// The value will quickly become too large for an int to store
unsigned long previousMillis = 0;        // will store last time LED was updated

// constants won't change:
const long interval = 500;
//---------------------------------------------------------------------------------------//
uint32_t color = 0x35FF00; //orange

void noDos() {
  doblinkR = false;
  doblinkG = false;
  doblinkB = false;
  doblinkY = false;
  dochaseR = false;
  dochaseG = false;
  dochaseB = false;
  dochaseY = false;
}

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
        Serial.println('R');
        noDos();
        color = 0x00FF00; //red 
        break;
      case 'G':
        Serial.println('G');
        noDos();
        color = 0xFF0000; //green
        break;
      case 'B':
        Serial.println('B');
        noDos();
        color = 0x0000FF; //blue
        break;
      case 'Y':
        Serial.println('Y');
        noDos();
        color = 0xFFFF00; //yellow
        break;
      case 'X':
        Serial.println('X');
        noDos();
        color = 0x000000; //off
        break;
      case 'r':
        Serial.println("Rblink");
        noDos();
        doblinkR = true;
        break;
      case 'g':
        Serial.println("Gblink");
        noDos();
        doblinkG = true;
        break;
      case 'b':
        Serial.println("Bblink");
        noDos();
        doblinkB = true;
        break;
      case 'y':
        Serial.println("Yblink");
        noDos();
        doblinkY = true;
        break;
      case 'O':
        Serial.println('O');
        noDos();
        color = 0x35FF00;
        break;
      case '1':
        Serial.println("Rchase");
        noDos();
        dochaseR = true;
        break;
      case '2':
        Serial.println("Gchase");
        noDos();
        dochaseG = true;
        break;
      case '3':
        Serial.println("Bchase");
        noDos();
        dochaseB = true;
        break;
      case '4':
        Serial.println("Ychase");
        noDos();
        dochaseY = true;
        break;
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
    color = 0x000000;
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x00FF00);
          strip.show();
          delay(10);
          color = 0x000000;
        }
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x000000);
          strip.show();
          delay(10);
          color = 0x000000;
        }
    }
    simpleOn(color, tail, head);
  }
  if (dochaseG) {
    color = 0x000000;
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0xFF0000);
          strip.show();
          delay(10);
          color = 0x000000;
        }
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x000000);
          strip.show();
          delay(10);
          color = 0x000000;
        }
    }
    simpleOn(color, tail, head);
  }
  if (dochaseB) {
    color = 0x000000;
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x0000FF);
          strip.show();
          delay(10);
          color = 0x000000;
        }
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x000000);
          strip.show();
          delay(10);
          color = 0x000000;
        }
    }
    simpleOn(color, tail, head);
  }
  if (dochaseY) {
    color = 0x000000;
    unsigned long currentMillis = millis();
    Serial.println(currentMillis);
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0xFFFF00);
          strip.show();
          delay(10);
          color = 0x000000;
        }
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x000000);
          strip.show();
          delay(10);
          color = 0x000000;
        }
    }
    simpleOn(color, tail, head);
  }
  if(!doblinkR && !doblinkG && !doblinkB && !doblinkY && !dochaseR && !dochaseG && !dochaseB && !dochaseY){
    Serial.println("Solid Color");
    simpleOn(color, tail, head);
  }
}
