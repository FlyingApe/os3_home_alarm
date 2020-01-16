#include <ArduinoJson.h>
//token
const String token = "760bd43960df6149";

//alarm status
String alarmStatus;

//pins
const int buzzerPin = 2;
const int micAnalogPin = A0;
const int micDigitalPin = 13;
const int movementPin = 12;
const int distancePin = A1;

//sample size for analog sensors
const int micSamples = 100;
const int distanceSamples = 20;

//minimum average dinstance sensor value that will trigger the alarm 
const int distanceTriggerValue = 50;

//integerer value which decides if the audio is on or the alarm is in silent mode (0 for audioOn, 1 for silent mode)
boolean alarmAudioOn;

//creating a StaticJsonDocument to deal with incomming json over serial connection
StaticJsonDocument<128> receivabledoc;

//creating a StaticJsonDocument to create json to be send over serial connection
StaticJsonDocument<512> sendabledoc;

// the setup function runs once when you press reset or power the board
void setup() {
  Serial.begin(9600);
  
  pinMode(buzzerPin, OUTPUT);
  pinMode(movementPin, INPUT);
  pinMode(distancePin, INPUT);
  pinMode(micAnalogPin, INPUT);
  pinMode(micDigitalPin, INPUT);

  alarmAudioOn = true ;
  alarmStatus = "Active";
}

// the loop function runs over and over again forever
void loop() {
  sendabledoc.clear();
  receivabledoc.clear();
  
  String command;
  if (Serial.available()) {
    String json = Serial.readStringUntil(13);
    deserializeJson(receivabledoc, json);
    command = String(receivabledoc["command"].as<char*>());
    processCommand(command);
  }

  sendabledoc["token"] = token;
  getMicSample();
  getDistanceSample();
  checkMovementSensor();

  sendabledoc["status"] = alarmStatus;
  
  if(alarmAudioOn){
    sendabledoc["alarmAudioOn"] = "true";
  } else if (!alarmAudioOn){
    sendabledoc["alarmAudioOn"] = "false";
  }

  buzz();
  
  String out;
  serializeJsonPretty(sendabledoc, out); 
  Serial.print(out);
  Serial.print("\n\r");
}

//deal with received commands
void processCommand(String command){
  if(command == "setStatusActive") {
      alarmStatus = "Active";
  } else if (command == "setStatusInactive"){
      alarmStatus = "InActive";
  } else if (command == "setAudioOn"){
      alarmAudioOn = true;
  } else if (command == "setAudioOff"){
      alarmAudioOn = false;
  }
}

//methode to buzz the buzzer
void buzz(){
  if(alarmStatus == "InAlarm" && alarmAudioOn){
    /*
    digitalWrite(buzzerPin, HIGH);
    delay(500);
    digitalWrite(buzzerPin, LOW);
    */
  }
}

//get the average mic value over 
//delay in function defines the interval to check in milliseconds
//const int micSamples defines sample size
void getMicSample(){
  int totalMicValue = 0;
  for (int i = 0; i < micSamples; i++){
    int micValue = analogRead(micAnalogPin);
    if(micValue < 0){
      micValue = 0 - micValue;
    }
    totalMicValue += micValue;
    delay(10);
  }  
  int averageMicValue = totalMicValue / micSamples;
  sendabledoc["microphone"] = String(averageMicValue);
}

//get the average value for the distance sensor
//delay in function defines the interval to check in milliseconds
//const int distanceSamples defines sample size
void getDistanceSample(){
  int totalDistanceValue = 0;
  for (int i = 0; i < distanceSamples; i++){
    int distanceValue = analogRead(distancePin);
    totalDistanceValue += distanceValue;
    delay(10);
  }
  int averageDistanceValue = totalDistanceValue / distanceSamples;  
  sendabledoc["distance"] = String(averageDistanceValue);
  
  if(averageDistanceValue > distanceTriggerValue && alarmStatus != "InActive"){
    alarmStatus = "InAlarm";
  }
}

void checkMovementSensor(){
  if(digitalRead(movementPin) == 1){
    sendabledoc["movement"] = "1";
    if(alarmStatus != "InActive"){
      alarmStatus = "InAlarm";
    }
  } else if (digitalRead(movementPin) == 0){
    sendabledoc["movement"] = "0";
  }  
}
