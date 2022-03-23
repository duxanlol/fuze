// @ts-ignore

import {Component, Injectable, OnInit} from '@angular/core';
import {Session} from "../../@core/data/session";
import {Config} from "../../@core/data/config";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Component({
  selector: 'app-sessions-component',
  templateUrl: './sessions-component.component.html',
  styleUrls: ['./sessions-component.component.css']
})

@Injectable({
  providedIn: 'root',
})
export class SessionsComponentComponent implements OnInit {


  ngOnInit(): void {
    this.allSessions().subscribe(data => {
      this.sessions = data;
      this.sessions.forEach(session => {
        session.sectors.forEach(sector => {
          sector.picked = sector.guesses[0];
        })
      })

    })
  }

  sessions: Session[];
  excelPath : string;
  dirPath : string;
  hidden : boolean;

  constructor(private http: HttpClient) {
  }

  private httpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  allSessions(): Observable<Session[]> {
    const url = "http://localhost:8080/main/test";
    return new Observable((o: any) => {
      this.http.get<Session[]>(url).subscribe((data: Session[]) => {
        o.next(data);
        return o.complete();
      });

    });
  }

  configure(){
    const url = "http://localhost:8080/main/setConfig";
    console.log(this.excelPath, this.dirPath);
    let config: Config = {
      dirPath: this.dirPath,
      excel: this.excelPath,
    };
    this.http.post('http://localhost:8080/main/setConfig', config, {headers: this.httpHeaders})
      .subscribe(res => {
        console.log('inside postmehtod of sub.function', res);//only objects
      })

  }

  exportSession(session: Session) {
    const url = "http://localhost:8080/main/exportSession";
    this.http.post('http://localhost:8080/main/exportSession', session, {headers: this.httpHeaders})
      .subscribe(res => {
        console.log('inside postmehtod of sub.function', res);//only objects
      })
  }
}
