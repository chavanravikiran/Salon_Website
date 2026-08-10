export interface FilterFieldOption {
  label: string;
  value: string | number | boolean;
}

export interface FilterFieldConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'boolean' | 'date' | 'select';
  options?: FilterFieldOption[];
}
