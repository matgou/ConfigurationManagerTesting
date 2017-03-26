import { PipeTransform, Pipe, Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Response } from '@angular/http';
import { EventManager, ParseLinks, PaginationUtil, AlertService, DataUtils } from 'ng-jhipster';
import { Router } from '@angular/router';

import { Process } from '../entities/process/process.model';
import { Rule } from '../entities/rule/rule.model';

import { ProcessTree } from '../entities/process/processTree.model';
import { ProcessService } from '../entities/process/process.service';
import { RuleService } from '../entities/rule/rule.service';

@Pipe({name: 'toRuleSummary'})
export class ToRuleSummary implements PipeTransform {
  transform(value, args:string[]) : any {
    let keys = [];
    let rules = [];
    for (let key in value) {
        rules = this.processToRules(value[key], value[key].processName);
        keys = keys.concat(rules);
    }
    //console.log("Resultat toRuleSummary :");
    //console.log(keys);
    return keys;
  }
    
  private processToRules(p: ProcessTree, prefix: string) {
      let rules = [];
      //console.log("Start de la methode processToRules avec le process : ");
      //console.log(p);
      for(let key in p.rules) {
          let rule = p.rules[key];
          console.log(rule);
          rule.processName = prefix + ' / ' + p.processName;
          rule.processId = p.id;
          rule.ruleReportId = 0;
          rules.push(rule);
      }
      for(let key in p.childs) {
          let pChild = p.childs[key];
          rules = rules.concat(this.processToRules(pChild, prefix + ' / ' + p.processName));
      }
      
      //console.log("Resultat processToRules :");
      //console.log(rules);
      return rules;
  } 
}
@Pipe({name: 'toClass'})
export class ToClassPipe implements PipeTransform {
  transform(value, args:string[]) : any {
    switch(value) {
            case "Success":
                return "table-success";
            case "Unknown":
                return "table-warning ";
            case "Running":
                return "table-info";
            case "SoftFail":
                return "table-danger";
            case "HardFail":
                return "table-danger";
            case "ForceSuccess":
                return "table-success";
            default:
                return "";
    }
  }
}
@Pipe({name: 'toBadgeClass'})
export class ToBadgePipe implements PipeTransform {
  transform(value, args:string[]) : any {
    switch(value) {
            case "Success":
                return "badge-success";
            case "Unknown":
                return "badge-warning ";
            case "Running":
                return "badge-info";
            case "SoftFail":
                return "badge-danger";
            case "HardFail":
                return "badge-danger";
            case "ForceSuccess":
                return "badge-success";
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
        private ruleService: RuleService,
        private alertService: AlertService,
        private router: Router
    ) {
            this.processes = [];
        
        }

    ngOnInit() {
        this.processService.queryRoot().subscribe(
            (res: Response) => this.onSuccess(res.json(), res.headers),
            (res: Response) => this.onError(res.json())
        );
    }
    
    displayReport(rule: Rule) {
      this.ruleService.queryLastReport(rule.id).subscribe(
            (res: Response) => this.navigateToReport(res, res.headers),
            (res: Response) => this.onError(res)
      );
    }
  
    private navigateToReport(data, header) {
      this.router.navigate(["rule-report/", data.id]);
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
