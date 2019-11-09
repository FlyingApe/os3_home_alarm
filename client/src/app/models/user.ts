import {Alarm} from './alarm';

export class User {
    public id: number;
    public alarms: Alarm[];
    public clientConnections: WebSocket;
}
