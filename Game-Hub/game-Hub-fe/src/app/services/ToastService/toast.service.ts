import { Injectable } from '@angular/core';
import {toast} from '@spartan-ng/brain/sonner';

@Injectable({
  providedIn: 'root',
})
export class ToastService {

  success(message: string): void {
    toast.success(message, {
      position: 'top-right',
      classes: {
        toast: '!bg-[#16a34a] !border-[#16a34a]/40 !text-white',
        title: '!text-white',
        description: '!text-gray-400',
      }
    });
  }

  error(message: string): void {
    toast.error(message, {
      position: 'top-right',
      classes: {
        toast: '!bg-[#ef4444] !border-[#ef4444]/40 !text-white',
        title: '!text-white',
        description: '!text-gray-400',
      }
    });
  }

}
