import { Pipe, PipeTransform } from '@angular/core';
import { UploadService } from '../services/upload.service';

@Pipe({ name: 'imageUrl' })
export class ImageUrlPipe implements PipeTransform {
  constructor(private uploadService: UploadService) {}

  transform(value: string | undefined | null): string {
    return this.uploadService.resolveUrl(value);
  }
}
