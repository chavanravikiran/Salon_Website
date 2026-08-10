import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FilterFieldConfig } from './filter-field.model';

@Component({
  selector: 'app-filter-bar',
  templateUrl: './filter-bar.component.html'
})
export class FilterBarComponent implements OnChanges {
  @Input() fields: FilterFieldConfig[] = [];
  @Output() filterChange = new EventEmitter<Record<string, unknown>>();

  open = false;
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({});
  }

  ngOnChanges(): void {
    const controls: Record<string, unknown> = {};
    for (const field of this.fields) {
      controls[field.key] = [null];
    }
    this.form = this.fb.group(controls);
  }

  toggle(): void {
    this.open = !this.open;
  }

  apply(): void {
    this.filterChange.emit(this.form.value);
    this.open = false;
  }

  clear(): void {
    this.form.reset();
    this.filterChange.emit(this.form.value);
    this.open = false;
  }

  get activeCount(): number {
    return Object.values(this.form.value || {}).filter(v => v !== null && v !== undefined && v !== '').length;
  }
}
