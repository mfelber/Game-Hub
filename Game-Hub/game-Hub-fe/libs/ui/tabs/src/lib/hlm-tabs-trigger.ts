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
    ` relative z-10 inline-flex h-[42px] items-center justify-start whitespace-nowrap
    before:absolute
    before:bottom-[-1px]
    before:left-0
    before:right-0
    before:h-px
    before:bg-[#151923]
    before:content-['']
    before:opacity-0
    data-active:before:opacity-100
    gap-1.5 px-6
    text-sm font-medium
    text-white/55
    rounded-t-md
    transition-colors duration-200
    hover:cursor-pointer
    hover:text-white
    hover:bg-[#151B27]/60
    data-active:text-white
    data-active:!bg-[#151923]
    data-active:!border-b-0
    data-active:!border-white/15
    border-t border-x border-white/15
    group-data-[orientation=vertical]/tabs:w-full
    group-data-[orientation=vertical]/tabs:justify-start
    group-data-[variant=line]/tabs-list:data-active:shadow-none
    focus-visible:outline-none
    focus-visible:ring-2
    focus-visible:ring-[#3B82F6]/40
    disabled:pointer-events-none
    disabled:opacity-50
    [&_ng-icon]:pointer-events-none
    [&_ng-icon]:shrink-0`,

    `group-data-[variant=line]/tabs-list:bg-transparent`,

    `after:absolute
    after:bottom-0
    after:left-0
    after:right-0
    after:h-0.5
    after:rounded-full
    after:bg-[#3B82F6]
    after:opacity-0
    after:transition-opacity
    after:duration-200
    group-data-[variant=line]/tabs-list:data-active:after:opacity-0`,
    ]);
  }
}
