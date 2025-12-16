import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Category {
  id: string;
  name: string;
}

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent implements OnInit {
  categories = signal<Category[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  message = signal<string | null>(null);
  
  view = signal<'list' | 'form'>('list');
  formData = signal({ name: '', description: '' });

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.loading.set(true);
    this.error.set(null);
    
    this.http.get<Category[]>('http://localhost:8080/genres').subscribe({
      next: (data) => {
        this.categories.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load categories. Make sure Genre Service is running on port 8080.');
        this.loading.set(false);
      }
    });
  }

  deleteCategory(categoryId: string, categoryName: string) {
    if (!confirm(`Delete "${categoryName}"?`)) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.message.set(null);

    this.http.delete(`http://localhost:8080/genres/${categoryId}`).subscribe({
      next: () => {
        this.categories.update(cats => cats.filter(c => c.id !== categoryId));
        this.message.set(`Deleted: "${categoryName}"`);
        this.loading.set(false);
        setTimeout(() => this.message.set(null), 2500);
      },
      error: (err) => {
        this.error.set(`Failed to delete: ${err.message}`);
        this.loading.set(false);
      }
    });
  }

  createSampleData() {
    this.loading.set(true);
    this.error.set(null);
    this.message.set(null);

    // POST to both services
    Promise.all([
      this.http.post('http://localhost:8080/admin/sample-data', {}).toPromise(),
      this.http.post('http://localhost:8081/admin/sample-data', {}).toPromise()
    ]).then(() => {
      this.message.set('Sample data created successfully!');
      this.loading.set(false);
      setTimeout(() => this.loadCategories(), 500);
    }).catch((err) => {
      this.error.set('Failed to create sample data');
      this.loading.set(false);
      console.error('Error creating sample data:', err);
    });
  }

  showForm() {
    this.view.set('form');
    this.formData.set({ name: '', description: '' });
    this.error.set(null);
    this.message.set(null);
  }

  cancelForm() {
    this.view.set('list');
    this.formData.set({ name: '', description: '' });
  }

  saveCategory() {
    const name = this.formData().name.trim();
    
    if (!name) {
      this.error.set('Category name cannot be empty');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.message.set(null);

    const newCategory = {
      name,
      description: this.formData().description.trim() || ''
    };

    this.http.post<Category>('http://localhost:8080/genres', newCategory).subscribe({
      next: () => {
        this.message.set('Category created successfully');
        this.formData.set({ name: '', description: '' });
        this.view.set('list');
        this.loading.set(false);
        this.loadCategories();
      },
      error: (err) => {
        this.error.set('Failed to create category: ' + (err.error?.message || err.message));
        this.loading.set(false);
      }
    });
  }
}
