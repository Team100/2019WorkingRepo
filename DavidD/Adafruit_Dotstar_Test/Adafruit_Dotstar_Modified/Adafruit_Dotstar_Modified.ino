#include <SPI.h>
#include <Adafruit_DotStar.h>

#define NUMPIXELS 60

#define MODE_OFF 0
#define MODE_SOLID 1
#define MODE_BLINKING 2
#define MODE_CHASING 3
#define MODE_ALTERNATING 4

// Create the srtip

Adafruit_DotStar strip = Adafruit_DotStar(NUMPIXELS, DOTSTAR_BRG);

//---------------------------------------------------------------------------------------//
int mode = 0;
int blinkState = LOW;
// Generally, you should use "unsigned long" for variables that hold time
// The value will quickly become too large for an int to store
unsigned long currentMillis = 0;
unsigned long previousMillis = 0;        // will store last time LED was updated

// constants won't change:
const long interval = 500;
//---------------------------------------------------------------------------------------//
uint32_t color = 0x35FF00; //orange
uint32_t secondcolor = 0x000000;

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
    //String firstByte = Serial.read(); //fix this
    //String colorByte = firstByte.substring(0, 1);
    //int colorByte = Serial.read();
    int colorByte = Serial.read();
    switch (colorByte) {
      case 'R':
        mode = MODE_SOLID;
        color = 0x00FF00; //red
        break;
      case 'G':
        mode = MODE_SOLID;
        color = 0xFF0000; //green
        break;
      case 'B':
        mode = MODE_SOLID;
        color = 0x0000FF; //blue
        break;
      case 'Y':
        mode = MODE_SOLID;
        color = 0xFFFF00; //yellow
        break;
      case 'O':
        mode = MODE_SOLID;
        color = 0x35FF00; //orange
        break;
      case 'X':
        mode = MODE_OFF;
        color = 0x000000; //off
        break;
      case 'r':
        mode = MODE_BLINKING;
        color = 0x00FF00;
        break;
      case 'g':
        mode = MODE_BLINKING;
        color = 0xFF0000;
        break;
      case 'b':
        mode = MODE_BLINKING;
        color = 0x0000FF;
        break;
      case 'y':
        mode = MODE_BLINKING;
        color = 0xFFFF00;
        break;
      case '1':
        mode = MODE_CHASING;
        color = 0x00FF00;
        break;
      case '2':
        mode = MODE_CHASING;
        color = 0xFF0000;
        break;
      case '3':
        mode = MODE_CHASING;
        color = 0x0000FF;
        break;
      case '4':
        mode = MODE_CHASING;
        color = 0xFFFF00;
        break;
    }
    strip.show();
  }
  Serial.println(mode);
  switch (mode) {
    case MODE_OFF:
      simpleOn(0x000000, tail, head);
      break;
    case MODE_SOLID:
      simpleOn(color, tail, head);
      break;
    case MODE_BLINKING:
      currentMillis = millis();
      Serial.println(currentMillis);
      if (currentMillis - previousMillis >= interval) {
        previousMillis = currentMillis;
        if (blinkState == LOW) {
          blinkState = HIGH;
          simpleOn(color, tail, head);
        } else {
          blinkState = LOW;
          simpleOn(0x000000, tail, head);
        }
      }
      break;
    case MODE_CHASING:
      //Serial.println("chase");
      currentMillis = millis();
      //Serial.println(currentMillis);
      if (currentMillis - previousMillis >= interval) {
        previousMillis = currentMillis;
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, color);
          strip.show();
          delay(10);
        }
        for (int i = tail; i < head; i++) {
          strip.setPixelColor(i, 0x000000);
          strip.show();
          delay(10);
        }
        previousMillis = millis();
      }
      break;
    case MODE_ALTERNATING:
      currentMillis = millis();
      Serial.println(currentMillis);
      if (currentMillis - previousMillis >= interval) {
        previousMillis = currentMillis;
        if (blinkState == LOW) {
          blinkState = HIGH;
          simpleOn(color, tail, head);
        } else {
          blinkState = LOW;
          simpleOn(secondcolor, tail, head);
        }
      }
      break;
      strip.show();
  }
}
