import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SchedulingDetailComponent } from '../../../../../../main/webapp/app/entities/scheduling/scheduling-detail.component';
import { SchedulingService } from '../../../../../../main/webapp/app/entities/scheduling/scheduling.service';
import { Scheduling } from '../../../../../../main/webapp/app/entities/scheduling/scheduling.model';

describe('Component Tests', () => {

    describe('Scheduling Management Detail Component', () => {
        let comp: SchedulingDetailComponent;
        let fixture: ComponentFixture<SchedulingDetailComponent>;
        let service: SchedulingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [SchedulingDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    SchedulingService
                ]
            }).overrideComponent(SchedulingDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SchedulingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SchedulingService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Scheduling(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.scheduling).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
