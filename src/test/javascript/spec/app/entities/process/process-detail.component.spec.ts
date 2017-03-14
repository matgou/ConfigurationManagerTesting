import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcessDetailComponent } from '../../../../../../main/webapp/app/entities/process/process-detail.component';
import { ProcessService } from '../../../../../../main/webapp/app/entities/process/process.service';
import { Process } from '../../../../../../main/webapp/app/entities/process/process.model';

describe('Component Tests', () => {

    describe('Process Management Detail Component', () => {
        let comp: ProcessDetailComponent;
        let fixture: ComponentFixture<ProcessDetailComponent>;
        let service: ProcessService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [ProcessDetailComponent],
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
                    ProcessService
                ]
            }).overrideComponent(ProcessDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcessDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcessService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Process(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.process).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
