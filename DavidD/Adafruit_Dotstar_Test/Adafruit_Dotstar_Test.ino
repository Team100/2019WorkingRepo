#include <SPI.h>
#include <Adafruit_DotStar.h>


#define NUMPIXELS 60


// Create the srtip


Adafruit_DotStar strip = Adafruit_DotStar(NUMPIXELS, DOTSTAR_BRG);

uint32_t color = 0x35FF00; //orange

void simpleOn(uint32_t color, int tail, int head) {
  for (int i=tail; i<head; i++){
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
      color = 0x00FF00; //red
      Serial.println('R');
      break;
    case 'G':
      color = 0xFF0000; //green
      Serial.println('G');
      break;
    case 'B':
      color = 0x0000FF; //blue
      Serial.println('B');
      break;
    case 'Y':
      color = 0xFFFF00; //yellow
      Serial.println('Y');
      break;
    case 'X':
      color = 0x000000; //off
      Serial.println('X');
      break;
  }
  }

  simpleOn(color, tail, head);
}
