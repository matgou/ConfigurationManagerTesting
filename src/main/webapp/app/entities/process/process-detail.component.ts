import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Process } from './process.model';
import { ProcessService } from './process.service';

@Component({
    selector: 'jhi-process-detail',
    templateUrl: './process-detail.component.html'
})
export class ProcessDetailComponent implements OnInit, OnDestroy {

    process: Process;
    private subscription: any;

    constructor(
        private processService: ProcessService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.processService.find(id).subscribe(process => {
            this.process = process;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
