import {Directive, input} from '@angular/core';
import {BrnTabsTrigger} from '@spartan-ng/brain/tabs';
import {classes} from '@spartan/utils';

@Directive({
  selector: '[hlmTabsTrigger]',
  hostDirectives: [{directive: BrnTabsTrigger, inputs: ['brnTabsTrigger: hlmTabsTrigger', 'disabled']}],
  host: {
    'data-slot': 'tabs-trigger',
  },
})
export class HlmTabsTrigger {
  public readonly triggerFor = input.required<string>({alias: 'hlmTabsTrigger'});

  constructor() {
    classes(() => [
    `       'inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 data-[state=active]:bg-background data-[state=active]:text-foreground data-[state=active]:shadow-sm',
`,

    ]);
  }
}
