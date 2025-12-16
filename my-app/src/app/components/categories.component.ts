import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Category {
  id: string;
  name: string;
  description?: string;
}

interface Movie {
  id: string;
  title: string;
  releaseYear?: number;
  rating?: number;
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
  
  view = signal<'list' | 'create' | 'edit' | 'details'>('list');
  formData = signal({ name: '', description: '' });
  
  // For edit view
  editingId = signal<string | null>(null);
  
  // For details view
  selectedCategory = signal<Category | null>(null);
  elements = signal<Movie[]>([]);

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
        this.error.set('Failed to load categories. Make sure Gateway Service is running on port 8090.');
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
    this.view.set('create');
    this.formData.set({ name: '', description: '' });
    this.error.set(null);
    this.message.set(null);
  }

  showEditForm(category: Category) {
    this.view.set('edit');
    this.editingId.set(category.id);
    this.loading.set(true);
    this.error.set(null);
    
    // Fetch full category details including description
    this.http.get<Category>(`http://localhost:8080/genres/${category.id}`).subscribe({
      next: (fullCategory) => {
        this.formData.set({ 
          name: fullCategory.name, 
          description: fullCategory.description || '' 
        });
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load category details');
        this.loading.set(false);
        this.view.set('list');
      }
    });
  }

  cancelForm() {
    this.view.set('list');
    this.formData.set({ name: '', description: '' });
    this.editingId.set(null);
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

    const categoryData = {
      name,
      description: this.formData().description.trim() || ''
    };

    if (this.view() === 'create') {
      this.http.post<Category>('http://localhost:8080/genres', categoryData).subscribe({
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
    } else if (this.view() === 'edit' && this.editingId()) {
      this.http.put(`http://localhost:8080/genres/${this.editingId()}`, categoryData).subscribe({
        next: () => {
          this.message.set('Category updated successfully');
          this.formData.set({ name: '', description: '' });
          this.editingId.set(null);
          this.view.set('list');
          this.loading.set(false);
          this.loadCategories();
        },
        error: (err) => {
          this.error.set('Failed to update category: ' + (err.error?.message || err.message));
          this.loading.set(false);
        }
      });
    }
  }

  showDetails(category: Category) {
    this.view.set('details');
    this.error.set(null);
    this.message.set(null);
    this.elements.set([]);
    this.loading.set(true);
    
    // Fetch full category details including description
    this.http.get<Category>(`http://localhost:8080/genres/${category.id}`).subscribe({
      next: (fullCategory) => {
        this.selectedCategory.set(fullCategory);
        this.loading.set(false);
        this.loadCategoryElements(category.id);
      },
      error: (err) => {
        this.error.set('Failed to load category details');
        this.loading.set(false);
        this.view.set('list');
      }
    });
  }

  loadCategoryElements(categoryId: string) {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<Movie[]>(`http://localhost:8081/genres/${categoryId}/movies`).subscribe({
      next: (data) => {
        this.elements.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load elements. Make sure Gateway Service is running on port 8090.');
        this.loading.set(false);
      }
    });
  }

  deleteElement(elementId: string, elementTitle: string) {
    if (!confirm(`Remove "${elementTitle}" from this category?`)) {
      return;
    }

    const categoryId = this.selectedCategory()?.id;
    if (!categoryId) return;

    this.loading.set(true);
    this.error.set(null);
    this.message.set(null);

    this.http.delete(`http://localhost:8081/genres/${categoryId}/movies/${elementId}`).subscribe({
      next: () => {
        this.message.set(`Removed: "${elementTitle}"`);
        this.loading.set(false);
        this.loadCategoryElements(categoryId);
        setTimeout(() => this.message.set(null), 2500);
      },
      error: (err) => {
        this.error.set(`Failed to remove element: ${err.message}`);
        this.loading.set(false);
      }
    });
  }

  backToList() {
    this.view.set('list');
    this.selectedCategory.set(null);
    this.elements.set([]);
  }
}
