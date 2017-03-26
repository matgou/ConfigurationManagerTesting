import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { RuleReport } from '../rule-report/rule-report.model';
import { Rule } from './rule.model';
@Injectable()
export class RuleService {

    private resourceUrl = 'api/rules';

    constructor(private http: Http) { }

    create(rule: Rule): Observable<Rule> {
        let copy: Rule = Object.assign({}, rule);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(rule: Rule): Observable<Rule> {
        let copy: Rule = Object.assign({}, rule);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Rule> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    execute(id: number): Observable<Rule> {
        return this.http.post(`${this.resourceUrl}/${id}/execute`, {}).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
        ;
    }
    
    queryLastReport(id: number): Observable<RuleReport> {
        return this.http.get(`${this.resourceUrl}/${id}/lastReport`).map((res: Response) => {
            return res.json();
        });
    }
  
    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }



    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
