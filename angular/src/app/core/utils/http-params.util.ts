import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../models/page.model';

export function buildPageParams(query: PageQuery, filters?: Record<string, unknown>): HttpParams {
  let params = new HttpParams()
    .set('page', String(query.page))
    .set('size', String(query.size));

  if (query.sortBy) {
    params = params.set('sortBy', query.sortBy);
  }
  if (query.sortDir) {
    params = params.set('sortDir', query.sortDir);
  }

  if (filters) {
    for (const key of Object.keys(filters)) {
      const value = filters[key];
      if (value === null || value === undefined || value === '') {
        continue;
      }
      params = params.set(key, String(value));
    }
  }

  return params;
}
