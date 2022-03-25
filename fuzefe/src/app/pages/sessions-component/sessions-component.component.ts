// @ts-ignore

import {Component, ElementRef, Injectable, OnInit} from '@angular/core';
import {Session} from "../../@core/data/session";
import {Config} from "../../@core/data/config";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Sector} from "../../@core/data/sector";

@Component({
  selector: 'app-sessions-component',
  templateUrl: './sessions-component.component.html',
  styleUrls: ['./sessions-component.component.css']
})

@Injectable({
  providedIn: 'root',
})
export class SessionsComponentComponent implements OnInit {

  insert = (arr: any[], index: any, newItem: any) => [
    // part of the array before the specified index
    ...arr.slice(0, index),
    // inserted item
    newItem,
    // part of the array after the specified index
    ...arr.slice(index)
  ]

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

  tdClick(sector: Sector, session: Session, indexOfelement: number){
    console.log(sector);
    console.log(indexOfelement)
    let newSector = new Sector();
    newSector.activity = "#Added item.#";
    newSector.lesson = "";
    newSector.guesses = sector.guesses;
    newSector.picked = sector.guesses[sector.guesses.length];
    session.sectors = this.insert(session.sectors, indexOfelement+1, newSector)

  }

  exportSession(session: Session) {
    const url = "http://localhost:8080/main/exportSession";
    let notNull = false;
    session.sectors.forEach( sector =>{
      if (!sector.picked.fileName.includes("NULL")){
        notNull = true;
      }
    })
    console.log("NOT NULL IS " + notNull);
    if(!notNull) {
      alert("Can't export when all files are NULL.");
      return;
    }
    this.http.post('http://localhost:8080/main/exportSession', session, {headers: this.httpHeaders})
      .subscribe(res => {
        console.log('inside postmehtod of sub.function', res);//only objects
      })
  }

  removeSector(session: Session, indexOfelement: number) {
    session.sectors.splice(indexOfelement,1);

  }
}
