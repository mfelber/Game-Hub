import { Directive } from '@angular/core';
import { classes } from '@spartan/utils';

@Directive({
	selector: '[hlmDialogFooter],hlm-dialog-footer',
	host: { 'data-slot': 'dialog-footer' },
})
export class HlmDialogFooter {
	constructor() {
		classes(() => 'bg-[#10131a] -mx-4 -mb-4 rounded-b-[18px] ' +
      'border-t border-[rgba(255,255,255,0.12)] p-4 ' +
      'flex flex-col-reverse gap-2 sm:flex-row sm:justify-end');
	}
}
