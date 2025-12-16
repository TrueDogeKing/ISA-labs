import { Component } from '@angular/core';
import { CategoriesComponent } from './components/categories.component';

@Component({
  selector: 'app-root',
  imports: [CategoriesComponent],
  template: '<app-categories></app-categories>',
  styleUrl: './app.css'
})
export class App {}
