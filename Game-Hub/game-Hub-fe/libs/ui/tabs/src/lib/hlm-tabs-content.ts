import { Directive, input } from '@angular/core';
import { BrnTabsContent } from '@spartan-ng/brain/tabs';
import { classes } from '@spartan/utils';

@Directive({
	selector: '[hlmTabsContent]',
	hostDirectives: [{ directive: BrnTabsContent, inputs: ['brnTabsContent: hlmTabsContent'] }],
	host: {
		'data-slot': 'tabs-content',
	},
})
export class HlmTabsContent {
	public readonly contentFor = input.required<string>({ alias: 'hlmTabsContent' });

	constructor() {
    classes(() =>
      'flex-1 text-sm outline-none bg-[#151923] rounded-r-md rounded-bl-md border border-white/15 pl-2 '
    );
	}
}
