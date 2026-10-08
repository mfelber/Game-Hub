import {Component} from '@angular/core';
import {
  HlmDialog, HlmDialogClose,
  HlmDialogContent, HlmDialogDescription, HlmDialogFooter,
  HlmDialogHeader,
  HlmDialogPortal,
  HlmDialogTitle,
  HlmDialogTrigger
} from '@spartan/dialog';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import {HlmInputGroup, HlmInputGroupInput} from '@spartan/input-group';
import {HlmDropdownMenu, HlmDropdownMenuItem, HlmDropdownMenuTrigger} from '@spartan/dropdown-menu';
import {NewsControllerService} from '../../../../../../services/services/news-controller.service';

@Component({
  selector: 'app-news',
  imports: [
    HlmDialog,
    HlmDialogTrigger,
    HlmDialogPortal,
    HlmDialogContent,
    HlmDialogHeader,
    HlmDialogTitle,
    HlmDialogDescription,
    HlmDialogFooter,
    HlmDialogClose,
    FormsModule,
    HlmInputGroup,
    HlmInputGroupInput,
    ReactiveFormsModule,
    HlmDropdownMenu,
    HlmDropdownMenuItem,
    HlmDropdownMenuTrigger
  ],
  templateUrl: './news.component.html',
  styleUrl: './news.component.scss',
})
export class NewsComponent {

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private newsService: NewsControllerService
  ) {
    this.form = this.fb.group({
      version: [''],
      title: [''],
      sections: this.fb.array([])
    });
  }

  get section() {
    return this.form.get('sections') as FormArray;
  }

  getNewsItems(index: number) {
    return this.section.at(index).get('newsItems') as FormArray;
  }


  addSection() {
    this.section.push(this.fb.group({
      newsType: ['ADDED'],
      newsItems: this.fb.array([])
    }))
  }

  deleteSection(index: number) {
    this.section.removeAt(index);
  }

  deleteNewsItems(index: number, itemIndex: number) {
    this.getNewsItems(index).removeAt(itemIndex);
  }

  addDescription(index: number) {
    this.getNewsItems(index).push(this.fb.group({
      description: ['']
    }))
  }

  selectNewsType(section: AbstractControl, type: string) {
    section.get('newsType')?.setValue(type)
  }

  submitNews() {
    this.newsService.createNews({body: this.form.value}).subscribe({
      next: (res) => {
        console.log("news created");
        console.log(res);
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  resetForm() {
    this.form.reset({
      version: '',
      title: ''
    });

    this.section.clear();
  }
}
