import {type BooleanInput} from '@angular/cdk/coercion';
import {CdkMenuItem} from '@angular/cdk/menu';
import {booleanAttribute, Directive, HOST_TAG_NAME, inject, input} from '@angular/core';
import {classes} from '@spartan/utils';
import {HlmDropdownMenuFocusOnHover} from './hlm-dropdown-menu-focus-on-hover';

@Directive({
  selector: '[hlmDropdownMenuItem],hlm-dropdown-menu-item',
  hostDirectives: [
    {
      directive: CdkMenuItem,
      inputs: ['cdkMenuItemDisabled: disabled'],
      outputs: ['cdkMenuItemTriggered: triggered'],
    },
    HlmDropdownMenuFocusOnHover,
  ],
  host: {
    'data-slot': 'dropdown-menu-item',
    '[attr.disabled]': '_isButton && disabled() ? "" : null',
    '[attr.data-disabled]': 'disabled() ? "" : null',
    '[attr.data-variant]': 'variant()',
    '[attr.data-inset]': 'inset() ? "" : null',
  },
})
export class HlmDropdownMenuItem {
  protected readonly _isButton = inject(HOST_TAG_NAME) === 'button';

  public readonly disabled = input<boolean, BooleanInput>(false, {transform: booleanAttribute});

  public readonly variant = input<'default' | 'destructive'>('default');

  public readonly inset = input<boolean, BooleanInput>(false, {
    transform: booleanAttribute,
  });

  constructor() {
    classes(
      () =>
        "hover:bg-[#26334a] focus:bg-[#26334a] " +
        "text-[#ddd] hover:text-[#ddd] focus:text-[#ddd] " +
        "gap-1.5 rounded-[7px] px-[15px] py-2.5 text-sm " +
        "data-inset:ps-7 " +
        "[&_ng-icon:not([class*='text-'])]:text-[length:--spacing(4)] " +
        "group/dropdown-menu-item relative flex w-full cursor-pointer " +
        "items-center outline-hidden select-none " +
        "data-disabled:pointer-events-none data-disabled:opacity-50 " +
        "[&_ng-icon]:pointer-events-none [&_ng-icon]:shrink-0",
    );
  }
}
