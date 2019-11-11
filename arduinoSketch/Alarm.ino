const int buzzerPin = 2;
const int micAnalogPin = A0;
const int micDigitalPin = 13;
const int movementPin = 12;
const int distancePin = A1;

const int micSamples = 100;
const int distanceSamples = 20;

const int distanceTriggerValue = 50;

boolean soundAlarm;

int inByte;

// the setup function runs once when you press reset or power the board
void setup() {
  Serial.begin(9600);
  
  pinMode(buzzerPin, OUTPUT);
  pinMode(movementPin, INPUT);
  pinMode(distancePin, INPUT);
  pinMode(micAnalogPin, INPUT);
  pinMode(micDigitalPin, INPUT);

  soundAlarm = false;
}

// the loop function runs over and over again forever
void loop() {
  if (Serial.available()) {
    inByte = Serial.read();
  }
  
  if(soundAlarm && inByte == 1){
    digitalWrite(buzzerPin, HIGH);
    delay(500);
    digitalWrite(buzzerPin, LOW);
  }
  
  int totalMicValue = 0;
  for (int i = 0; i < micSamples; i++){
    int micValue = analogRead(micAnalogPin);
    totalMicValue += micValue;
    delay(10);
  }
  int averageMicValue = totalMicValue / micSamples;

  int totalDistanceValue = 0;
  for (int i = 0; i < distanceSamples; i++){
    int distanceValue = analogRead(distancePin);
    totalDistanceValue += distanceValue;
    delay(10);
  }
  int averageDistanceValue = totalDistanceValue / distanceSamples;

  if(averageDistanceValue > distanceTriggerValue){
    soundAlarm = true;
  }
  
  int movementValue = digitalRead(movementPin);

  if(movementValue == 1){
    soundAlarm = true;
  }
  
  //int adjustedValue = (sensorValue - 560) * 10;
  
   Serial.print("{\n");
   Serial.print("\"microfone\":\"");
   Serial.print(averageMicValue);
   Serial.print("\",");
   Serial.print("\"distance\":\"");
   Serial.print(averageDistanceValue);
   Serial.print("\",");
   Serial.print("\"movement\":\"");
   Serial.print(movementValue);
   Serial.print("\",");
   Serial.print("\"inByte\":\"");
   Serial.print(inByte);
   Serial.print("\"\n");
   Serial.print("}\n");
   delay(1000);
 
}
