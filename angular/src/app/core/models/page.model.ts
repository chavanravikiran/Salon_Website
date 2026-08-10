export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}

export interface PageQuery {
  page: number;
  size: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}
