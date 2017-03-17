import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'ng-jhipster';
import { Scheduling } from './scheduling.model';
import { SchedulingService } from './scheduling.service';

@Component({
    selector: 'jhi-scheduling-detail',
    templateUrl: './scheduling-detail.component.html'
})
export class SchedulingDetailComponent implements OnInit, OnDestroy {

    scheduling: Scheduling;
    private subscription: any;

    constructor(
        private dataUtils: DataUtils,
        private schedulingService: SchedulingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.schedulingService.find(id).subscribe(scheduling => {
            this.scheduling = scheduling;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
