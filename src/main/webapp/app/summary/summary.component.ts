import { PipeTransform, Pipe, Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Response } from '@angular/http';
import { EventManager, ParseLinks, PaginationUtil, AlertService, DataUtils } from 'ng-jhipster';

import { Process } from '../entities/process/process.model';
import { ProcessTree } from '../entities/process/processTree.model';
import { ProcessService } from '../entities/process/process.service';

@Pipe({name: 'toClass'})
export class ToClassPipe implements PipeTransform {
  transform(value, args:string[]) : any {
    switch(value) {
            case "Success":
                return "table-success";
            default:
                return "";
    }
  }
}

@Component({
    selector: 'jhi-summary',
    templateUrl: './summary.component.html',
})
export class SummaryComponent implements OnInit {
    modalRef: NgbModalRef;
    processes: ProcessTree[];
    totalItems: any;

    constructor(
        private processService: ProcessService,
        private alertService: AlertService,
    ) {
            this.processes = [];
        
        }

    ngOnInit() {
        this.processService.queryRoot().subscribe(
            (res: Response) => this.onSuccess(res.json(), res.headers),
            (res: Response) => this.onError(res.json())
        );
    }
    
    
    private onSuccess(data, headers) {
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.processes.push(data[i]);
        }
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
