import { Injectable } from '@angular/core';
import { Subject, Observer, Observable } from 'rxjs/Rx';
@Injectable()

export class WebsocketService {
  private window:Window;
      
  constructor(_window: Window) {
      this.window = _window;
  }
    
  public createWebsocket(): Subject<MessageEvent> {
        let location = this.window.location;
        let url = "";
        if(location.protocol == "http:") {
            url = "ws://" + location.host + "/order66";  
        } else {
            url = "wss://" + location.host + "/order66";
        }
        let socket = new WebSocket(url);
        let observable = Observable.create(
                    (observer: Observer<MessageEvent>) => {
                        socket.onmessage = observer.next.bind(observer);
                        socket.onerror = observer.error.bind(observer);
                        socket.onclose = observer.complete.bind(observer);
                        return socket.close.bind(socket);
                    }
        );
        let observer = {
                next: (data: Object) => {
                    if (socket.readyState === WebSocket.OPEN) {
                        socket.send(JSON.stringify(data));
                    }
                }
        };
        return Subject.create(observer, observable);
  }
}
