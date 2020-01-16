import DateTimeFormat = Intl.DateTimeFormat;

export class Alarm {
  public id: number;
  public token: string;
  public user: string;
  public distance: number;
  public movement: number;
  public microphone: number;
  public status: AlarmStatus;
  public audioOn: boolean;
  // public events: Event[];
  // public sensors: Sensor[];
  // public socket: WebSocket;
}

export enum AlarmStatus {
  Active,
  Inactive,
  Inoperable,
  InAlarm,
  Disconnected
}

// export class Sensor {
//   public sensorStatus: boolean;
//   public sensorData: SensorData;
// }

// export interface SensorData {
//   GetData(): number;
// }

// export class MicrophoneData implements SensorData {
//   private decibel: number;

//   GetData(): number {
//     return this.decibel;
//   }
// }

// export class InfraredData implements SensorData {
//   /// TODO: Define metric definition for field 'range'.
//   private range: number;

//   GetData(): number {
//     return this.range;
//   }
// }

// export class Event {
//   public dateTime: DateTimeFormat;
//   public sensor: Sensor;
//   /// TODO: Implement event typing.
// }
