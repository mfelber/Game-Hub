import { Directive } from '@angular/core';
import { BrnFieldControlDescribedBy } from '@spartan-ng/brain/field';
import { BrnTextarea } from '@spartan-ng/brain/textarea';
import { classes } from '@spartan/utils';

@Directive({
	selector: '[hlmTextarea]',
	hostDirectives: [{ directive: BrnTextarea, inputs: ['id', 'forceInvalid'] }, BrnFieldControlDescribedBy],
	host: { 'data-slot': 'textarea' },
})
export class HlmTextarea {
	constructor() {
		classes(
			() =>
        'border border-[rgba(255,255,255,0.12)] ' +
        'bg-[#10131a] text-[#ddd] ' +
        'focus-visible:border-[#4da3ff] ' +
        'focus-visible:ring-2 focus-visible:ring-[rgba(77,163,255,0.15)] ' +
        'data-[matches-spartan-invalid=true]:border-red-500 ' +
        'data-[matches-spartan-invalid=true]:ring-2 ' +
        'data-[matches-spartan-invalid=true]:ring-red-500/20 ' +
        'disabled:bg-[#151923] rounded-[10px] ' +
        'px-2.5 py-2 text-base transition-all ' +
        'md:text-sm placeholder:text-[#777] ' +
        'flex field-sizing-content min-h-16 w-full outline-none ' +
        'disabled:cursor-not-allowed disabled:opacity-50',
		);
	}
}
