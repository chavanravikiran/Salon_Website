import { Component, OnInit } from '@angular/core';
import { GalleryImage } from 'src/app/core/models/gallery-image.model';
import { GalleryApiService } from 'src/app/core/services/gallery-api.service';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html'
})
export class GalleryComponent implements OnInit {
  images: GalleryImage[] = [];

  constructor(private galleryApi: GalleryApiService) {}

  ngOnInit(): void {
    this.galleryApi.getAll().subscribe(images => (this.images = images));
  }
}
