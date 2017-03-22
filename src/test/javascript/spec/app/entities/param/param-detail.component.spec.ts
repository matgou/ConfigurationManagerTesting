import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ParamDetailComponent } from '../../../../../../main/webapp/app/entities/param/param-detail.component';
import { ParamService } from '../../../../../../main/webapp/app/entities/param/param.service';
import { Param } from '../../../../../../main/webapp/app/entities/param/param.model';

describe('Component Tests', () => {

    describe('Param Management Detail Component', () => {
        let comp: ParamDetailComponent;
        let fixture: ComponentFixture<ParamDetailComponent>;
        let service: ParamService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [ParamDetailComponent],
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
                    ParamService
                ]
            }).overrideComponent(ParamDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParamDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParamService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Param(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.param).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
